# Biblioteca Saraiva API

REST API developed with Java and Spring Boot for managing a personal book collection.

The project was built as part of a backend learning path, evolving from basic HTTP and SQL concepts to a layered REST API with PostgreSQL, JPA/Hibernate, dynamic filtering, automated tests and database migrations.

## Features

- Book CRUD operations
- Author CRUD operations
- Book and author relationship management
- Dynamic book search with combinable filters
- Pagination and sorting
- Book filtering by title, author, genre, reading status and publication year
- Book statistics grouped by genre and author
- Input validation
- Global exception handling
- Database migrations with Flyway
- Unit tests for service and controller layers
- JPA repository tests using an isolated PostgreSQL test database
- Interactive API documentation with OpenAPI / Swagger

## Technologies

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- PostgreSQL
- Flyway
- Maven
- Bean Validation
- JUnit
- Mockito
- Spring MockMvc
- OpenAPI / Swagger
- Postman

## Architecture

The application follows a layered architecture:

```text
HTTP Request
    ↓
Controller
    ↓
Service
    ↓
Repository
    ↓
JPA / Hibernate
    ↓
PostgreSQL
```
- **Controller:** handles HTTP requests and responses.
- **Service:** contains business rules and coordinates application operations.
- **Repository:** provides persistence operations through Spring Data JPA.
- **DTOs:** define request and response contracts without exposing entities directly.
- **Mappers:** convert entities into response DTOs.
- **Specifications:** build dynamic and composable database filters.

## Database and Migrations

The application uses PostgreSQL as its relational database.

Database schema changes are versioned with Flyway migrations located in:

```text
src/main/resources/db/migration
```
Current migrations:

- `V1__initial_schema.sql` — creates the initial `authors` and `books` tables.
- `V2__add_nationality_to_authors.sql` — adds the optional `nationality` field to authors.

When the application starts, Flyway automatically applies pending migrations.

## Configuration

The application uses the following default PostgreSQL configuration:

```text
Host: localhost
Port: 5432
Database: biblioteca-saraiva
Username: postgres
```

The database password is not stored in the repository.

The application expects the following environment variable:

```text
DB_PASSWORD
```
The database connection is configured in `application.properties`.

## How to Run
### Prerequisites

- Java 21
- PostgreSQL

The project includes the Maven Wrapper, so a local Maven installation is not required.

### 1. Clone the repository

```bash
git clone <https://github.com/Iasmim-Saraiva/biblioteca-api.git>
cd biblioteca-api
```

### 2. Create the database

Create an empty PostgreSQL database named:

```text
biblioteca-saraiva
```

Flyway will automatically create and update the database schema when the application starts.

### 3. Configure the database password

Set the `DB_PASSWORD` environment variable with the password of your PostgreSQL user.

### 4. Run the application

On Windows:

```bash
.\mvnw.cmd spring-boot:run
```

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

## Tests
The project contains automated tests for different layers of the application.

### Service Tests
Service tests use JUnit and Mockito to test business logic with mocked repositories.

### Controller Tests
Controller tests use MockMvc with mocked services to test the HTTP layer, validation, status codes and JSON responses.

### Repository Tests
Repository tests use `@DataJpaTest` with an isolated PostgreSQL test database to execute real JPA/Hibernate queries.

Repository tests currently cover:

- JPQL aggregation with `GROUP BY`
- Dynamic filtering with combined `Specification` objects
- Verification that repository tests use the isolated test database

Test data is rolled back after each repository test.

### Running Tests
Repository tests use a separate PostgreSQL database named:

```text
biblioteca-saraiva-test
```

Create this database before running the complete test suite. The test database uses the same `DB_PASSWORD` environment variable as the main application.

On Windows:

```bash
.\mvnw.cmd test
```

On Linux/macOS:

```bash
./mvnw test
```

## API Endpoints

### Books

| Method | Endpoint | Description |
|---|---|---|
| GET | `/books` | Lists books with optional filters, pagination and sorting |
| GET | `/books/{id}` | Returns a book by ID |
| POST | `/books` | Creates a new book |
| PUT | `/books/{id}` | Updates an existing book |
| PATCH | `/books/{id}/read` | Updates only the reading status of a book |
| DELETE | `/books/{id}` | Deletes a book |
| GET | `/books/stats/count` | Returns the total number of books |
| GET | `/books/stats/genres` | Returns the number of books grouped by genre |
| GET | `/books/stats/authors` | Returns the number of books grouped by author |

