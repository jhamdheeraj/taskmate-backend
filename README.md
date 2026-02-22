# Taskmate Backend

A comprehensive Spring Boot backend service for Taskmate application - a modern task management system with soft delete functionality and advanced filtering capabilities.

## Overview

Taskmate is a robust task management application that allows users to create, organize, and track their tasks with advanced features including soft delete, pagination, and dynamic filtering. This backend provides a RESTful API with comprehensive validation and error handling.

## Features

### Core Functionality
- **Task Management**: Create, read, update, and soft delete tasks
- **Soft Delete**: Tasks are marked as deleted instead of permanent removal
- **Task Completion**: Boolean flag for completed/incomplete status
- **Due Date Tracking**: Set and monitor task deadlines with overdue detection
- **Automatic Timestamps**: Created and updated timestamps managed automatically

### Advanced Features
- **Dynamic Filtering**: Filter tasks by completion status, due dates, and deletion status
- **Pagination**: Efficient pagination with customizable page size and sorting
- **Flexible Sorting**: Sort by any field with ascending/descending order
- **CORS Support**: Cross-origin requests enabled for frontend integration
- **Input Validation**: Comprehensive validation with meaningful error messages

### Data Management
- **Database Indexes**: Optimized queries with proper indexing
- **UUID Primary Keys**: Secure, non-sequential task identifiers
- **Data Integrity**: Soft delete preserves data for audit trails
- **H2 Support**: In-memory database for testing and development

## Tech Stack

- **Java 17** - Modern Java with latest language features
- **Spring Boot 4.0.3** - Application framework with auto-configuration
- **Spring Data JPA** - Database ORM with repository pattern
- **Spring Boot Validation** - Bean validation for API requests
- **PostgreSQL** - Primary production database
- **H2 Database** - In-memory database for testing
- **Lombok** - Boilerplate code reduction
- **Jackson JSR310** - Java 8 time serialization support
- **Gradle** - Build tool with dependency management

## Project Structure

```
src/main/java/com/taskmate/
├── TaskmateApplication.java          # Main application class
├── controller/
│   └── TaskController.java           # REST API endpoints
├── entity/
│   └── Task.java                    # Task entity with soft delete
├── repository/
│   └── TaskRepository.java          # Data access layer with custom queries
├── service/
│   ├── TaskService.java             # Service interface
│   └── impl/
│       └── TaskServiceImpl.java     # Service implementation
└── pojos/
    ├── TaskResponse.java            # API response wrapper
    └── TaskSpecification.java      # Dynamic filtering specifications

src/test/java/com/taskmate/
├── TaskmateApplicationTests.java    # Application context test
├── entity/
│   └── TaskTest.java              # Entity unit tests
├── service/
│   └── impl/
│       └── TaskServiceImplTest.java # Service unit tests
├── controller/
│   └── TaskControllerTest.java    # Controller unit tests
├── repository/
│   └── TaskRepositoryTest.java     # Repository integration tests
└── integration/
    └── TaskIntegrationTest.java      # Full integration tests
```

## Database Schema

### Tasks Table
- `id` (UUID) - Primary key with auto-generation
- `title` (VARCHAR 255) - Task title (required, validated)
- `description` (VARCHAR 2000) - Task description (optional)
- `completed` (BOOLEAN) - Task completion status (default: false)
- `deleted` (BOOLEAN) - Soft delete flag (default: false)
- `due_date` (TIMESTAMP) - Task deadline (optional)
- `created_at` (TIMESTAMP) - Creation timestamp (auto-managed)
- `updated_at` (TIMESTAMP) - Last update timestamp (auto-managed)

### Indexes
- `idx_tasks_completed` - Optimizes queries by completion status
- `idx_tasks_deleted` - Optimizes soft delete filtering
- `idx_tasks_due_date` - Optimizes queries by due date

## API Endpoints

### Task Management
- **POST** `/api/v1/tasks` - Create a new task
  - Request Body: Task object
  - Response: TaskResponse with created task
  - Validation: Title required, max 255 chars; Description max 2000 chars

- **GET** `/api/v1/tasks/{id}` - Get task by ID
  - Path Variable: Task UUID
  - Response: Task object (null if not found or deleted)
  - Behavior: Returns null for deleted tasks

