# Payment Router

### Stack

- Java
- Spring Boot
- PostgreSQL
- Docker

### Создание .env файла
Необходимо добавить файл .env и прописать в нем
системные переменные, например как в файле `.env.example`  

### Запуск PostgreSQL
`docker compose up -d`

### Запуск приложения
`./mvnw spring-boot:run`

### Проверка
`GET http://localhost:8080/actuator/health`