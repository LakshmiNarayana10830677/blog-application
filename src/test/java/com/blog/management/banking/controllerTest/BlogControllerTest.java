package com.blog.management.banking.controllerTest;

import com.blog.management.banking.controller.BlogController;
import com.blog.management.banking.dto.BlogPost;
import com.blog.management.banking.entity.User;
import com.blog.management.banking.repository.BlogPostRepository;
import com.blog.management.banking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlogControllerTest {

    @InjectMocks
    private BlogController blogController;

    @Mock
    private UserRepository userRepo;

    @Mock
    private BlogPostRepository blogRepo;

    private Principal principal;

    @BeforeEach
    void setUp () {
        principal = () -> "testUser";
    }

    @Test
    void testCreateBlogSuccess() {
        User user = new User();
        user.setUsername("testUser");
        BlogPost blog = new BlogPost();
        blog.setTitle("Test Blog");
        BlogPost savedBlog = new BlogPost();
        savedBlog.setId(1L);
        savedBlog.setTitle("Test Blog");
        when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(blogRepo.save(any(BlogPost.class))).thenReturn(savedBlog);
        ResponseEntity<?> response = blogController.createBlog(blog, principal);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedBlog, response.getBody());
    }
    @Test
    void testCreateBlogUserNotFound() {
        BlogPost blog = new BlogPost();
        when(userRepo.findByUsername("testUser")).thenReturn(Optional.empty());
        ResponseEntity<?> response = blogController.createBlog(blog, principal);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error creating blog post", response.getBody());
    }
    @Test
    void testGetAllBlogs() {
        List<BlogPost> blogs = List.of(new BlogPost(), new BlogPost());
        when(blogRepo.findAll()).thenReturn(blogs);
        ResponseEntity<List<BlogPost>> response = blogController.getAllBlogs();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(blogs, response.getBody());
    }
}
