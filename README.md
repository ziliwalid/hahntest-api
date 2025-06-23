# Task Manager API

A comprehensive REST API for task management built with Spring Boot 3.2. This application provides user authentication, task CRUD operations, and advanced filtering capabilities with JWT security.

## 🚀 Features

- **User Management**
  - User registration and authentication
  - JWT-based security with access and refresh tokens
  - User profile management

- **Task Management**
  - Create, read, update, and delete tasks
  - Task status management (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)
  - Priority levels (LOW, MEDIUM, HIGH, URGENT)
  - Due date tracking with overdue detection
  - Advanced filtering and search capabilities

- **Security**
  - JWT authentication with Bearer tokens
  - Password encryption using BCrypt
  - User-scoped data access (users can only access their own tasks)

- **Additional Features**
  - Comprehensive API documentation with Swagger
  - Health check endpoints
  - Pagination support
  - Task statistics and analytics
  - Docker containerization
  - PostgreSQL database integration
  - Comprehensive test suite

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.2.1
- **Database**: PostgreSQL 15
- **Security**: Spring Security with JWT
- **Documentation**: Swagger/OpenAPI 3
- **Testing**: JUnit 5, Mockito, AssertJ
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven
- **Java Version**: 17

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose
- PostgreSQL 15 (if running locally without Docker)

## 🚀 Quick Start

### Option 1: Using Docker (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd hahntestback
   ```

2. **Start the database**
   ```bash
   docker-compose up -d postgres
   ```

3. **Build and run the application**
   ```bash
   # Using Maven Wrapper
   ./mvnw clean package -DskipTests
   ./mvnw spring-boot:run
   
   # Or using Docker
   docker build -t task-manager-api .
   docker run -p 8080:8080 task-manager-api
   ```

### Option 2: Local Development

1. **Start PostgreSQL with Docker**
   ```bash
   docker-compose up -d postgres
   ```

2. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The API will be available at `http://localhost:8080`

## 🧪 Testing

The application includes a comprehensive test suite with unit and integration tests.

### Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=TaskServiceTest

# Run tests with coverage
./mvnw test jacoco:report
```

### Test Structure

```
src/test/java/
├── controller/     # Controller unit tests
├── service/        # Service layer tests  
├── entity/         # Entity tests
├── security/       # JWT and security tests
└── integration/    # Integration tests
```

### Test Coverage

- **Unit Tests**: Service layer, controllers, entities
- **Integration Tests**: Full application context
- **Security Tests**: JWT token validation
- **Repository Tests**: Database operations

## 🐳 Docker Configuration

### Database (PostgreSQL)

The application uses PostgreSQL running in Docker:

- **Port**: 5433 (mapped from container port 5432)
- **Database**: taskmanager
- **Username**: taskmanager_user
- **Password**: taskmanager_password

### PgAdmin (Database Management)

Access the database management interface:

- **URL**: http://localhost:5050
- **Email**: admin@taskmanager.com
- **Password**: admin123

### Application Dockerfile

Multi-stage Docker build for optimized production deployment:

```dockerfile
# Build stage with Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
# Runtime stage with JRE only
FROM eclipse-temurin:17-jre-alpine
```

## 📚 API Documentation

### Swagger UI

Access the interactive API documentation at:
- **Development**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

### Health Check

Monitor application health:
- **Health Endpoint**: http://localhost:8080/api/health
- **Actuator Health**: http://localhost:8080/actuator/health

## 🔐 Authentication

The API uses JWT (JSON Web Tokens) for authentication:

### Register a new user
```bash
POST /api/auth/register
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securepassword123",
  "firstName": "John",
  "lastName": "Doe"
}
```

### Login
```bash
POST /api/auth/login
{
  "usernameOrEmail": "john_doe",
  "password": "securepassword123"
}
```

### Using the JWT Token

Include the JWT token in the Authorization header:
```bash
Authorization: Bearer <your-jwt-token>
```

## 📖 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh` - Refresh JWT token
- `POST /api/auth/logout` - Logout (client-side)

