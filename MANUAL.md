# User Manual — RoutinEXE

## Table of Contents
1. Introduction
2. First Run
3. User Management
4. Category Management
5. Exercise Management
6. Routine Management
7. Training Sessions
8. Dashboard
9. Calendar
10. Troubleshooting

---

## 1. Introduction

RoutinEXE is a desktop application for managing exercise routines offline. It allows creating and organizing users, exercise categories, custom routines, and recording training sessions with detailed tracking.

## 2. First Run

When opening RoutinEXE for the first time:
- The SQLite database is automatically created in the `data/` folder (inside the application directory or current working directory).
- The user management screen appears.
- No additional configuration is required.

**Requirement**: JDK 25+ installed on the system.

## 3. User Management

From the initial screen, users can be managed:

### Create User
1. Click the **Create** button (primary/blue button).
2. Fill in the **username** field (required).
3. Optionally, provide **age**, **height**, and **weight**.
4. Click **Save**.

### Edit User
1. Select a user from the table.
2. Click the **edit** (pencil icon) button on the row.
3. Modify the fields and save.

### Delete User
1. Select a user from the table.
2. Click the **delete** (trash icon) button.
3. Confirm the deletion in the dialog.

> **Note**: Selecting a user navigates to their dashboard with all features.

## 4. Category Management

Categories group exercises (e.g., Cardio, Strength, Flexibility).

### Create Category
1. In the categories view, click **Create**.
2. Enter the **name** of the category.
3. Save.

### Edit/Delete
- Use the action buttons (pencil / trash) in the table.

## 5. Exercise Management

Exercises belong to a category and can have a description and time-based flag.

### Create Exercise
1. In the exercises view, click **Create**.
2. Fill in:
   - **Name** (required)
   - **Category** (select from list)
   - **Description** (optional)
   - **Time Based** (checkbox, if the exercise is measured by duration)
3. Save.

### Edit/Delete
- Use the action buttons in the table.

## 6. Routine Management

Routines are associated with a user and contain exercises scheduled by days of the week.

### Create Routine
1. In the routines view, click **Create**.
2. Select the **user** the routine belongs to.
3. Enter the **name** of the routine.
4. Set **start and end dates** (optional).
5. Select the **days of the week** to train.
6. Assign exercises to each day with **sets** and **reps**:
   - Add exercises from the list.
   - For each assigned exercise, specify sets and reps.
7. Save.

### Edit/Delete
- Use the action buttons in the table.

## 7. Training Sessions

Sessions record training sessions with per-exercise tracking.

### View Sessions
1. Click the **Sessions** tab in the dashboard sidebar.
2. Use the **calendar** to navigate by month.
3. Click a date to open a session.

### Create Session
1. Select a date on the calendar or click **Start Session** for today's date.
2. Select the associated routine.
3. The view shows the exercises scheduled for that day.
4. For each exercise, record:
   - **Sets**
   - **Reps**
   - **Weight** (optional)
   - **Done** (checkbox to mark as completed)
5. Save.

### Mark/Unmark as Completed
- Use the **Done** checkbox on each exercise in the session to mark or unmark completion.
- The entire day can be marked as done using the **Session Day Done** switch.

> **Important**: Sessions on past dates are read-only. Only sessions for the current day can be edited.

## 8. Dashboard

Selecting a user from the main screen navigates to the dashboard:

- **Sidebar**: Navigation to Users, Categories, Exercises, Routines, and Sessions.
- **Statistics**: Cards with summarized user information.
- **Logout button**: Return to the user selection screen.

### Navigation
- Click the sidebar buttons to navigate to each section.
- Dashboard views are pre-filtered by the selected user.

## 9. Calendar

The calendar in the sessions view allows:

- **Navigation**: Use arrows to go to the previous/next month.
- **Create Session**: Click an available date cell to create a session.
- **Reset**: Selecting a routine resets the calendar to the current month.

## 10. Troubleshooting

### Application Won't Launch
- Make sure **JDK 25** or higher is installed.
- Verify that `java -version` returns the correct version.

### Database Error
- The file `data/routin-exe.db` is created automatically. Do not delete it manually.
- If corruption occurs, back up and delete the file to create a new one.

### AppImage Won't Execute
- Ensure the file has execute permissions:
  ```bash
  chmod +x RoutinEXE-x86_64.AppImage
  ```
- Verify your system is x86_64 (64-bit).

### Dashboard Shows No Data
- Make sure you have created at least one user and one routine.
- The dashboard only shows data associated with the selected user.

### Console Warnings
- Warnings about "restricted method" and "native access" are normal on Java 25 and do not affect functionality.
- "xapp-gtk3-module" is an optional GTK module, not essential.

---

**RoutinEXE v1.0.0**
