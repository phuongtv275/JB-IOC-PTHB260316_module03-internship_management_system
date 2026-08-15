# Kịch bản kiểm thử API đầy đủ

Tài liệu này kiểm thử 44 endpoint theo đúng API đang triển khai. Mỗi test case có ID để có thể ghi nhận kết quả `PASS`/`FAIL` trong Postman, Excel hoặc hệ thống quản lý test. Không chạy các ca xoá dữ liệu trước khi hoàn thành những ca phụ thuộc vào dữ liệu đó.

## 1. Chuẩn bị

1. Tạo database PostgreSQL `internship-management-system`, chạy lần lượt `../src/main/resources/static/sql/schema.sql` và `../src/main/resources/static/sql/indexes.sql`.
2. Thiết lập biến môi trường rồi khởi động ứng dụng:

   ```bash
   export DB_PASSWORD='password-for-rikkei'
   export JWT_SECRET="$(openssl rand -base64 32)"
   ./mvnw spring-boot:run
   ```

3. Các tài khoản khởi tạo tự động là `ADMIN`/`admin`, `MENTOR`/`mentor`, `STUDENT`/`student`. Chúng chỉ dùng ở môi trường dev/demo.
4. Đặt `baseUrl=http://localhost:8080`. Mọi API trừ `POST /api/auth/login` cần header `Authorization: Bearer <token>`.

### Quy ước kiểm tra response

- Thành công: body có `status`, `message`, `data`; mã HTTP đúng với bảng test case.
- Lỗi: body có `status`, `errorCode`, `message`, `timestamp`, `errors`.
- Không gửi/lưu token, mật khẩu hoặc dữ liệu cá nhân vào báo cáo kiểm thử hay log.
- Với endpoint yêu cầu xác thực, chạy thêm các ca chung sau, trừ khi một ca riêng đã bao phủ:

| ID | Điều kiện | Kỳ vọng |
| --- | --- | --- |
| SEC-01 | Không có `Authorization` | `401`, `BAD_CREDENTIALS` |
| SEC-02 | `Bearer invalid-token` | `401`, `INVALID_JWT_TOKEN` |
| SEC-03 | Token hợp lệ nhưng role không được phép | `403`, `ACCESS_DENIED` |
| VAL-01 | Body thiếu trường bắt buộc, sai enum/date/number hoặc vượt giới hạn `@Size` | `400`, `INVALID_INPUT_DATA` |
| NF-01 | ID path hoặc ID tham chiếu không tồn tại, ví dụ `2147483647` | `404`, `RESOURCE_NOT_FOUND` |

> `SEC-01` và `SEC-02` chỉ cần chạy đại diện một lần cho mỗi nhóm endpoint có cùng cấu hình bảo mật. `SEC-03` phải chạy cho từng loại quyền khác nhau.

## 2. Dữ liệu test cô lập và thứ tự chạy

Đăng nhập ADMIN, lấy ID ba tài khoản mặc định. Sau đó tạo thêm ba user bằng hậu tố theo lần chạy, ví dụ `QA_<YYYYMMDDHHMM>_MENTOR_2`, `QA_<...>_STUDENT_2`, `QA_<...>_ADMIN_2`; mật khẩu tối thiểu 8 ký tự. Tạo profile cho hai Mentor và hai Student.

| Biến | Dữ liệu cần có | Mục đích |
| --- | --- | --- |
| `mentor1Id` | Profile của `MENTOR` | Mentor sở hữu phân công |
| `mentor2Id` | Profile của user MENTOR thứ hai | Kiểm tra không được đọc/chấm phân công khác |
| `student1Id` | Profile của `STUDENT` | Student sở hữu phân công |
| `student2Id` | Profile của user STUDENT thứ hai | Kiểm tra không được đọc/sửa profile hay phân công khác |
| `phase1Id` | `QA Phase <runId>`, 2026-09-01 đến 2026-12-31 | Phase hợp lệ |
| `phase2Id` | `QA Phase 2 <runId>`, 2027-01-01 đến 2027-03-31 | Kiểm tra sai quan hệ phase |
| `criterion1Id` | `QA Technical <runId>`, `maxScore=10` | Tiêu chí trong round |
| `criterion2Id` | `QA Communication <runId>`, `maxScore=5` | Tiêu chí không thuộc round / test CRUD |
| `round1Id` | Round thuộc `phase1Id`, chứa `criterion1Id`, weight `1` | Round hợp lệ |
| `assignment1Id` | `student1Id` – `mentor1Id` – `phase1Id` | Phân công có quyền truy cập |
| `result1Id` | Kết quả của `assignment1Id` / `round1Id` / `criterion1Id`, score `8.5` | Kết quả hợp lệ |

