-- Insert Users
INSERT INTO users (user_id, user_name, user_mail_id, user_category) VALUES
(1001, 'Alice Johnson', 'alice.johnson@example.com', 'faculty'),
(1002, 'Bob Smith', 'bob.smith@example.com', 'teacher'),
(1003, 'Charlie Brown', 'charlie.brown@example.com', 'admin'),
(1004, 'Diana Prince', 'diana.prince@example.com', 'faculty'),
(1005, 'Ethan Hunt', 'ethan.hunt@example.com', 'faculty'),
(1006, 'Ethan Brown', 'ethan.brown@example.com', 'admin');

-- Insert Academic Years
INSERT INTO ACADEMICYEARS (AYRID, AYRNAME, AYRROWSTATE, AYRFIELD1) VALUES
(10, '2023-2024', 1, 10),
(11, '2024-2025', 1, 20);

-- Insert Terms (linked to academic years)
INSERT INTO TERMS (TRMID, TRMNAME, TRMROWSTATE, TRMFIELD1, TRMAYRID) VALUES
(1, 'Autumn', 1, 101, 10),
(2, 'Winter', 1, 102, 10),
(3, 'Summer', 1, 103, 10),
(4, 'Autumn', 1, 104, 11),
(5, 'Winter', 1, 105, 11),
(6, 'Summer', 1, 106, 11);

-- Insert Courses
INSERT INTO COURSES (CRSID, CRSNAME, CRSCODE, CRSROWSTATE) VALUES
(101, 'Introduction to Computer Science', 'CS101', 1),
(102, 'Data Structures', 'CS102', 1),
(103, 'Algorithms', 'CS103', 1),
(104, 'Database Systems', 'CS104', 1),
(105, 'Operating Systems', 'CS105', 1),
(106, 'Software Engineering', 'CS106', 1),
(107, 'Computer Networks', 'CS107', 1),
(108, 'Artificial Intelligence', 'CS108', 1),
(109, 'Machine Learning', 'CS109', 1),
(110, 'Web Technologies', 'CS110', 1);

-- Insert TermCourses (linking terms, courses, and faculty)
INSERT INTO TERMCOURSES (TCRID, TCRTRMID, TCRCRSID, TCRFACULTYID, TCRROWSTATE) VALUES
(1, 1, 101, 1001, 1),
(2, 1, 102, 1002, 1),
(3, 2, 103, 1003, 1),
(4, 2, 104, 1002, 1),
(5, 3, 105, 1003, 1),
(6, 3, 106, 1004, 1),
(7, 4, 107, 1002, 1),
(8, 5, 108, 1001, 1),
(9, 6, 109, 1005, 1),
(10, 6, 110, 1006, 1);

-- Insert Exam Types
INSERT INTO EGEXAMM1 (EXAMTYPE_ID, EXAMTYPE_TITLE, ROW_ST) VALUES
(1, 'Midterm Exam 1', 1),
(2, 'Midterm Exam 2', 1),
(3, 'Final Exam', 1);

-- Insert Exam Details linking exams to TermCourses
INSERT INTO EGGRSTT1 (EXMRID, EXAMTYPE_ID, TCRID, ROW_ST) VALUES
(1, 1, 1, 1),
(2, 2, 2, 1),
(3, 1, 3, 1),
(4, 1, 4, 1),
(11, 2, 3, 1),
(12, 2, 4, 1),
(13, 3, 3, 1),
(14, 3, 4, 1),
(5, 2, 5, 1),
(6, 3, 6, 1),
(7, 1, 7, 1),
(8, 2, 8, 1),
(9, 3, 9, 1),
(10, 1, 10, 1);

-- PROGRAMS
INSERT INTO PROGRAMS (prgid, prgname) VALUES
(1, 'B.Tech in ICT'),
(2, 'B.Tech in ICT-CS'),
(3, 'B.Tech in MNC'),
(4, 'B.Tech in EVD');

-- BATCHES
INSERT INTO BATCHES (bchid, bchprgid) VALUES
(101, 1), -- ICT
(102, 2), -- ICT-CS
(103, 3), -- MNC
(104, 4); -- EVD

