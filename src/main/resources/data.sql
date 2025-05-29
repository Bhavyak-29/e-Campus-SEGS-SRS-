-- Insert User

-- user entity
INSERT INTO users (user_id, user_name, user_mail_id, user_category) VALUES
(1001, 'Alice Johnson', 'alice.johnson@example.com', 'faculty'),
(1002, 'Bob Smith', 'bob.smith@example.com', 'teacher'),
(1003, 'Charlie Brown', 'charlie.brown@example.com', 'admin'),
(1004, 'Diana Prince', 'diana.prince@example.com', 'faculty'),
(1005, 'Ethan Hunt', 'ethan.hunt@example.com', 'faculty'),
(1006, 'Ethan Brown', 'ethan.brown@example.com', 'admin');

-- Insert Academic Years
INSERT INTO ACADEMICYEARS (AYRID, AYRNAME, AYRROWSTATE, AYRFIELD1) VALUES (10, '2023-2024', 1, 10);
INSERT INTO ACADEMICYEARS (AYRID, AYRNAME, AYRROWSTATE, AYRFIELD1) VALUES (11, '2024-2025', 1, 20);

-- Insert Terms (linked to updated academic year IDs)
INSERT INTO TERMS (TRMID, TRMNAME, TRMROWSTATE, TRMFIELD1, TRMAYRID) VALUES (1, 'Autumn', 1, 101, 10);
INSERT INTO TERMS (TRMID, TRMNAME, TRMROWSTATE, TRMFIELD1, TRMAYRID) VALUES (2, 'Winter', 1, 102, 10);
INSERT INTO TERMS (TRMID, TRMNAME, TRMROWSTATE, TRMFIELD1, TRMAYRID) VALUES (3, 'Summer', 1, 103, 10);
INSERT INTO TERMS (TRMID, TRMNAME, TRMROWSTATE, TRMFIELD1, TRMAYRID) VALUES (4, 'Autumn', 1, 104, 11);
INSERT INTO TERMS (TRMID, TRMNAME, TRMROWSTATE, TRMFIELD1, TRMAYRID) VALUES (5, 'Winter', 1, 105, 11);
INSERT INTO TERMS (TRMID, TRMNAME, TRMROWSTATE, TRMFIELD1, TRMAYRID) VALUES (6, 'Summer', 1, 106, 11);
-- INSERT INTO TERMS (TRMID, TRMNAME, TRMROWSTATE, TRMFIELD1, TRMAYRID) VALUES (7, 'Term A', 1, 107, 10);
-- INSERT INTO TERMS (TRMID, TRMNAME, TRMROWSTATE, TRMFIELD1, TRMAYRID) VALUES (8, 'Term B', 1, 108, 10);
-- INSERT INTO TERMS (TRMID, TRMNAME, TRMROWSTATE, TRMFIELD1, TRMAYRID) VALUES (9, 'Term C', 1, 109, 11);
-- INSERT INTO TERMS (TRMID, TRMNAME, TRMROWSTATE, TRMFIELD1, TRMAYRID) VALUES (10, 'Term D', 1, 110, 11);

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


INSERT INTO EGEXAMM1 (EXAMTYPE_ID, EXAMTYPE_TITLE, ROW_ST) VALUES (1, 'Midterm Exam 1', 1);
INSERT INTO EGEXAMM1 (EXAMTYPE_ID, EXAMTYPE_TITLE, ROW_ST) VALUES (3, 'Final Exam', 1);
INSERT INTO EGEXAMM1 (EXAMTYPE_ID, EXAMTYPE_TITLE, ROW_ST) VALUES (2, 'Midterm Exam 2', 1);

INSERT INTO EGGRSTT1 (EXMRID,EXAMTYPE_ID, TCRID, ROW_ST) VALUES
(1,1, 1, 1),
(2,2, 2, 1),
(3,3, 3, 1),
(4,1, 4, 1),
(5,2, 5, 1),
(6,3, 6, 1),
(7,1, 7, 1),
(8,2, 8, 1),
(9,3, 9, 1),
(10,1, 10, 1);