Thứ tự an toàn: Auth & Users → Profiles → Phase & Criteria → Round & Round Criteria → Assignment → Assessment Result → các ca update/read/negative → cleanup. Luôn dùng tên, username, email và mã sinh viên chứa `runId` để tránh va chạm dữ liệu của lần chạy trước.

## 3. Auth và Users

| ID | Request / dữ liệu | Kỳ vọng |
| --- | --- | --- |
| AUTH-01 | `POST /api/auth/login`, `ADMIN` / `admin` | `200`; `data.accessToken` không rỗng và `data.tokenType=Bearer` |
| AUTH-02 | Login bằng `MENTOR`, `STUDENT`, Mentor2, Student2 | Mỗi login `200`; lưu token riêng |
| AUTH-03 | Sai mật khẩu hoặc username không tồn tại | `401`, `BAD_CREDENTIALS` |
| AUTH-04 | Username hoặc password rỗng | `400`, `INVALID_INPUT_DATA` |
| AUTH-05 | `GET /api/auth/me` với từng token hợp lệ | `200`; `data.username` và `data.role` đúng token |
| AUTH-06 | `GET /api/auth/me` với `SEC-01`, `SEC-02` | Mã lỗi như bảng ca chung |
| USER-01 | `GET /api/users` bằng ADMIN | `200`; thấy các user seed và user QA |
| USER-02 | `GET /api/users?role=MENTOR`, sau đó `role=STUDENT` | `200`; toàn bộ phần tử có role đã lọc |
| USER-03 | `GET /api/users?role=INVALID` | `400`, `INVALID_INPUT_DATA` |
| USER-04 | `GET /api/users/{mentor2UserId}` bằng ADMIN | `200`; đúng user |
| USER-05 | `POST /api/users` tạo user MENTOR_2, STUDENT_2, ADMIN_2 | Mỗi request `201`; userId được lưu |
| USER-06 | Tạo user trùng username; tạo user trùng email | `400`, `DUPLICATE_RESOURCE` |
| USER-07 | Tạo/cập nhật với password < 8, email sai, role sai, trường bắt buộc rỗng | `400`, `INVALID_INPUT_DATA` |
| USER-08 | `PUT /api/users/{mentor2UserId}` đổi username/email/fullName/password | `200`; login bằng username + password mới thành công |
| USER-09 | `PUT /api/users/{student2UserId}/status` `{ "isActive": false }`, sau đó login | Update `200`; login bị từ chối `401`; đổi lại `true` thì login `200` |
| USER-10 | `PUT /api/users/{student2UserId}/role` đổi từ STUDENT sang MENTOR khi chưa có profile, sau đó đổi lại STUDENT | Cả hai request `200`; profile Student2 chỉ được tạo sau khi role đã trở lại STUDENT |
| USER-11 | Tạo profile Student/Mentor rồi đổi role profile owner sang role khác | `403`, `ACCESS_DENIED` |
| USER-12 | ADMIN đổi role của `ADMIN_2` | `403`, `ACCESS_DENIED` |
| USER-13 | `DELETE /api/users/{temporaryUserId}` không có profile/phân công | `204`; `GET` lại trả `404` |
| USER-14 | Thử mọi endpoint `/api/users/**` bằng MENTOR/STUDENT và `SEC-01` | `403` với token role khác, `401` không token |
| USER-15 | Dùng ID không tồn tại cho GET/PUT/status/role/delete | `404`, `RESOURCE_NOT_FOUND` |

## 4. Student và Mentor profiles

