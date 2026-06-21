# Cat Shelter
Full-stack Cat Shelter app built with Spring Boot and React.

## Features
- Create cats
- Feed, pet and play interactions
- Cooldown handling
- Friendship-based adoption
- Honor wall for adopted cats
- Filtering by adoption status
- Validation and custom error responses
- OpenAPI/Swagger documentation
- Unit tests for service logic

## Tech stack
Backend:
- Java 21
- Spring Boot
- Spring Data JPA
- H2
- Gradle
- OpenAPI/Swagger

Frontend:
- React
- TypeScript
- Vite

## Run backend
```bash
./gradlew bootRun
```
Backend runs on:
http://localhost:8080

Swagger UI:
http://localhost:8080/swagger-ui/index.html

## Run frontend
```bash
cd frontend
npm install
npm run dev
```

Frontend runs on:
http://localhost:5173

## API examples
```http
GET /api/cats
GET /api/cats?status=IN_SHELTER
GET /api/cats?status=ADOPTED
POST /api/cats
POST /api/cats/{id}/interactions
POST /api/cats/{id}/adopt
```