INSERT INTO STUDENT (ROLL_NO, NAME, BRANCH, BATCH, SECTION, FATHER_NAME) VALUES
('202201001', 'Alice Johnson', 'ICT', '2022', 'A', 'Michael Johnson'),
('202201002', 'Bob Smith', 'ICT-CS', '2022', 'B', 'Robert Smith'),
('202201003', 'Charlie Brown', 'MNC', '2022', '', 'James Brown'),
('202201004', 'David Wilson', 'EVD', '2022', '', 'John Wilson'),
('202201005', 'Eva Davis', 'ICT', '2022', 'A', 'William Davis'),
('202201006', 'Frank Miller', 'ICT-CS', '2022', 'B', 'Charles Miller'),
('202201007', 'Grace Lee', 'MNC', '2022', '', 'Thomas Lee'),
('202201008', 'Helen Walker', 'EVD', '2022', '', 'Robert Walker'),
('202201009', 'Ivan Harris', 'ICT', '2022', 'B', 'Michael Harris'),
('202201010', 'Jack Young', 'ICT-CS', '2022', 'A', 'David Young'),
('202201011', 'Karen Hall', 'MNC', '2022', '', 'John Hall'),
('202201012', 'Larry King', 'EVD', '2022', '', 'James King'),
('202201013', 'Mona Wright', 'ICT', '2022', 'B', 'William Wright'),
('202201014', 'Nina Scott', 'ICT-CS', '2022', 'A', 'Charles Scott'),
('202201015', 'Oliver Green', 'MNC', '2022', '', 'Thomas Green'),
('202201016', 'Paula Adams', 'EVD', '2022', '', 'Robert Adams'),
('202201017', 'Quincy Baker', 'ICT', '2022', 'A', 'Michael Baker'),
('202201018', 'Rachel Nelson', 'ICT-CS', '2022', 'B', 'David Nelson'),
('202201019', 'Steve Carter', 'MNC', '2022', '', 'John Carter'),
('202201020', 'Tina Mitchell', 'EVD', '2022', '', 'James Mitchell'),
('202201021', 'Uma Roberts', 'ICT', '2022', 'B', 'William Roberts'),
('202201022', 'Victor Phillips', 'ICT-CS', '2022', 'A', 'Charles Phillips'),
('202201023', 'Wendy Campbell', 'MNC', '2022', '', 'Thomas Campbell'),
('202201024', 'Xander Parker', 'EVD', '2022', '', 'Robert Parker'),
('202201025', 'Yvonne Evans', 'ICT', '2022', 'A', 'Michael Evans'),
('202201026', 'Zack Edwards', 'ICT-CS', '2022', 'B', 'David Edwards'),
('202201027', 'Aaron Collins', 'MNC', '2022', '', 'John Collins'),
('202201028', 'Bella Stewart', 'EVD', '2022', '', 'James Stewart'),
('202201029', 'Caleb Sanchez', 'ICT', '2022', 'B', 'William Sanchez'),
('202201030', 'Diana Morris', 'ICT-CS', '2022', 'A', 'Charles Morris'),
('202201031', 'Ethan Rogers', 'MNC', '2022', '', 'Thomas Rogers'),
('202201032', 'Fiona Reed', 'EVD', '2022', '', 'Robert Reed'),
('202201033', 'George Cook', 'ICT', '2022', 'A', 'Michael Cook'),
('202201034', 'Hannah Morgan', 'ICT-CS', '2022', 'B', 'David Morgan'),
('202201035', 'Isaac Bell', 'MNC', '2022', '', 'John Bell'),
('202201036', 'Julia Murphy', 'EVD', '2022', '', 'James Murphy'),
('202201037', 'Kevin Bailey', 'ICT', '2022', 'B', 'William Bailey'),
('202201038', 'Laura Rivera', 'ICT-CS', '2022', 'A', 'Charles Rivera'),
('202201039', 'Mark Cooper', 'MNC', '2022', '', 'Thomas Cooper'),
('202201040', 'Nora Richardson', 'EVD', '2022', '', 'Robert Richardson'),
('202201041', 'Owen Cox', 'ICT', '2022', 'A', 'Michael Cox'),
('202201042', 'Pamela Howard', 'ICT-CS', '2022', 'B', 'David Howard'),
('202201043', 'Quinn Ward', 'MNC', '2022', '', 'John Ward'),
('202201044', 'Rebecca Torres', 'EVD', '2022', '', 'James Torres'),
('202201045', 'Samuel Peterson', 'ICT', '2022', 'B', 'William Peterson'),
('202201046', 'Tracy Gray', 'ICT-CS', '2022', 'A', 'Charles Gray'),
('202201047', 'Ulysses Ramirez', 'MNC', '2022', '', 'Thomas Ramirez'),
('202201048', 'Victoria James', 'EVD', '2022', '', 'Robert James'),
('202201049', 'William Watson', 'ICT', '2022', 'A', 'Michael Watson'),
('202201050', 'Ximena Brooks', 'ICT-CS', '2022', 'B', 'David Brooks'),
('202201051', 'Yusuf Kelly', 'MNC', '2022', '', 'John Kelly'),
('202201052', 'Zara Sanders', 'EVD', '2022', '', 'James Sanders'),
('202201053', 'Adam Price', 'ICT', '2022', 'B', 'William Price'),
('202201054', 'Betty Bennett', 'ICT-CS', '2022', 'A', 'Charles Bennett'),
('202201055', 'Carl Wood', 'MNC', '2022', '', 'Thomas Wood'),
('202201056', 'Daisy Barnes', 'EVD', '2022', '', 'Robert Barnes'),
('202201057', 'Edward Ross', 'ICT', '2022', 'A', 'Michael Ross'),
('202201058', 'Felicity Henderson', 'ICT-CS', '2022', 'B', 'David Henderson'),
('202201059', 'Gavin Coleman', 'MNC', '2022', '', 'John Coleman'),
('202201060', 'Holly Jenkins', 'EVD', '2022', '', 'James Jenkins'),
('202201061', 'Ian Perry', 'ICT', '2022', 'B', 'William Perry'),
('202201062', 'Jane Powell', 'ICT-CS', '2022', 'A', 'Charles Powell'),
('202201063', 'Kyle Long', 'MNC', '2022', '', 'Thomas Long'),
('202201064', 'Lily Patterson', 'EVD', '2022', '', 'Robert Patterson'),
('202201065', 'Mason Hughes', 'ICT', '2022', 'A', 'Michael Hughes'),
('202201066', 'Nina Flores', 'ICT-CS', '2022', 'B', 'David Flores'),
('202201067', 'Oscar Washington', 'MNC', '2022', '', 'John Washington'),
('202201068', 'Paige Butler', 'EVD', '2022', '', 'James Butler'),
('202201069', 'Quentin Simmons', 'ICT', '2022', 'B', 'William Simmons'),
('202201070', 'Rose Foster', 'ICT-CS', '2022', 'A', 'Charles Foster'),
('202201071', 'Sean Gonzales', 'MNC', '2022', '', 'Thomas Gonzales'),
('202201072', 'Tara Bryant', 'EVD', '2022', '', 'Robert Bryant'),
('202201073', 'Umar Alexander', 'ICT', '2022', 'A', 'Michael Alexander'),
('202201074', 'Vera Russell', 'ICT-CS', '2022', 'B', 'David Russell'),
('202201075', 'Wesley Griffin', 'MNC', '2022', '', 'John Griffin'),
('202201076', 'Xena Diaz', 'EVD', '2022', '', 'James Diaz'),
('202201077', 'Yara Hayes', 'ICT', '2022', 'B', 'William Hayes'),
('202201078', 'Zane Myers', 'ICT-CS', '2022', 'A', 'Charles Myers'),
('202201079', 'Aaron Ford', 'MNC', '2022', '', 'Thomas Ford'),
('202201080', 'Bella Hamilton', 'EVD', '2022', '', 'Robert Hamilton'),
('202201081', 'Calvin Graham', 'ICT', '2022', 'A', 'Michael Graham'),
('202201082', 'Denise Sullivan', 'ICT-CS', '2022', 'B', 'David Sullivan'),
('202201083', 'Eli Woods', 'MNC', '2022', '', 'John Woods'),
('202201084', 'Faith West', 'EVD', '2022', '', 'James West'),
('202201085', 'Gabe Cole', 'ICT', '2022', 'B', 'William Cole'),
('202201086', 'Hailey Jordan', 'ICT-CS', '2022', 'A', 'Charles Jordan'),
('202201087', 'Ian Reynolds', 'MNC', '2022', '', 'Thomas Reynolds'),
('202201088', 'Jill Fisher', 'EVD', '2022', '', 'Robert Fisher'),
('202201089', 'Karl Ellis', 'ICT', '2022', 'A', 'Michael Ellis'),
('202201090', 'Lana Bryant', 'ICT-CS', '2022', 'B', 'David Bryant'),
('202201091', 'Mike Hanson', 'MNC', '2022', '', 'John Hanson'),
('202201092', 'Nora Webb', 'EVD', '2022', '', 'James Webb'),
('202201093', 'Omar Stevens', 'ICT', '2022', 'B', 'William Stevens'),
('202201094', 'Paula Tucker', 'ICT-CS', '2022', 'A', 'Charles Tucker'),
('202201095', 'Quinn Barnes', 'MNC', '2022', '', 'Thomas Barnes'),
('202201096', 'Rita Hayes', 'EVD', '2022', '', 'Robert Hayes'),
('202201097', 'Sam Burton', 'ICT', '2022', 'A', 'Michael Burton'),
('202201098', 'Tina Chambers', 'ICT-CS', '2022', 'B', 'David Chambers'),
('202201099', 'Uriel Dean', 'MNC', '2022', '', 'John Dean'),
('202201100', 'Violet Wells', 'EVD', '2022', '', 'James Wells');




