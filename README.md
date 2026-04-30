# 🔗 URL Shortener

## 💭 Overview

A simple URL shortener currently at its local MVP stage. It allows users to submit a long URL, receive a shortened version, view saved links, and manage them through a small web interface and backend API.

## 🗼 Status

This project is currently a work in progress.
It is functional as a local MVP, but it is not yet production-ready.
Security, scalability, persistence, and deployment concerns are still being improved.

## 🌟 Features

* Shorten long URLs
* View stored URLs
* View URL click count
* Delete saved URLs
* User authentication with OAuth2 (Google)

---

## 📝 TODO

* ~~Add user authentication and ownership of links~~
* Add persistent database storage
* Improve short code generation algorithm
* Improve input validation and sanitization
* Improve UI/UX

## 🔮 Future Features

* Prohibit shortening suspicious and insecure links
* Rate limiting and abuse protection
* Add transition page to notify users of URL redirect for user safety
* Custom short aliases
* QR code generation for shortened links
* Click analytics and usage tracking
* Expiration dates for short URLs
* Deployment to a cloud platform

---

## 📚 Tech Stack

### Backend

* Java
* Spring Boot
* Maven

### Frontend

* HTML
* CSS
* JavaScript

### Other Tools

* Git
* GitHub
* Postman
* IntelliJ IDEA

## Project Structure

```text
url-shortener/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md
```

---

## 🛜 API Endpoints

### Create a Short URL

**POST** `/api/urls`

Request body:

```json
{
  "longUrl": "https://example.com/some/very/long/link"
}
```

Example response:

```json
{
  "id": 1,
  "longUrl": "https://example.com/some/very/long/link",
  "shortCode": "abc123",
  "shortUrl": "http://localhost:8080/abc123"
}
```

### Get All URLs

**GET** `/api/urls`

Example response:

```json
[
  {
    "id": 1,
    "longUrl": "https://example.com",
    "shortCode": "abc123",
    "shortUrl": "http://localhost:8080/abc123"
  }
]
```

### Delete a URL by ID

**DELETE** `/api/urls/{id}`

Success response:

```json
{
  "message": "Deleted successfully"
}
```

Error response:

```json
{
  "error": "id not found"
}
```

### Redirect Using Short Code

**GET** `/{shortCode}`

Example:

```text
GET /abc123
```

This redirects the user to the original long URL.

---

## Running the Project Locally

### Prerequisites

* Java installed
* Maven installed, or use the Maven wrapper included in the project
* IntelliJ IDEA or another Java IDE

### Steps

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Run the Spring Boot application.
4. Open the frontend in your browser if it is served separately, or access the app through the configured local host.
