-- ============================================================
-- Indexes: Hệ thống Quản lý Thực tập (PostgreSQL)
-- Chạy SAU khi đã tạo xong schema.sql.
--
-- Lưu ý: UNIQUE constraint trong schema.sql (Username, Email,
-- StudentCode, PhaseName, CriterionName, (RoundID,CriterionID),
-- (StudentID,PhaseID), (AssignmentID,RoundID,CriterionID)) đã tự
-- tạo index đi kèm — KHÔNG tạo trùng ở đây.
--
-- PostgreSQL không tự đánh index cho cột FOREIGN KEY (khác với
-- một số DB khác), nên các cột FK dùng để filter/join đều cần
-- index thủ công — đây là phần chiếm phần lớn file này.
-- ============================================================

-- ---- Users ----
-- GET /api/users có thể lọc theo vai trò (role)
CREATE INDEX IF NOT EXISTS idx_users_role ON Users (Role);
-- lọc user đang active/bị khóa (dùng ở nhiều màn hình quản trị)
CREATE INDEX IF NOT EXISTS idx_users_is_active ON Users (IsActive);

-- ---- Students ----
-- lọc/tìm kiếm sinh viên theo ngành, lớp (danh sách sinh viên)
CREATE INDEX IF NOT EXISTS idx_students_major ON Students (Major);
CREATE INDEX IF NOT EXISTS idx_students_class ON Students (Class);

-- ---- Mentors ----
-- lọc mentor theo bộ môn/khoa
CREATE INDEX IF NOT EXISTS idx_mentors_department ON Mentors (Department);

-- ---- InternshipPhases ----
-- lọc giai đoạn thực tập đang diễn ra theo khoảng thời gian
CREATE INDEX IF NOT EXISTS idx_phases_date_range ON InternshipPhases (StartDate, EndDate);

-- ---- AssessmentRounds ----
-- GET /api/assessment_rounds có thể lọc theo phase_id (FK, dùng thường xuyên nhất)
CREATE INDEX IF NOT EXISTS idx_rounds_phase_id ON AssessmentRounds (PhaseID);
CREATE INDEX IF NOT EXISTS idx_rounds_is_active ON AssessmentRounds (IsActive);

-- ---- RoundCriteria ----
-- UNIQUE(RoundID, CriterionID) đã cover tra cứu theo RoundID (cột đầu composite index),
-- nhưng tra cứu ngược theo CriterionID một mình thì chưa có index -> thêm riêng.
CREATE INDEX IF NOT EXISTS idx_round_criteria_criterion_id ON RoundCriteria (CriterionID);

-- ---- InternshipAssignments ----
-- GET /api/internship_assignments lọc theo quyền và user_id (student/mentor) + theo phase
CREATE INDEX IF NOT EXISTS idx_assignments_student_id ON InternshipAssignments (StudentID);
CREATE INDEX IF NOT EXISTS idx_assignments_mentor_id ON InternshipAssignments (MentorID);
CREATE INDEX IF NOT EXISTS idx_assignments_phase_id ON InternshipAssignments (PhaseID);
CREATE INDEX IF NOT EXISTS idx_assignments_status ON InternshipAssignments (Status);

-- ---- AssessmentResults ----
-- UNIQUE(AssignmentID, RoundID, CriterionID) đã cover tra cứu theo AssignmentID,
-- nhưng GET /api/assessment_results còn lọc riêng theo round_id/criterion_id/user_id.
CREATE INDEX IF NOT EXISTS idx_results_round_id ON AssessmentResults (RoundID);
CREATE INDEX IF NOT EXISTS idx_results_criterion_id ON AssessmentResults (CriterionID);
-- MENTOR chỉ xem/sửa kết quả do chính mình chấm -> lọc theo EvaluatedBy rất thường xuyên
CREATE INDEX IF NOT EXISTS idx_results_evaluated_by ON AssessmentResults (EvaluatedBy);