| ID | Request / dữ liệu | Kỳ vọng |
| --- | --- | --- |
| STU-01 | `POST /api/students` cho `student1Id` và `student2Id`, `studentCode` khác nhau | `201` và profile ID bằng user ID |
| STU-02 | Tạo profile bằng user role ADMIN/MENTOR | `403`, `ACCESS_DENIED` |
| STU-03 | Tạo lại profile hoặc dùng `studentCode` trùng | `400`, `DUPLICATE_RESOURCE` |
| STU-04 | `GET /api/students` bằng ADMIN | `200`; thấy cả Student1/Student2 |
| STU-05 | `GET /api/students/{student1Id}` bằng ADMIN và STUDENT1 | `200`; đúng profile |
| STU-06 | `GET` hoặc `PUT /api/students/{student2Id}` bằng STUDENT1 | `403`, `ACCESS_DENIED` |
| STU-07 | `PUT /api/students/{student1Id}` bằng STUDENT1, thay major/class/address | `200`; GET lại phản ánh dữ liệu mới |
| STU-08 | PUT Student1 với `studentCode` của Student2 | `400`, `DUPLICATE_RESOURCE` |
| STU-09 | `GET /api/students` bằng MENTOR1 trước khi có assignment | `200`, danh sách rỗng (hành vi hiện tại) |
| MENT-01 | `POST /api/mentors` cho `mentor1Id` và `mentor2Id` | `201` |
| MENT-02 | Tạo profile bằng user role ADMIN/STUDENT hoặc tạo profile Mentor lần hai | Sai role `403`; trùng profile `400`, `DUPLICATE_RESOURCE` |
| MENT-03 | `GET /api/mentors` bằng ADMIN/STUDENT | `200`; thấy Mentor1/Mentor2 |
| MENT-04 | `GET /api/mentors/{mentor1Id}` bằng ADMIN, STUDENT1 và MENTOR1 | `200` |
| MENT-05 | `GET` hoặc `PUT /api/mentors/{mentor2Id}` bằng MENTOR1 | `403`, `ACCESS_DENIED` |
| MENT-06 | `PUT /api/mentors/{mentor1Id}` bằng MENTOR1 | `200`; department/rank được cập nhật |
| PROFILE-01 | Chạy `VAL-01`, `NF-01` và `SEC-03` cho cả hai nhóm profile | Các mã lỗi đúng bảng ca chung |

## 5. Internship phases và evaluation criteria

| ID | Request / dữ liệu | Kỳ vọng |
| --- | --- | --- |
| PHASE-01 | `POST /api/internship_phases` tạo `phase1Id`, `phase2Id` | `201` |
| PHASE-02 | Tạo phase có `endDate < startDate` | `400`, `INVALID_INTERNSHIP_PHASE` |
| PHASE-03 | Tạo hoặc cập nhật thành `phaseName` trùng | `400`, `DUPLICATE_RESOURCE` |
| PHASE-04 | `GET /api/internship_phases` và `GET /{phase1Id}` bằng ADMIN, MENTOR, STUDENT | `200`; dữ liệu như nhau |
| PHASE-05 | `PUT /api/internship_phases/{phase2Id}` với date hợp lệ | `200`; GET xác nhận dữ liệu |
| PHASE-06 | `DELETE /api/internship_phases/{phase2Id}` khi chưa được tham chiếu | `204`; GET trả `404` |
| CRIT-01 | `POST /api/evaluation_criteria` tạo Criterion1 (`10`) và Criterion2 (`5`) | `201` |
| CRIT-02 | `maxScore=0`, âm, quá 3 số nguyên/2 số thập phân hoặc tên rỗng | `400`, `INVALID_INPUT_DATA` hoặc `INVALID_EVALUATION_CRITERION` |
| CRIT-03 | Tạo/cập nhật tên tiêu chí trùng | `400`, `DUPLICATE_RESOURCE` |
| CRIT-04 | GET list/detail bằng cả ba role; PUT Criterion2 với maxScore hợp lệ | `200` |
| CRIT-05 | Tạo Criterion3 không được round tham chiếu, sau đó DELETE | `204`; GET trả `404` |
| CATALOG-01 | POST/PUT/DELETE phase hoặc criterion bằng MENTOR/STUDENT | `403`, `ACCESS_DENIED` |
| CATALOG-02 | GET phase/criterion ID không tồn tại; mọi body không hợp lệ | `404` / `400` tương ứng |

## 6. Assessment rounds và round criteria

