# Library Management System

A complete, production-ready Library Management System built with Spring Boot, Thymeleaf, and PostgreSQL (Supabase).

## ⚡ Quick Start

**New to this project?** See **[INSTALLATION.md](INSTALLATION.md)** for comprehensive setup instructions including:
- Multiple ways to install Maven (Chocolatey, Winget, Manual, Scoop)
- Step-by-step guide with screenshots
- IDE setup (IntelliJ, Eclipse, VS Code)
- Troubleshooting common issues

**TL;DR:**
```bash
# Install Maven (Windows with Chocolatey)
choco install maven -y

# Run the application
cd library-management
mvn spring-boot:run

# Access at http://localhost:8080 (login: admin / admin123)
```

## 🚀 Features

- **Book Management**: Add, edit, delete, and track books with ISBN, author, publisher, and copy availability
- **Student Management**: Manage student records with roll numbers, contact info, and departments
- **Issue/Return System**: Issue books to students with due dates and automatic fine calculation
- **Fine Calculation**: Automatic fine of ₹10 per day for late returns
- **Dashboard**: Real-time statistics and quick actions
- **Session-based Authentication**: Simple admin login system
- **Responsive UI**: Bootstrap-based clean and modern interface
- **Database**: PostgreSQL via Supabase with UUID primary keys

## 📋 Prerequisites

Before you begin, ensure you have:

