package com.blog.management.banking.serviceTest;

import com.blog.management.banking.entity.User;
import com.blog.management.banking.repository.UserRepository;
import com.blog.management.banking.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepo;

    @Test
    public void testUserRegistration() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpass");
        when(userRepo.existsByUsername("testuser")).thenReturn(false);
        when(userRepo.save(any(User.class))).thenReturn(user);
        User registeredUser = authService.register("testuser", "testpass");
        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
    }

    @Test
    public void testUserLoginSuccess() {
        User user = new User();
        user.setUsername("loginuser");
        user.setPassword("loginpass");
        when(userRepo.findByUsername("loginuser")).thenReturn(Optional.of(user));
        boolean result = authService.login("loginuser", "loginpass");
        assertTrue(result);
    }

    @Test
    public void testUserLoginFailure() {
        when(userRepo.findByUsername("nonexistent")).thenReturn(Optional.empty());
        boolean result = authService.login("nonexistent", "wrongpass");
        assertFalse(result);
    }

    @Test
    public void testDuplicateUserRegistrationThrowsException() {
        when(userRepo.existsByUsername("duplicateUser")).thenReturn(true);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.register("duplicateUser", "pass123");
        });
        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    public void testLoginWithNullUsernameThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(null, "somepass");
        });
        assertEquals("Username and password must not be null", exception.getMessage());
    }
}