| ID | Request / dữ liệu | Kỳ vọng |
| --- | --- | --- |
| ROUND-01 | `POST /api/assessment_rounds` với `phase1Id`, khoảng ngày hợp lệ, `criteria:[{criterion1Id, weight:1}]` | `201`; lưu `round1Id` |
| ROUND-02 | Tạo round có `endDate < startDate` | `400`, `INVALID_ASSESSMENT_ROUND` |
| ROUND-03 | Tạo round với `phaseId`/`criterionId` không tồn tại | `404`, `RESOURCE_NOT_FOUND` |
| ROUND-04 | Tạo round có cùng criterion hai lần trong `criteria` | `400`, `DUPLICATE_RESOURCE` |
| ROUND-05 | `GET /api/assessment_rounds`, `GET ?phase_id={phase1Id}`, `GET /{round1Id}` bằng 3 role | `200`; filter chỉ trả round của phase yêu cầu |
| ROUND-06 | `PUT /api/assessment_rounds/{round1Id}` giữ nguyên phase, đổi name/date/description | `200` |
| RC-01 | `GET /api/round_criteria`, `GET ?round_id={round1Id}`, `GET /{roundCriterion1Id}` bằng 3 role | `200`; criterion1 thuộc round1 |
| RC-02 | `POST /api/round_criteria` thêm Criterion2 vào Round1 | `201`; lưu `roundCriterion2Id` |
| RC-03 | POST lại cặp Round1–Criterion2 | `400`, `DUPLICATE_RESOURCE` |
| RC-04 | `PUT /api/round_criteria/{roundCriterion2Id}` giữ round/criterion, đổi weight | `200` |
| RC-05 | PUT `roundCriterion2Id` nhưng đổi `roundId` hoặc `criterionId` | `400`, `INVALID_ASSESSMENT_ROUND` |
| RC-06 | `DELETE /api/round_criteria/{roundCriterion2Id}` | `204`; GET trả `404` |
| ROUND-07 | POST/PUT/DELETE round hoặc round criterion bằng MENTOR/STUDENT | `403`, `ACCESS_DENIED` |
| ROUND-08 | GET ID không tồn tại; query `round_id` không phải integer; body thiếu field/weight `<=0` | `404` / `400` tương ứng |

## 7. Internship assignments

| ID | Request / dữ liệu | Kỳ vọng |
| --- | --- | --- |
| ASSIGN-01 | `POST /api/internship_assignments` với Student1, Mentor1, Phase1 | `201`; status ban đầu `PENDING`; lưu `assignment1Id` |
| ASSIGN-02 | Tạo lại assignment cho cùng Student1 và Phase1, kể cả đổi Mentor | `400`, `DUPLICATE_RESOURCE` |
| ASSIGN-03 | POST có student/mentor/phase không tồn tại | `404`, `RESOURCE_NOT_FOUND` |
| ASSIGN-04 | `GET /api/internship_assignments` bằng ADMIN | `200`; thấy toàn bộ assignment |
| ASSIGN-05 | GET list/detail `assignment1Id` bằng MENTOR1 và STUDENT1 | `200`; chỉ thấy assignment được sở hữu |
| ASSIGN-06 | GET list bằng MENTOR2/STUDENT2 | `200`, không chứa `assignment1Id` |
| ASSIGN-07 | GET detail `assignment1Id` bằng MENTOR2/STUDENT2 | `403`, `ACCESS_DENIED` |
| ASSIGN-08 | `PUT /api/internship_assignments/{assignment1Id}/status` lần lượt `IN_PROGRESS`, `COMPLETED`, `CANCELLED` bằng ADMIN | Mỗi lần `200`, status được lưu đúng |
| ASSIGN-09 | PUT status enum sai hoặc thiếu | `400`, `INVALID_INPUT_DATA` |
| ASSIGN-10 | POST/PUT status bằng MENTOR/STUDENT | `403`, `ACCESS_DENIED` |
| ASSIGN-11 | GET/PUT với assignment ID không tồn tại | `404`, `RESOURCE_NOT_FOUND` |

## 8. Assessment results

