# Bookshelf — Spring Boot REST API

Bookshelf — это веб-приложение для управления личной библиотекой. Пользователи могут регистрироваться с подтверждением email, загружать книги, читать их с сохранением прогресса, создавать полки и распределять книги по ним.

---

## Технологии

- Java 23
- Spring Boot 3.5.3
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Spring Mail (Mailtrap)
- Google reCAPTCHA v2
- Swagger / OpenAPI (springdoc)
- Maven

---

## Требования

Перед запуском убедитесь, что установлены:

- JDK 23
- Apache Maven 3.9+
- PostgreSQL 14+ (создана база `bookshelf`)
- Аккаунт на [Mailtrap.io](https://mailtrap.io) (для тестовой отправки email)
- Ключи Google reCAPTCHA v2 (для регистрации)

---

## Инструкция по запуску

### 1. Клонирование и сборка

```bash
git clone <url-репозитория>
cd bookshelf
mvn clean install
```

### 2. Настройка базы данных

Создайте базу данных в PostgreSQL:

```sql
CREATE DATABASE bookshelf;
```

Проверьте параметры подключения в `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bookshelf
    username: postgres
    password: ваш_пароль
```

### 3. Настройка Mailtrap (тестовая почта)

1. Зарегистрируйтесь на [mailtrap.io](https://mailtrap.io)
2. Создайте Sandbox Inbox
3. Скопируйте SMTP credentials (Username / Password)
4. Вставьте их в `application.yml`:

```yaml
spring:
  mail:
    host: sandbox.smtp.mailtrap.io
    port: 2525
    username: ваш_username
    password: ваш_password
```

### 4. Настройка Google reCAPTCHA

1. Получите ключи на [Google reCAPTCHA Admin Console](https://www.google.com/recaptcha/admin)
2. Выберите reCAPTCHA v2 → "I'm not a robot" Checkbox
3. Добавьте домен `localhost`
4. Скопируйте Secret Key в `application.yml`:

```yaml
recaptcha:
  secret: ваш_secret_key
  verify-url: https://www.google.com/recaptcha/api/siteverify
```

> **Для тестов через Postman:** реальный токен reCAPTCHA генерируется только в браузере. Для тестирования API без фронтенда временно отключите проверку капчи в `AuthService` (закомментируйте блок `if (!recaptchaService.verifyCaptcha(...))`).

### 5. Запуск приложения

```bash
mvn spring-boot:run
```

Или через IDE — запустите `BookshelfApplication.java`.

Приложение поднимется на:  
**http://localhost:8080**

### 6. Документация API (Swagger)

После запуска откройте в браузере:  
**http://localhost:8080/swagger-ui.html**

Там доступны все endpoint'ы с возможностью тестирования прямо из интерфейса.
