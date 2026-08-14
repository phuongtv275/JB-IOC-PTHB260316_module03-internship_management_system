-- Run after schema.sql. Idempotent sample data: at least 20 rows for every table.
BEGIN;

INSERT INTO users (username, passwordhash, fullname, email, phonenumber, role)
SELECT 'sample_admin_' || n, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Sample Admin ' || n, 'sample_admin_' || n || '@example.test', '090000' || lpad(n::text, 4, '0'), 'ADMIN'
FROM generate_series(1, 20) n ON CONFLICT (username) DO NOTHING;
INSERT INTO users (username, passwordhash, fullname, email, phonenumber, role)
SELECT 'sample_mentor_' || n, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Sample Mentor ' || n, 'sample_mentor_' || n || '@example.test', '091000' || lpad(n::text, 4, '0'), 'MENTOR'
FROM generate_series(1, 20) n ON CONFLICT (username) DO NOTHING;
INSERT INTO users (username, passwordhash, fullname, email, phonenumber, role)
SELECT 'sample_student_' || n, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Sample Student ' || n, 'sample_student_' || n || '@example.test', '092000' || lpad(n::text, 4, '0'), 'STUDENT'
FROM generate_series(1, 20) n ON CONFLICT (username) DO NOTHING;

INSERT INTO mentors (mentorid, department, academicrank)
SELECT u.userid, 'Engineering', 'MSc' FROM users u WHERE u.username LIKE 'sample_mentor_%'
ON CONFLICT (mentorid) DO NOTHING;
INSERT INTO students (studentid, studentcode, major, class, dateofbirth, address)
SELECT u.userid, 'SAMPLE-' || lpad(substring(u.username from '[0-9]+$'), 3, '0'), 'Computer Science', 'PTHB260316', '2004-01-01', 'Hanoi'
FROM users u WHERE u.username LIKE 'sample_student_%' ON CONFLICT (studentid) DO NOTHING;

INSERT INTO internshipphases (phasename, startdate, enddate, description)
SELECT 'Sample Phase ' || n, date '2026-01-01' + (n - 1) * 30, date '2026-01-28' + (n - 1) * 30, 'Sample phase '
FROM generate_series(1, 20) n WHERE NOT EXISTS (SELECT 1 FROM internshipphases p WHERE p.phasename = 'Sample Phase ' || n);
INSERT INTO evaluationcriteria (criterionname, description, maxscore)
SELECT 'Sample Criterion ' || n, 'Sample criterion', 10 FROM generate_series(1, 20) n
WHERE NOT EXISTS (SELECT 1 FROM evaluationcriteria c WHERE c.criterionname = 'Sample Criterion ' || n);
INSERT INTO assessmentrounds (phaseid, roundname, startdate, enddate, description)
SELECT p.phaseid, 'Sample Round ' || n, p.startdate, p.enddate, 'Sample round'
FROM generate_series(1, 20) n JOIN internshipphases p ON p.phasename = 'Sample Phase ' || n
WHERE NOT EXISTS (SELECT 1 FROM assessmentrounds r WHERE r.roundname = 'Sample Round ' || n);
INSERT INTO roundcriteria (roundid, criterionid, weight)
SELECT r.roundid, c.criterionid, 1 FROM generate_series(1, 20) n
JOIN assessmentrounds r ON r.roundname = 'Sample Round ' || n
JOIN evaluationcriteria c ON c.criterionname = 'Sample Criterion ' || n
ON CONFLICT (roundid, criterionid) DO NOTHING;
INSERT INTO internshipassignments (studentid, mentorid, phaseid, status)
SELECT s.studentid, m.mentorid, p.phaseid, 'IN_PROGRESS'
FROM generate_series(1, 20) n
JOIN students s ON s.studentcode = 'SAMPLE-' || lpad(n::text, 3, '0')
JOIN mentors m ON m.mentorid = (SELECT u.userid FROM users u WHERE u.username = 'sample_mentor_' || n)
JOIN internshipphases p ON p.phasename = 'Sample Phase ' || n
ON CONFLICT (studentid, phaseid) DO NOTHING;
INSERT INTO assessmentresults (assignmentid, roundid, criterionid, score, comments, evaluatedby)
SELECT a.assignmentid, r.roundid, c.criterionid, 8.5, 'Sample result', a.mentorid
FROM generate_series(1, 20) n
JOIN internshipassignments a ON a.studentid = (SELECT s.studentid FROM students s WHERE s.studentcode = 'SAMPLE-' || lpad(n::text, 3, '0'))
JOIN assessmentrounds r ON r.roundname = 'Sample Round ' || n
JOIN evaluationcriteria c ON c.criterionname = 'Sample Criterion ' || n
ON CONFLICT (assignmentid, roundid, criterionid) DO NOTHING;

COMMIT;