#### `GET /books`

Lists books using optional filters. Filters can be combined.

##### Query parameters

| Parameter | Type | Required | Description |
|---|---|---|---|
| `title` | String | No | Filters books by title |
| `author` | String | No | Filters books by author name |
| `read` | Boolean | No | Filters books by reading status |
| `genre` | String | No | Filters books by genre |
| `minYear` | Integer | No | Minimum publication year |
| `maxYear` | Integer | No | Maximum publication year |
| `page` | Integer | No | Page number, starting at `0` |
| `size` | Integer | No | Number of elements per page |
| `sort` | String | No | Sorting field and direction, e.g. `title,asc` |

Example:

```http
GET /books?genre=Fantasia&read=true&page=0&size=5&sort=title,asc
```
#### `POST /books`

Creates a new book.

##### Request body

| Field | Type | Required |
|---|---|---|
| `title` | String | Yes |
| `publicationYear` | Integer | Yes |
| `read` | Boolean | Yes |
| `authorId` | Integer | Yes |
| `genre` | String | Yes |

Example:

```json
{
  "title": "Frankenstein",
  "publicationYear": 1818,
  "read": true,
  "authorId": 1,
  "genre": "Terror"
}
```
Sucess response:`201 Created`.

#### `PUT /books/{id}`

Updates an existing book.

Uses the same request body as `POST /books`.

Success response: `200 OK`.

Returns `404 Not Found` if the book does not exist.

#### `PATCH /books/{id}/read`

Updates only the reading status of a book.

##### Request body

```json
{
  "read": true
}
```
Success response: ```200 OK```

### Statistics
#### `GET /books/stats/count`
Response:
```json
  12
```

#### `GET /books/stats/genres`
Response:
```json
[
  {
    "genre": "Fantasia",
    "count": 5
  },
  {
    "genre": "Terror",
    "count": 3
  }
]
```
#### `GET /books/stats/authors`
```json
[
  {
    "authorName": "Machado de Assis",
    "count": 4
  },
  {
    "authorName": "Mary Shelley",
    "count": 1
  }
]
```

### Authors

| Method | Endpoint | Description |
|---|---|---|
| GET | `/authors` | Lists all authors |
| GET | `/authors/{id}` | Returns an author by ID |
| POST | `/authors` | Creates a new author |
| PUT | `/authors/{id}` | Updates an existing author |
| DELETE | `/authors/{id}` | Deletes an author |
| GET | `/authors/{id}/books` | Lists all books associated with an author |

#### `POST /authors`

Creates a new author.

##### Request body

| Field | Type | Required |
|---|---|---|
| `name` | String | Yes |
| `nationality` | String | No |

Example:

```json
{
  "name": "Mary Shelley",
  "nationality": "British"
}
```
Success response: `201 Created`.

#### `DELETE /authors/{id}`

Deletes an author.

Success response: `204 No Content`.

An author that still has associated books cannot be deleted and returns `409 Conflict`.

Returns `404 Not Found` if the author does not exist.

## Error Responses

Errors follow this structure:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Book not found"
}
```

### `400 Bad Request`

Returned when request validation fails or when invalid parameters are provided.

Example:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Title is required"
}
```

### `404 Not Found`

Returned when a requested resource does not exist.

Example:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Book not found"
}
```

### `409 Conflict`

Returned when an operation conflicts with a business rule, such as trying to delete an author that still has associated books.

Example:

```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Author cannot be deleted because it has associated books"
}
```

## Postman Collection
A Postman collection containing the main API requests is available in the [`postman`](postman/) directory.

The collection uses the `baseUrl` variable, configured by default as:
```text
http://localhost:8080
```

## OpenAPI / Swagger
Interactive API documentation is generated with Springdoc OpenAPI.

With the application running, Swagger UI is available at:
```text
http://localhost:8080/swagger-ui.html
```

The generated OpenAPI specification is available at:
```text
http://localhost:8080/v3/api-docs
```

## Future Improvements
- Replace the boolean reading status with an enum such as `WANT_TO_READ`, `READING`, `READ` and `ABANDONED`
- Build a frontend interface for the library
- Improve validation error responses to return multiple field errors
- Add containerized development and testing infrastructure

