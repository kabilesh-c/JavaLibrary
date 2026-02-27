# Quick Start Guide - Library Management System

Get up and running in 5 minutes! ⚡

## 🎯 Prerequisites Check

```bash
# Check Java version (must be 17+)
java -version

# Check Maven version
mvn -version
```

If not installed:
- **Java 17**: Download from https://adoptium.net/
- **Maven**: Download from https://maven.apache.org/download.cgi

## 🚀 3-Step Setup

### Step 1: Setup Supabase (2 minutes)

1. Go to https://supabase.com and sign up/login
2. Click **"New Project"**
3. Enter:
   - Project name: `library-management`
   - Database password: (choose a strong password)
   - Region: (closest to you)
4. Wait for project creation (~2 minutes)

### Step 2: Create Database Tables (1 minute)

1. In Supabase Dashboard, click **"SQL Editor"** in left menu
2. Click **"New Query"**
3. Open the file `database/init.sql` from this project
4. Copy ALL content and paste into Supabase SQL editor
5. Click **"Run"** button
6. You should see: "Success. No rows returned" ✅

### Step 3: Configure & Run (2 minutes)

1. **Get Supabase connection details**:
   - In Supabase: Go to **Settings** → **Database**
   - Find "Connection string" section
   - Copy the **URI** format
   - Extract: Host, Database name, Username, Password

2. **Update configuration**:
   - Open: `src/main/resources/application.properties`
   - Replace these lines:
   
   ```properties
   spring.datasource.url=jdbc:postgresql://db.YOUR_PROJECT_REF.supabase.co:5432/postgres
   spring.datasource.username=postgres.YOUR_PROJECT_REF
   spring.datasource.password=YOUR_DATABASE_PASSWORD
   ```

3. **Run the application**:
   
   ```bash
   mvn spring-boot:run
   ```
   
   Wait for: `"Started LibraryApplication in X seconds"`

4. **Open your browser**:
   - Navigate to: http://localhost:8080
   - Login with:
     - Username: `admin`
     - Password: `admin123`

## 🎉 You're Done!

You should now see the dashboard with sample data:
- 8 sample books
- 6 sample students
- Ready to issue/return books

## 📋 Quick Test

1. Click **"Issue Book"** in navigation
2. Select any book and student
3. Set due date (use the default)
4. Click **"Issue Book"**
5. Go to **"History"** to see the issued book
6. Click **"Return"** button to return it

## ❓ Troubleshooting

**Can't connect to database?**
- Double-check your connection string in `application.properties`
- Make sure Supabase project is active (green status)

**Tables don't exist?**
- Re-run the SQL script from `database/init.sql` in Supabase

**Port 8080 in use?**
- Add to `application.properties`: `server.port=8081`
- Access at: http://localhost:8081

## 📚 Next Steps

- Read the full **README.md** for detailed documentation
- Check **Test Cases** section in README for acceptance testing
- Explore all features: Books, Students, Issue/Return, History

---

**Need help?** Check the README.md for detailed troubleshooting.