### Tasks
- `GET /api/tasks` - Get all user tasks
- `GET /api/tasks/paginated` - Get tasks with pagination
- `GET /api/tasks/{id}` - Get specific task
- `POST /api/tasks` - Create new task
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task

### Task Filtering
- `GET /api/tasks/status/{status}` - Filter by status
- `GET /api/tasks/priority/{priority}` - Filter by priority
- `GET /api/tasks/search?title={title}` - Search by title
- `GET /api/tasks/overdue` - Get overdue tasks
- `GET /api/tasks/statistics` - Get task statistics

### Task Quick Actions
- `PATCH /api/tasks/{id}/complete` - Mark task as completed
- `PATCH /api/tasks/{id}/progress` - Mark task as in progress

### User Profile
- `GET /api/users/profile` - Get current user profile
- `GET /api/users/me` - Get current user info

### Health & Monitoring
- `GET /api/health` - Application health check

## 📊 Database Schema

### Users Table
```sql
- id (BIGINT, Primary Key)
- username (VARCHAR, Unique)
- email (VARCHAR, Unique)
- password (VARCHAR, Encrypted)
- first_name (VARCHAR)
- last_name (VARCHAR)
- is_enabled (BOOLEAN)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

### Tasks Table
```sql
- id (BIGINT, Primary Key)
- title (VARCHAR)
- description (TEXT)
- status (ENUM: PENDING, IN_PROGRESS, COMPLETED, CANCELLED)
- priority (ENUM: LOW, MEDIUM, HIGH, URGENT)
- due_date (TIMESTAMP)
- user_id (BIGINT, Foreign Key)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

## ⚙️ Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5433/taskmanager
spring.datasource.username=taskmanager_user
spring.datasource.password=taskmanager_password

# JWT
jwt.secret=myVerySecretKeyThatShouldBeAtLeast256BitsLongForHS256Algorithm
jwt.expiration=86400000
jwt.refresh-expiration=604800000

# CORS
spring.web.cors.allowed-origins=http://localhost:3000
```

### Environment Variables

For production deployment, override these via environment variables:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`

## 📦 Building for Production

### Maven Build
```bash
./mvnw clean package -DskipTests
```

### Docker Build
```bash
docker build -t task-manager-api .
```

### Multi-architecture Build
```bash
docker buildx build --platform linux/amd64,linux/arm64 -t task-manager-api .
```

## 🚀 Deployment

### Docker Compose (Complete Stack)
```bash
docker-compose up -d
```

This starts:
- PostgreSQL database
- PgAdmin interface
- Application (if configured)

### Production Considerations

1. **Security**
   - Change default JWT secret
   - Use environment variables for sensitive data
   - Enable HTTPS

2. **Database**
   - Use managed PostgreSQL service
   - Configure connection pooling
   - Set up database backups

3. **Monitoring**
   - Configure logging aggregation
   - Set up health check monitoring
   - Monitor JVM metrics

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Issues**
   ```bash
   # Check if PostgreSQL is running
   docker-compose ps
   
   # Check logs
   docker-compose logs postgres
   ```

2. **Port Conflicts**
   - Database uses port 5433 (not 5432)
   - Application uses port 8080
   - PgAdmin uses port 5050

3. **JWT Token Issues**
   - Ensure the JWT secret is at least 256 bits
   - Check token expiration times
   - Verify Authorization header format: `Bearer <token>`

4. **Build Issues**
   ```bash
   # Clean and rebuild
   ./mvnw clean compile
   
   # Skip tests if needed
   ./mvnw clean package -DskipTests
   ```

5. **Test Failures**
   ```bash
   # Run specific test
   ./mvnw test -Dtest=TaskServiceTest
   
   # Run tests with debug info
   ./mvnw test -X
   ```

## 📋 Reference Documentation

For further reference, please consider the following sections:

### Spring Boot Documentation
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/html/#build-image)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools)
* [Validation](https://docs.spring.io/spring-boot/docs/current/reference/html/io.html#io.validation)
* [Spring Web](https://docs.spring.io/spring-boot/docs/current/reference/html/web.html#web)