- **Java 17** or higher installed ([Download Java](https://adoptium.net/))
- **Maven 3.6+** installed ([Download Maven](https://maven.apache.org/download.cgi))
- **Supabase Account** (free tier works) - [Sign up here](https://supabase.com/)
- **Git** (optional, for cloning)

## 🛠️ Setup Instructions

### Step 1: Set Up Supabase Database

1. **Create a Supabase Project**:
   - Go to [https://supabase.com](https://supabase.com)
   - Click "New Project"
   - Choose organization, enter project name, database password
   - Select a region close to you
   - Wait for project to be created (~2 minutes)

2. **Get Database Connection Details**:
   - In your Supabase dashboard, go to **Settings** → **Database**
   - Scroll to "Connection string" section
   - Copy the **URI** connection string (looks like: `postgresql://postgres.[PROJECT-REF]:[PASSWORD]@[HOST]:5432/postgres`)
   - Note down:
     - **Host**: `db.[PROJECT-REF].supabase.co`
     - **Database**: `postgres`
     - **Username**: `postgres.[PROJECT-REF]`
     - **Password**: Your database password

3. **Run the Database Schema**:
   - In Supabase dashboard, go to **SQL Editor**
   - Click "New Query"
   - Copy the entire content from `database/init.sql` file
   - Paste it into the SQL editor
   - Click **Run** to create tables and insert test data
   - You should see success messages

### Step 2: Configure the Application

1. **Navigate to the project directory**:
   ```bash
   cd library-management
   ```

2. **Edit `src/main/resources/application.properties`**:
   
   Replace the placeholders with your Supabase connection details:
   
   ```properties
   # Before (placeholders):
   spring.datasource.url=jdbc:postgresql://<SUPABASE_HOST>:5432/<DB_NAME>
   spring.datasource.username=<DB_USERNAME>
   spring.datasource.password=<DB_PASSWORD>
   
   # After (example with your actual values):
   spring.datasource.url=jdbc:postgresql://db.abcdefghijk.supabase.co:5432/postgres
   spring.datasource.username=postgres.abcdefghijk
   spring.datasource.password=your_actual_password
   ```

3. **Optional: Change Admin Credentials**:
   
   In the same file, you can change the default admin credentials:
   ```properties
   app.admin.username=admin
   app.admin.password=admin123
   ```

### Step 3: Build and Run the Application

1. **Build the project using Maven**:
   ```bash
   mvn clean install
   ```
   
   This will download dependencies and compile the project.

2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```
   
   Or run the JAR directly:
   ```bash
   java -jar target/library-management-1.0.0.jar
   ```

3. **Access the application**:
   - Open your browser and go to: **http://localhost:8080**
   - You'll be redirected to the login page

### Step 4: Login and Test

1. **Login with default credentials**:
   - Username: `admin`
   - Password: `admin123`

2. **Explore the features**:
   - View the dashboard with statistics
   - Browse books and students (sample data already loaded)
   - Try issuing a book to a student
   - Return a book and see fine calculation

## 📁 Project Structure

```
library-management/
├── src/
│   ├── main/
│   │   ├── java/com/example/library/
│   │   │   ├── LibraryApplication.java       # Main Spring Boot application
│   │   │   ├── config/
│   │   │   │   └── WebConfig.java            # Web MVC configuration
│   │   │   ├── controller/                   # Controllers for web pages
│   │   │   │   ├── AuthController.java       # Login/logout
│   │   │   │   ├── DashboardController.java  # Dashboard
│   │   │   │   ├── BookController.java       # Book CRUD
│   │   │   │   ├── StudentController.java    # Student CRUD
│   │   │   │   └── IssueController.java      # Issue/return operations
│   │   │   ├── entity/                       # JPA entities
│   │   │   │   ├── Book.java
│   │   │   │   ├── Student.java
│   │   │   │   ├── Staff.java
│   │   │   │   └── Issue.java
│   │   │   ├── repository/                   # Spring Data JPA repositories
│   │   │   │   ├── BookRepository.java
│   │   │   │   ├── StudentRepository.java
│   │   │   │   ├── StaffRepository.java
│   │   │   │   └── IssueRepository.java
│   │   │   └── service/
│   │   │       └── LibraryService.java       # Business logic
│   │   └── resources/
│   │       ├── templates/                    # Thymeleaf HTML templates
│   │       │   ├── login.html
│   │       │   ├── dashboard.html
│   │       │   ├── books/
│   │       │   │   ├── list.html
│   │       │   │   └── form.html
│   │       │   ├── students/
│   │       │   │   ├── list.html
│   │       │   │   └── form.html
│   │       │   └── issues/
│   │       │       ├── issue_form.html
│   │       │       └── history.html
│   │       ├── static/css/
│   │       │   └── style.css                 # Custom CSS
│   │       └── application.properties        # App configuration
├── database/
│   └── init.sql                              # Database schema and test data
├── pom.xml                                   # Maven dependencies
├── Dockerfile                                # Docker configuration
└── README.md                                 # This file
```

## 🧪 Test Cases / Acceptance Criteria

### Test Case 1: Login
- **Steps**: Navigate to http://localhost:8080, enter credentials `admin` / `admin123`, click Login
- **Expected**: Redirected to dashboard showing statistics

### Test Case 2: Add a New Book
- **Steps**: Click "Books" → "Add New Book", fill in title "Test Book", author "Test Author", total copies 3, click Save
- **Expected**: Book appears in Books list with available_copies = 3

### Test Case 3: Add a New Student
- **Steps**: Click "Students" → "Add New Student", fill in roll "TEST001", name "Test Student", click Save
- **Expected**: Student appears in Students list

### Test Case 4: Issue a Book
- **Steps**: Click "Issue Book", select a book and student, set due date (e.g., tomorrow), click Issue
- **Expected**: 
  - New record in History with status "issued"
  - Book's available_copies decreased by 1

### Test Case 5: Return a Book (No Fine)
- **Steps**: Go to "History", find an issued book, click "Return" button
- **Expected**:
  - Issue status changes to "returned"
  - Book's available_copies increased by 1
  - Fine = ₹0 (if returned before due date)

### Test Case 6: Return a Book (With Fine)
- **Steps**: Issue a book with due date in the past, then return it
- **Expected**:
  - Fine calculated as ₹10 × number of days late
  - Fine displayed in History

### Test Case 7: Prevent Issuing Unavailable Book
- **Steps**: Issue all copies of a book until available_copies = 0, try to issue it again
- **Expected**: Error message "No copies available for this book"

### Test Case 8: Prevent Deleting Book with Active Issues
- **Steps**: Issue a book, then try to delete it from Books list
- **Expected**: Error message "Cannot delete book with active issues"

### Test Case 9: Form Validation
- **Steps**: Try to add a book without entering the required "Title" field
- **Expected**: Validation error displayed, form not submitted

### Test Case 10: Logout
- **Steps**: Click "Logout" in navigation
- **Expected**: Redirected to login page with success message

## 🐳 Docker Deployment (Optional)

### Build Docker Image

```bash
docker build -t library-management:latest .
```

### Run Docker Container

```bash
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://your-supabase-host:5432/postgres \
  -e SPRING_DATASOURCE_USERNAME=your-username \
  -e SPRING_DATASOURCE_PASSWORD=your-password \
  --name library-app \
  library-management:latest
```

Access at: http://localhost:8080

## 🌐 Deployment to Production

### Deploy to Heroku

1. Install Heroku CLI
2. Login: `heroku login`
3. Create app: `heroku create my-library-app`
4. Set config vars:
   ```bash
   heroku config:set SPRING_DATASOURCE_URL=jdbc:postgresql://...
   heroku config:set SPRING_DATASOURCE_USERNAME=...
   heroku config:set SPRING_DATASOURCE_PASSWORD=...
   ```
5. Deploy: `git push heroku main`

### Deploy to AWS/Azure/GCP

Build the JAR and deploy to your preferred cloud platform with Java runtime support.

## 📊 Database Schema

### Tables
- **books**: Store book information (ISBN, title, author, copies)
- **students**: Student records (roll, name, email, department)
- **staff**: Library staff members
- **issues**: Book issue/return transactions with fines

### Relationships
- `issues.book_id` → `books.id` (CASCADE on delete)
- `issues.student_id` → `students.id` (CASCADE on delete)
- `issues.staff_id` → `staff.id` (optional)

## 🔒 Security Notes

⚠️ **Important**: The current authentication system is for **demonstration purposes only**.

For production use:
1. Replace session-based auth with **Supabase Auth** or **Spring Security with JWT**
2. Use environment variables for credentials (never commit passwords)
3. Enable HTTPS/TLS
4. Implement role-based access control (RBAC)
5. Add CSRF protection
6. Sanitize all user inputs

## 🛠️ Troubleshooting

### Issue: "Connection refused" error
- **Solution**: Check your Supabase connection details are correct in `application.properties`
- Verify your Supabase project is active and database is running

### Issue: "Table doesn't exist" error
- **Solution**: Make sure you ran the `database/init.sql` script in Supabase SQL Editor

### Issue: Port 8080 already in use
- **Solution**: Change the port in `application.properties`:
  ```properties
  server.port=8081
  ```

### Issue: Maven build fails
- **Solution**: Ensure Java 17+ is installed: `java -version`
- Clear Maven cache: `mvn clean`

## 📝 Future Enhancements

- [ ] Implement Supabase Auth for secure authentication
- [ ] Add book cover image upload using Supabase Storage
- [ ] Implement pagination and search on all list pages
- [ ] Add email notifications for due dates
- [ ] Generate reports (PDF/Excel) for issued books
- [ ] QR code generation for books
- [ ] Mobile-responsive improvements
- [ ] REST API for mobile apps
- [ ] Analytics dashboard with charts

## 📄 License

This project is open source and available for educational purposes.

## 👤 Author
**Kabilesh C**
kabileshc.dev@gmail.com
- Created as a demonstration project for Library Management System.

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

---

**Enjoy managing your library! 📚**
