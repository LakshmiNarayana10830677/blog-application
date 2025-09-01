package com.blog.management.banking.controllerTest;

import com.blog.management.banking.controller.CommentController;
import com.blog.management.banking.dto.BlogPost;
import com.blog.management.banking.dto.Comment;
import com.blog.management.banking.entity.User;
import com.blog.management.banking.repository.BlogPostRepository;
import com.blog.management.banking.repository.CommentRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentRepository commentRepo;

    @Mock
    private BlogPostRepository blogRepo;

    @Mock
    private UserRepository userRepo;

    private Principal principal;

    @BeforeEach
    void setUp() {
        principal = () -> "testUser";
    }

    @Test
    void testAddCommentSuccess() {
        BlogPost blog = new BlogPost();
        blog.setId(1L);
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        Comment comment = new Comment();
        comment.setContent("Nice post!");
        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setContent("Nice post!");

        when(blogRepo.findById(1L)).thenReturn(Optional.of(blog));
        when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(commentRepo.save(any(Comment.class))).thenReturn(savedComment);
        ResponseEntity<?> response = commentController.addComment(1L, comment, principal);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedComment, response.getBody());
    }

    @Test
    void testAddCommentBlogNotFound() {
        Comment comment = new Comment();
        when(blogRepo.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<?> response = commentController.addComment(1L, comment, principal);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Failed to add comment"));
    }

    @Test
    void testGetCommentsSuccess() {
        List<Comment> comments = List.of(new Comment(), new Comment());
        when(commentRepo.findByBlogPostId(1L)).thenReturn(comments);
        ResponseEntity<?> response = commentController.getComments(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comments, response.getBody());
    }

    @Test
    void testGetCommentsFailure() {
        when(commentRepo.findByBlogPostId(1L)).thenThrow(new RuntimeException("DB error"));
        ResponseEntity<?> response = commentController.getComments(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Failed to fetch comments"));
    }
}
