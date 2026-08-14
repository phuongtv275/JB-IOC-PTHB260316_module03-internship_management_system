# Internship Management System

Backend REST API quản lý quy trình thực tập: tài khoản và phân quyền, hồ sơ sinh viên/mentor, giai đoạn thực tập, tiêu chí/đợt đánh giá, phân công và chấm điểm.

## Công nghệ

- Java 17, Spring Boot 4.1, Maven
- Spring Data JPA, Spring Security, JWT
- PostgreSQL 16+; H2 cho test
- Lombok, Bean Validation

## Chức năng

- Xác thực JWT và RBAC cho `ADMIN`, `MENTOR`, `STUDENT`.
- Quản lý người dùng, trạng thái kích hoạt và role.
- Quản lý hồ sơ Mentor và Student.
- Quản lý giai đoạn thực tập, tiêu chí, đợt đánh giá và trọng số tiêu chí.
- Phân công Student cho Mentor theo giai đoạn.
- Mentor chấm điểm Student được phân công; Student chỉ xem dữ liệu của mình.

## Yêu cầu

- JDK 17+
- PostgreSQL 16+
- Maven Wrapper đi kèm repository

## Khởi tạo database

Tạo database và user PostgreSQL phù hợp với cấu hình. Sau đó chạy schema, rồi indexes:

```bash
createdb -U rikkei internship-management-system
psql -U rikkei -d internship-management-system -f src/main/resources/static/sql/schema.sql
psql -U rikkei -d internship-management-system -f src/main/resources/static/sql/indexes.sql
```

Ứng dụng dùng `spring.jpa.hibernate.ddl-auto=validate`, vì vậy schema phải tồn tại trước khi chạy.

## Cấu hình và chạy

Thiết lập biến môi trường:

```bash
export DB_PASSWORD='your-postgresql-password'
export JWT_SECRET="$(openssl rand -base64 32)"
```

Khởi động:

```bash
./mvnw spring-boot:run
```

Mặc định API chạy tại `http://localhost:8080`.

## Tài khoản development mặc định

Mỗi lần khởi động, hệ thống tạo tài khoản nếu username chưa tồn tại.

| Role | Username | Password |
| --- | --- | --- |
| ADMIN | `ADMIN` | `admin` |
| MENTOR | `MENTOR` | `mentor` |
| STUDENT | `STUDENT` | `student` |

Chỉ dùng các tài khoản này ở môi trường development/demo. Tài khoản bị vô hiệu hóa (`isActive=false`) không thể đăng nhập.

## API và kiểm thử

- API contract: [docs/openapi.yaml](docs/openapi.yaml)
- Kịch bản kiểm thử: [docs/testing_scenario.md](docs/testing_scenario.md)
- Postman collection: [docs/internship-management-system.postman_collection.json](docs/internship-management-system.postman_collection.json)

Import collection vào Postman, đặt `baseUrl` nếu cần, rồi chạy các folder theo thứ tự. Collection tự lưu access token và ID tạo ra vào collection variables.

## Kiểm tra chất lượng

```bash
./mvnw spotless:apply checkstyle:check test
```

## Logging

Log ứng dụng có prefix `IMS_` và `traceId` để liên kết các log của cùng request. Access log không ghi token, query string, mật khẩu hay dữ liệu cá nhân.

## Tài liệu bổ sung

- [SRS](docs/srs.md)
- [Báo cáo thay đổi](docs/implementation-report.md)
