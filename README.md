
# Blog Post REST API

This is a Spring Boot REST API for managing blog posts. It allows users to create blog posts, retrieve all blog posts, and retrieve their own blog posts.

## Features
- Create a blog post
- Retrieve all blog posts
- Retrieve blog posts by the authenticated user

## Technologies Used
- Java
- Spring Boot
- Spring Data JPA
- H2/MySQL Database

3. Build and run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints
### 2. `POST /auth/login`
Authenticates a user.

#### 🔸 Request Body
```json
{
  "username": "exampleUser",
  "password": "examplePassword"
}
```

#### 🔸 Response
- **200 OK**: `{ "message": "Login successful" }`
- **401 Unauthorized**: `{ "error": "Invalid credentials" }`
- **500 Internal Server Error**: `{ "error": "Login failed" }`

### Create Blog Post
- **URL:** `/`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
    "title": "My First Blog",
    "content": "This is the content of the blog post."
  }
  ```
- **Response:** `201 Created`

### Get All Blog Posts
- **URL:** `/`
- **Method:** `GET`
- **Response:** `200 OK`
  ```json
  [
    {
      "id": 1,
      "title": "My First Blog",
      "content": "This is the content of the blog post.",
      "author": {
        "id": 1,
        "username": "johndoe"
      }
    }
  ]
  ```

### Get My Blog Posts
- **URL:** `/my`
- **Method:** `GET`
- **Response:** `200 OK`
  ```json
  [
    {
      "id": 1,
      "title": "My First Blog",
      "content": "This is the content of the blog post."
    }
  ]
  ```
## Error Handling
- `500 Internal Server Error` for unexpected issues
- `404 Not Found` if the user is not found