-- ADDRESSES (dummy addresses)
INSERT INTO ADDRESSES (adrid, adrline1, adrline2) VALUES
(1001, 'A1', 'A2'),
(1002, 'B1', 'B2'),
(1003, 'C1', 'C2'),
(1004, 'D1', 'D2'),
(1005, 'E1', 'E2'),
(1006, 'F1', 'F2'),
(1007, 'G1', 'G2'),
(1008, 'H1', 'H2'),
(1009, 'I1', 'I2');

-- SEMESTERS
INSERT INTO SEMESTERS (strid, strrowstate, strseqno) VALUES
(1, 1, 1);

-- STUDENTS
INSERT INTO STUDENTS (STUDENTID, stdfirstname, stdlastname, stdrowstate, stdbchid, CURRADRID, PRMTADRID, EMGRADRID, stdemail) VALUES
(202201001, 'Alice', 'Johnson', 1, 101, 1001, 1002, 1003, '202201001@daiict.ac.in'),
(202201002, 'Bob', 'Smith', 1, 102, 1002, 1003, 1001, '202201002@daiict.ac.in'),
(202201003, 'Charlie', 'Brown', 1, 103, 1003, 1001, 1002, '202201003@daiict.ac.in'),
(202201004, 'David', 'Wilson', 1, 104, 1001, 1002, 1003, '202201004@daiict.ac.in'),
(202201005, 'Eva', 'Davis', 1, 101, 1002, 1003, 1001, '202201005@daiict.ac.in'),
(202201006, 'Frank', 'Miller', 1, 102, 1003, 1001, 1002, '202201006@daiict.ac.in'),
(202201007, 'Grace', 'Lee', 1, 103, 1001, 1002, 1003, '202201007@daiict.ac.in'),
(202201008, 'Helen', 'Walker', 1, 104, 1002, 1003, 1001, '202201008@daiict.ac.in'),
(202201010, 'Helen', 'Walker', 1, 104, 1002, 1003, 1001, '202201010@daiict.ac.in'); -- Duplicate entry intentionally retained

-- STUDENTREGISTRATIONS
INSERT INTO STUDENTREGISTRATIONS (srgid, srgstdid, srgstrid, srgrowstate) VALUES
(1, 1, 1, 1),
(2, 2, 1, 1),
(3, 3, 1, 1),
(4, 4, 1, 1),
(5, 5, 1, 1),
(6, 6, 1, 1),
(7, 7, 1, 1),
(8, 8, 1, 1),
(9, 20, 1, 1);


-- Sample ENROLLMENT inserts
INSERT INTO ENROLLMENTS (ENRID, STUDENTID, CRSID, ENRSTATUS,TRMID) VALUES
(1, 202201001, 101, 'enrolled',1),
(2, 202201002, 102, 'enrolled',1),
(3, 202201003, 102, 'enrolled',1),
(4, 202201004, 102, 'enrolled',1),
(5, 202201005, 101, 'enrolled',1),
(6, 202201006, 103, 'enrolled',1),
(7, 202201007, 101, 'enrolled',1),
(8, 202201008, 103, 'enrolled',1);

-- Sample GRADE inserts
INSERT INTO GRADES (GRADEID, ENRID, GRADE_VALUE, GRADE_REMARKS, ROWSTATE, EXAM_TYPE) VALUES
(1, 1, 'AA', 'Excellent performance', 1, 1),
(2, 2, 'BB', 'Good job', 1, 1),
(3, 3, 'AB', 'Satisfactory', 1, 1),
(4, 4, 'AA', 'Very good', 1, 2),
(5, 5, 'CC', 'Excellent', 1, 1),
(6, 6, 'BC', 'Needs improvement', 1, 1),
(7, 7, 'CD', 'Good', 1, 2),
(8, 8, 'F', 'Excellent', 1, 1);

-- -- Insert into student_grades table
-- INSERT INTO student_grades (student_id, course_id, grade) VALUES
-- (1, 101, 'A'),
-- (5, 101, 'C'),
-- (2, 101, 'B+'),
-- (3, 102, 'B'),
-- (4, 102, 'A-'),
-- (5, 103, 'A'),
-- (6, 104, 'C'),
-- (7, 105, 'B+'),
-- (8, 106, 'A');