| ID | Request / dữ liệu | Kỳ vọng |
| --- | --- | --- |
| RESULT-01 | `POST /api/assessment_results` bằng MENTOR1: Assignment1/round1/criterion1, score `8.5` | `201`; lưu `result1Id` |
| RESULT-02 | `GET /api/assessment_results` bằng ADMIN, MENTOR1, STUDENT1 | `200`; mỗi role thấy `result1Id` trong phạm vi của mình |
| RESULT-03 | GET results bằng MENTOR2/STUDENT2 | `200`, không chứa `result1Id` |
| RESULT-04 | MENTOR1 POST lại đúng Assignment/Round/Criterion | `400`, `DUPLICATE_RESOURCE` |
| RESULT-05 | MENTOR2 POST kết quả cho Assignment1 | `403`, `ACCESS_DENIED` |
| RESULT-06 | STUDENT1 hoặc ADMIN POST kết quả | `403`, `ACCESS_DENIED` |
| RESULT-07 | POST score âm hoặc `> maxScore` của criterion | `400`, `INVALID_ASSESSMENT_RESULT` |
| RESULT-08 | POST Criterion2 sau khi nó không còn thuộc Round1 | `400`, `INVALID_ASSESSMENT_RESULT` |
| RESULT-09 | POST Round thuộc Phase2 cho Assignment thuộc Phase1 | `400`, `INVALID_ASSESSMENT_RESULT` |
| RESULT-10 | POST với assignment/round/criterion không tồn tại | `404`, `RESOURCE_NOT_FOUND` |
| RESULT-11 | `PUT /api/assessment_results/{result1Id}` bằng MENTOR1, giữ 3 ID quan hệ, đổi score/comments | `200`; GET xác nhận score/comments mới |
| RESULT-12 | MENTOR2 PUT `result1Id` | `403`, `ACCESS_DENIED` |
| RESULT-13 | STUDENT1/ADMIN PUT result | `403`, `ACCESS_DENIED` |
| RESULT-14 | MENTOR1 PUT result nhưng đổi assignmentId, roundId hoặc criterionId | `400`, `INVALID_ASSESSMENT_RESULT` |
| RESULT-15 | PUT score âm, quá maxScore, ID result không tồn tại hoặc body thiếu field | `400` / `404` tương ứng |

## 9. Kiểm tra ràng buộc xoá và cleanup

Chạy các ca sau sau khi hoàn tất phần 8. Đây là kiểm thử ràng buộc quan hệ, không phải happy path.

| ID | Thao tác | Kỳ vọng |
| --- | --- | --- |
| DEL-01 | DELETE criterion/round/phase đang được `assessment_results` hoặc `internship_assignments` tham chiếu | Không được xoá dữ liệu. Nếu API trả `500` do FK database, ghi nhận đây là lỗi cần cải thiện thành lỗi nghiệp vụ `409` hoặc `400`. |
| DEL-02 | DELETE user có profile hoặc assignment tham chiếu | Không được xoá dữ liệu. Ghi nhận mã HTTP thực tế và xác nhận không mất dữ liệu liên quan. |
| DEL-03 | Xoá bằng API các resource độc lập có endpoint DELETE: round criterion, round không có result, criterion không được tham chiếu, phase không được tham chiếu và user QA không có profile | `204`; GET lại trả `404`. |
| DEL-04 | Cleanup dữ liệu có dependency | Hiện không có endpoint DELETE cho profile, assignment hoặc result. Dùng database test riêng và xoá thủ công theo thứ tự `assessmentresults → internshipassignments → roundcriteria → assessmentrounds → students/mentors → users`, sau khi đã lưu bằng chứng test. Đây là giới hạn API cần ghi nhận, không phải thao tác kiểm thử REST. |

## 10. Tiêu chí hoàn thành và bằng chứng

1. Toàn bộ ca `AUTH-*` đến `RESULT-*` đều có kết quả `PASS` hoặc ticket lỗi đã liên kết.
2. Mọi response lỗi có đúng HTTP status và `errorCode`; không có stack trace/sensitive data trong body.
3. Phân quyền và ownership ở các ca `STU-06`, `MENT-05`, `ASSIGN-06..07`, `RESULT-03/05/12` phải được kiểm thử bằng user thứ hai, không chỉ bằng tài khoản mặc định.
4. Lưu Postman run/export và log có `IMS_REQUEST` cùng `TRACE_ID` cho các ca lỗi hoặc lỗi bất thường.
5. Chụp kết quả PostgreSQL trước/sau cleanup để xác nhận không còn dữ liệu QA theo `runId`.

Collection tại [internship-management-system.postman_collection.json](./internship-management-system.postman_collection.json) đã tự động hoá luồng setup, toàn bộ method/path, các ca JWT/RBAC/ownership/duplicate/ràng buộc chính và cleanup độc lập. Chạy folder theo thứ tự; các biến token và ID được lưu tự động. Các biến thể validation còn lại được giữ trong bảng test case để chạy thủ công khi cần mở rộng phạm vi dữ liệu biên.
