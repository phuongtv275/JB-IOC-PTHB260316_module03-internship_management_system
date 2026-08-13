# Testing Scenario

## 1. Khởi tạo môi trường

1. Tạo database PostgreSQL `internship-management-system`.
2. Chạy `docs/schema.sql`, sau đó chạy `docs/indexes.sql` trên database đó.
3. Thiết lập biến môi trường:

   ```bash
   export DB_PASSWORD='password-for-rikkei'
   export JWT_SECRET="$(openssl rand -base64 32)"
   ```

4. Khởi động ứng dụng:

   ```bash
   ./mvnw spring-boot:run
   ```

Khi ứng dụng khởi động, các tài khoản sau được tạo nếu username tương ứng chưa tồn tại. Tài khoản đã tồn tại sẽ không bị thay đổi mật khẩu hoặc role.

| Role | Username | Password |
| --- | --- | --- |
| ADMIN | `ADMIN` | `admin` |
| MENTOR | `MENTOR` | `mentor` |
| STUDENT | `STUDENT` | `student` |

Các mật khẩu này chỉ dành cho môi trường phát triển/demo. Đổi hoặc vô hiệu hóa chúng trước khi triển khai production.

## 2. Chạy Postman collection

1. Import [internship-management-system.postman_collection.json](./internship-management-system.postman_collection.json) vào Postman.
2. Đặt collection variable `baseUrl` nếu ứng dụng không chạy ở `http://localhost:8080`.
3. Chạy các request theo thứ tự trong folder **Happy path**.

Collection tự lưu access token và các ID tạo ra từ response vào collection variables. Scenario giả định database mới hoặc chưa có profile/giai đoạn/tiêu chí/phân công demo tương ứng. Nếu chạy lại trên database đã có dữ liệu, hãy xóa dữ liệu demo hoặc dùng request tương ứng để truy vấn/cập nhật thay vì tạo lại.

## 3. Kết quả mong đợi

1. Admin đăng nhập và lấy được ID của Mentor/Student mặc định.
2. Admin tạo profile cho Mentor/Student, giai đoạn thực tập, tiêu chí, đợt đánh giá và phân công.
3. Mentor đăng nhập, tạo kết quả đánh giá cho sinh viên được phân công.
4. Student đăng nhập và đọc được kết quả của chính mình.
