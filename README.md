Translation Management System (TMS)

A simple API-driven translation management service built with Spring Boot and H2. It supports multi-language content with context tags and a JSON export endpoint for frontend use.

---

## 🚀 Getting Started

You can run the application in two ways:

### 🔧 Option 1: Using Docker

1. Build the project JAR:

   ```bash
   ./mvnw clean package -DskipTests
2 - Build and run the Docker container:
    docker build -t translation-service .
    docker run -p 8080:8080 translation-service


### 🔧 Option 2: Run With Java

java -jar target/translation-management-system-0.0.1-SNAPSHOT.jar

Note : This API is secured using JWT Authentication.
  To access any protected endpoints, follow these steps:

1 - Login with valid credentials using this endpoint:
  POST /api/auth/login

  {
    "username": "user",
    "password": "password"
  }

2 - Copy the token from the response:

  {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzUyODQ4NDg2LCJleHAiOjE3NTI5MzQ4ODZ9._0hgi8_Xpj3turtf6kLisS4_888zTD5bxajYN6N6gqY",
      "username": "user"
  }

3 - Use the token in all subsequent requests in the header:

  Authorization: Bearer <your_token_here>


IN order to fully utilize the API Service you must follow the order of execution
   1 -  First: Create languages i-e  
        POST /api/languages 
        {
            "code": "en",
            "name": "English"
        }
   2 - Second: Create tags
       POST /api/tags
       {
            "name": "web",
            "description": "Web application specific translations"
      }

  3 - Create translation keys
       POST /api/translation-keys
       {
          "keyName": "loading_message",
          "description": "Loading message displayed during operations",
          "tagNames": ["web", "mobile", "common"]
      }
  4 - Create all translations
      POST /api/translations
      {
          "keyName": "loading_message",
          "languageCode": "es",
          "content": "Cargando..."
      }
      
Finally: Test export and search endpoints

  GET  api/export/translations


✅ Features
Multi-language translation management

Context tagging (e.g., mobile, web)

Search and filter by key, tag, content, or locale

Live JSON export for frontend apps (Vue.js, React, Angular etc.)