- **DELETE** `/api/v1/tasks/{id}` - Soft delete task
  - Path Variable: Task UUID
  - Response: JSON with success status and message
  - Behavior: Marks task as deleted, doesn't remove from database

- **GET** `/api/v1/tasks` - Get paginated tasks with filtering
  - Query Parameters:
    - `completed` (Boolean, optional) - Filter by completion status
    - `overdue` (Boolean, optional) - Filter overdue tasks
    - `dueFrom` (DateTime, optional) - Filter tasks due after this date
    - `dueTo` (DateTime, optional) - Filter tasks due before this date
    - `deleted` (Boolean, optional, default: false) - Include deleted tasks
    - `page` (int, default: 0) - Page number
    - `size` (int, default: 10) - Page size
    - `sortBy` (String, default: "createdAt") - Sort field
    - `direction` (String, default: "desc") - Sort direction (asc/desc)
  - Response: Page<Task> with pagination metadata

## Response Formats

### Task Creation Response
```json
{
  "result": {
    "id": "uuid",
    "title": "Task Title",
    "description": "Task Description",
    "completed": false,
    "deleted": false,
    "dueDate": "2024-01-01T10:00:00",
    "createdAt": "2024-01-01T09:00:00",
    "updatedAt": "2024-01-01T09:00:00"
  },
  "message": "Task create Successfully!",
  "success": true
}
```

### Soft Delete Response
```json
{
  "success": true,
  "message": "Task deleted successfully"
}
```

### Error Response (Validation)
```json
{
  "success": false,
  "message": "Task not found or invalid ID"
}
```

## Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher (for production)
- Gradle 7.0 or higher

### Database Setup

1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE taskmate;
   ```

2. Update database credentials in `src/main/resources/application.yaml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/taskmate
       username: your_username
       password: your_password
   ```

### Running the Application

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd taskmate-backend
   ```

2. Build the application:
   ```bash
   ./gradlew build
   ```

3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080`

### Running Tests

1. Run all tests:
   ```bash
   ./gradlew test
   ```

2. Run specific test class:
   ```bash
   ./gradlew test --tests TaskServiceImplTest
   ```

3. Run with coverage:
   ```bash
   ./gradlew test jacocoTestReport
   ```

## Configuration

### Application Configuration

The main configuration is in `src/main/resources/application.yaml`:

- **Database Settings**: PostgreSQL connection with H2 fallback for testing
- **JPA Configuration**: Hibernate dialect and DDL settings
- **SQL Initialization**: Schema creation on startup
- **CORS Settings**: Frontend integration support

### Environment Profiles

- **Default**: PostgreSQL configuration for production
- **Test**: H2 in-memory database for automated testing

### Database Schema

The application uses `schema.sql` for database schema creation:
- Automatic table creation with proper constraints
- Optimized indexes for performance
- UUID generation for primary keys

## Development Guidelines

### Code Standards
- Follow Spring Boot conventions
- Use Lombok annotations to reduce boilerplate
- Implement comprehensive validation
- Write unit and integration tests for all features

### Testing Strategy
- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test API endpoints with real database
- **Repository Tests**: Test data access layer
- **Service Tests**: Test business logic with mocks
- **Controller Tests**: Test REST endpoints with MockMvc

### API Design Principles
- RESTful design with proper HTTP methods
- Consistent response formats
- Comprehensive error handling
- Input validation with meaningful messages
- Soft delete for data preservation

## Advanced Features

### Soft Delete Implementation
- Tasks are marked as deleted instead of permanent removal
- Deleted tasks are excluded from default queries
- Can be explicitly requested with `deleted=true` parameter
- Preserves data integrity and audit trails

### Dynamic Filtering
- Specification-based filtering for complex queries
- Combines multiple filter conditions
- Efficient database queries with proper indexing
- Pagination support for large datasets

### Validation Framework
- Bean validation annotations on entities
- Custom error messages for better UX
- Automatic validation in REST endpoints
- Comprehensive test coverage for validation scenarios

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Workflow
- Write tests before implementing features
- Ensure all tests pass before submitting PR
- Follow existing code style and patterns
- Update documentation for new features

## Support

For support and questions:
- Create an issue in the repository
- Check existing documentation
- Review test cases for usage examples