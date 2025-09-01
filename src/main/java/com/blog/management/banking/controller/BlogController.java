package com.blog.management.banking.controller;

import com.blog.management.banking.dto.BlogPost;
import com.blog.management.banking.entity.User;
import com.blog.management.banking.exception.ResourceNotFoundException;
import com.blog.management.banking.repository.BlogPostRepository;
import com.blog.management.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogPostRepository blogRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping
    public ResponseEntity<?> createBlog(@RequestBody BlogPost blog, Principal principal) {
        try {
            User user = userRepo.findByUsername(principal.getName())
            .orElseThrow(() -> new ResourceNotFoundException ("User not found"));
            blog.setAuthor(user);
            BlogPost savedBlog = blogRepo.save(blog);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBlog);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating blog post");
        }
    }

    @GetMapping
    public ResponseEntity<List<BlogPost>> getAllBlogs() {
        List<BlogPost> blogs = blogRepo.findAll();
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyBlogs(Principal principal) {
        try {
            User user = userRepo.findByUsername(principal.getName())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            List<BlogPost> myBlogs = blogRepo.findByAuthorId(user.getId());
            return ResponseEntity.ok(myBlogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user blogs");
        }
    }
}