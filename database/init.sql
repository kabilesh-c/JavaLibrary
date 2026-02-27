-- ============================================
-- Library Management System - Database Schema
-- ============================================
-- Run this SQL script in your Supabase SQL Editor
-- This will create all necessary tables with UUIDs and constraints

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ============================================
-- TABLE: students
-- ============================================
CREATE TABLE IF NOT EXISTS students (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    roll VARCHAR(50) NOT NULL UNIQUE,
    name TEXT NOT NULL,
    email TEXT UNIQUE,
    phone TEXT,
    department TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================
-- TABLE: books
-- ============================================
CREATE TABLE IF NOT EXISTS books (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    isbn VARCHAR(50) UNIQUE,
    title TEXT NOT NULL,
    author TEXT,
    publisher TEXT,
    total_copies INT DEFAULT 1 NOT NULL,
    available_copies INT DEFAULT 1 NOT NULL,
    cover_path TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    CONSTRAINT books_copies_check CHECK (available_copies >= 0 AND available_copies <= total_copies)
);

-- ============================================
-- TABLE: staff
-- ============================================
CREATE TABLE IF NOT EXISTS staff (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    email TEXT UNIQUE,
    role TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================
-- TABLE: issues
-- ============================================
CREATE TABLE IF NOT EXISTS issues (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    book_id UUID NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    student_id UUID NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    staff_id UUID REFERENCES staff(id),
    issue_date DATE DEFAULT CURRENT_DATE,
    due_date DATE,
    return_date DATE,
    fine NUMERIC(10, 2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'issued' CHECK (status IN ('issued', 'returned', 'overdue'))
);

-- ============================================
-- INDEXES for better performance
-- ============================================
CREATE INDEX IF NOT EXISTS idx_books_isbn ON books(isbn);
CREATE INDEX IF NOT EXISTS idx_students_roll ON students(roll);
CREATE INDEX IF NOT EXISTS idx_students_email ON students(email);
CREATE INDEX IF NOT EXISTS idx_issues_status ON issues(status);
CREATE INDEX IF NOT EXISTS idx_issues_book_id ON issues(book_id);
CREATE INDEX IF NOT EXISTS idx_issues_student_id ON issues(student_id);
CREATE INDEX IF NOT EXISTS idx_issues_issue_date ON issues(issue_date);

-- ============================================
-- TEST DATA: Sample Books
-- ============================================
INSERT INTO books (isbn, title, author, publisher, total_copies, available_copies) VALUES
    ('978-0134685991', 'Effective Java', 'Joshua Bloch', 'Addison-Wesley', 5, 5),
    ('978-0596009205', 'Head First Design Patterns', 'Eric Freeman', 'O''Reilly Media', 3, 3),
    ('978-0132350884', 'Clean Code', 'Robert C. Martin', 'Prentice Hall', 4, 4),
    ('978-0201633610', 'Design Patterns', 'Erich Gamma', 'Addison-Wesley', 2, 2),
    ('978-0134494166', 'Clean Architecture', 'Robert C. Martin', 'Prentice Hall', 3, 3),
    ('978-1617294945', 'Spring in Action', 'Craig Walls', 'Manning Publications', 4, 4),
    ('978-1449355739', 'Learning SQL', 'Alan Beaulieu', 'O''Reilly Media', 3, 3),
    ('978-0137081073', 'The Clean Coder', 'Robert C. Martin', 'Prentice Hall', 2, 2)
ON CONFLICT (isbn) DO NOTHING;

-- ============================================
-- TEST DATA: Sample Students
-- ============================================
INSERT INTO students (roll, name, email, phone, department) VALUES
    ('CS2021001', 'Rahul Sharma', 'rahul.sharma@example.com', '+91-9876543210', 'Computer Science'),
    ('CS2021002', 'Priya Patel', 'priya.patel@example.com', '+91-9876543211', 'Computer Science'),
    ('EE2021003', 'Amit Kumar', 'amit.kumar@example.com', '+91-9876543212', 'Electrical Engineering'),
    ('ME2021004', 'Sneha Reddy', 'sneha.reddy@example.com', '+91-9876543213', 'Mechanical Engineering'),
    ('CS2021005', 'Vikram Singh', 'vikram.singh@example.com', '+91-9876543214', 'Computer Science'),
    ('IT2021006', 'Ananya Gupta', 'ananya.gupta@example.com', '+91-9876543215', 'Information Technology')
ON CONFLICT (roll) DO NOTHING;

-- ============================================
-- TEST DATA: Sample Staff
-- ============================================
INSERT INTO staff (name, email, role) VALUES
    ('Dr. Rajesh Verma', 'rajesh.verma@library.edu', 'Head Librarian'),
    ('Ms. Pooja Mehta', 'pooja.mehta@library.edu', 'Assistant Librarian'),
    ('Mr. Suresh Nair', 'suresh.nair@library.edu', 'Library Assistant')
ON CONFLICT (email) DO NOTHING;

-- ============================================
-- VERIFICATION QUERIES
-- ============================================
-- Uncomment these to verify the data after insertion:

-- SELECT COUNT(*) AS total_books FROM books;
-- SELECT COUNT(*) AS total_students FROM students;
-- SELECT COUNT(*) AS total_staff FROM staff;
-- SELECT * FROM books ORDER BY title;
-- SELECT * FROM students ORDER BY roll;
