package com.blog.management.banking.controller;

import com.blog.management.banking.dto.BlogPost;
import com.blog.management.banking.dto.Comment;
import com.blog.management.banking.entity.User;
import com.blog.management.banking.exception.ResourceNotFoundException;
import com.blog.management.banking.repository.BlogPostRepository;
import com.blog.management.banking.repository.CommentRepository;
import com.blog.management.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private BlogPostRepository blogRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/{blogId}")
    public ResponseEntity<?> addComment(@PathVariable Long blogId,
                                        @RequestBody Comment comment,
                                        Principal principal) {
        try {
            BlogPost blog = blogRepo.findById(blogId)
                    .orElseThrow(() -> new ResourceNotFoundException ("Blog post not found"));
            User user = userRepo.findByUsername(principal.getName())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            comment.setBlogPost(blog);
            comment.setCommenter(user);
            Comment savedComment = commentRepo.save(comment);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add comment"));
        }
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<?> getComments(@PathVariable Long blogId) {
        try {
            List<Comment> comments = commentRepo.findByBlogPostId(blogId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch comments"));
        }
    }
}