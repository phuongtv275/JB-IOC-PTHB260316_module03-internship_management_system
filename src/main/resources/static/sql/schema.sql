-- ============================================================
-- Schema: Hệ thống Quản lý Thực tập (PostgreSQL)
-- Sinh tự động từ sheet "Cơ sở dữ liệu" trong file Excel gốc,
-- chuyển đổi sang cú pháp PostgreSQL:
--   - AUTO_INCREMENT          -> GENERATED ALWAYS AS IDENTITY
--   - ENUM(...)               -> CREATE TYPE ... AS ENUM (PostgreSQL không hỗ trợ inline ENUM)
--   - NVARCHAR(n)/VARCHAR(n)  -> VARCHAR(n) (PostgreSQL không phân biệt N-prefix, luôn UTF-8)
--   - NVARCHAR(MAX)           -> TEXT
--   - DATETIME                -> TIMESTAMP
--   - "... ON UPDATE CURRENT_TIMESTAMP" -> trigger set_updated_at() dùng chung
-- ============================================================

-- ---- ENUM types dùng chung ----
CREATE TYPE user_role AS ENUM ('ADMIN', 'MENTOR', 'STUDENT');
CREATE TYPE assignment_status AS ENUM ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');

-- ---- Trigger function dùng chung để tự cập nhật UpdatedAt ----
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updatedat = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 1. Users — thông tin cơ bản mọi người dùng (ADMIN, MENTOR, STUDENT)
CREATE TABLE Users (
                       UserID        INT             GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       Username      VARCHAR(50)     NOT NULL UNIQUE,
                       PasswordHash  VARCHAR(255)    NOT NULL,               -- BCrypt hoặc thuật toán mạnh tương đương
                       FullName      VARCHAR(100)    NOT NULL,
                       Email         VARCHAR(100)    NOT NULL UNIQUE,
                       PhoneNumber   VARCHAR(20),
                       Role          user_role       NOT NULL,
                       IsActive      BOOLEAN         DEFAULT TRUE,
                       CreatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                       UpdatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON Users
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 2. Students — thông tin riêng của tài khoản role=STUDENT
CREATE TABLE Students (
                          StudentID     INT             PRIMARY KEY,
                          StudentCode   VARCHAR(20)     NOT NULL UNIQUE,
                          Major         VARCHAR(100),
                          Class         VARCHAR(50),
                          DateOfBirth   DATE,
                          Address       VARCHAR(255),
                          CreatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                          UpdatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_students_user FOREIGN KEY (StudentID) REFERENCES Users(UserID)
);

CREATE TRIGGER trg_students_updated_at
    BEFORE UPDATE ON Students
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 3. Mentors — thông tin riêng của tài khoản role=MENTOR
CREATE TABLE Mentors (
                         MentorID      INT             PRIMARY KEY,
                         Department    VARCHAR(100),
                         AcademicRank  VARCHAR(50),
                         CreatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                         UpdatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_mentors_user FOREIGN KEY (MentorID) REFERENCES Users(UserID)
);

CREATE TRIGGER trg_mentors_updated_at
    BEFORE UPDATE ON Mentors
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 4. InternshipPhases — các giai đoạn thực tập (vd: "Thực tập cơ sở 1")
CREATE TABLE InternshipPhases (
                                  PhaseID       INT             GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                  PhaseName     VARCHAR(100)    NOT NULL UNIQUE,
                                  StartDate     DATE            NOT NULL,
                                  EndDate       DATE            NOT NULL,
                                  Description   TEXT,
                                  CreatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                                  UpdatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER trg_phases_updated_at
    BEFORE UPDATE ON InternshipPhases
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 5. EvaluationCriteria — tiêu chí dùng để đánh giá sinh viên
CREATE TABLE EvaluationCriteria (
                                    CriterionID   INT             GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                    CriterionName VARCHAR(200)    NOT NULL UNIQUE,
                                    Description   TEXT,
                                    MaxScore      DECIMAL(5,2)    NOT NULL CHECK (MaxScore > 0),
                                    CreatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                                    UpdatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER trg_criteria_updated_at
    BEFORE UPDATE ON EvaluationCriteria
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 6. AssessmentRounds — các đợt đánh giá cụ thể thuộc một giai đoạn thực tập
CREATE TABLE AssessmentRounds (
                                  RoundID       INT             GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                  PhaseID       INT             NOT NULL,
                                  RoundName     VARCHAR(100)    NOT NULL,
                                  StartDate     DATE            NOT NULL,
                                  EndDate       DATE            NOT NULL,
                                  Description   TEXT,
                                  IsActive      BOOLEAN         DEFAULT TRUE,
                                  CreatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                                  UpdatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                                  CONSTRAINT fk_rounds_phase FOREIGN KEY (PhaseID) REFERENCES InternshipPhases(PhaseID)
);

CREATE TRIGGER trg_rounds_updated_at
    BEFORE UPDATE ON AssessmentRounds
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 7. RoundCriteria — bảng trung gian AssessmentRounds <-> EvaluationCriteria (kèm trọng số)
CREATE TABLE RoundCriteria (
                               RoundCriterionID INT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               RoundID          INT          NOT NULL,
                               CriterionID      INT          NOT NULL,
                               Weight           DECIMAL(5,2) NOT NULL CHECK (Weight > 0),
                               CreatedAt        TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                               UpdatedAt        TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_rc_round FOREIGN KEY (RoundID) REFERENCES AssessmentRounds(RoundID),
                               CONSTRAINT fk_rc_criterion FOREIGN KEY (CriterionID) REFERENCES EvaluationCriteria(CriterionID),
                               CONSTRAINT uq_round_criterion UNIQUE (RoundID, CriterionID)
);

CREATE TRIGGER trg_round_criteria_updated_at
    BEFORE UPDATE ON RoundCriteria
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 8. InternshipAssignments — phân công sinh viên cho giáo viên hướng dẫn trong một giai đoạn
CREATE TABLE InternshipAssignments (
                                       AssignmentID  INT             GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                       StudentID     INT             NOT NULL,
                                       MentorID      INT             NOT NULL,
                                       PhaseID       INT             NOT NULL,
                                       AssignedDate  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                                       Status        assignment_status DEFAULT 'PENDING',
                                       CreatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                                       UpdatedAt     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                                       CONSTRAINT fk_assign_student FOREIGN KEY (StudentID) REFERENCES Students(StudentID),
                                       CONSTRAINT fk_assign_mentor FOREIGN KEY (MentorID) REFERENCES Mentors(MentorID),
                                       CONSTRAINT fk_assign_phase FOREIGN KEY (PhaseID) REFERENCES InternshipPhases(PhaseID),
                                       CONSTRAINT uq_student_phase UNIQUE (StudentID, PhaseID)
);

CREATE TRIGGER trg_assignments_updated_at
    BEFORE UPDATE ON InternshipAssignments
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 9. AssessmentResults — kết quả đánh giá chi tiết của mentor cho sinh viên theo từng tiêu chí
CREATE TABLE AssessmentResults (
                                   ResultID        INT           GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                   AssignmentID    INT           NOT NULL,
                                   RoundID         INT           NOT NULL,
                                   CriterionID     INT           NOT NULL,
                                   Score           DECIMAL(5,2)  NOT NULL CHECK (Score >= 0),
                                   Comments        TEXT,
                                   EvaluatedBy     INT           NOT NULL,                -- FK -> Users.UserID (Role = 'MENTOR')
                                   EvaluationDate  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                                   CreatedAt       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                                   UpdatedAt       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                                   CONSTRAINT fk_result_assignment FOREIGN KEY (AssignmentID) REFERENCES InternshipAssignments(AssignmentID),
                                   CONSTRAINT fk_result_round FOREIGN KEY (RoundID) REFERENCES AssessmentRounds(RoundID),
                                   CONSTRAINT fk_result_criterion FOREIGN KEY (CriterionID) REFERENCES EvaluationCriteria(CriterionID),
                                   CONSTRAINT fk_result_evaluator FOREIGN KEY (EvaluatedBy) REFERENCES Users(UserID),
                                   CONSTRAINT uq_assignment_round_criterion UNIQUE (AssignmentID, RoundID, CriterionID)
);

CREATE TRIGGER trg_results_updated_at
    BEFORE UPDATE ON AssessmentResults
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
