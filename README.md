# CareerNexus

## Student Opportunity Management Application

CareerNexus is an Android application developed using **Kotlin and Android Studio** to help students manage and keep track of important career-related opportunities such as internships, hackathons, scholarships, competitions, workshops, seminars, examinations, and jobs.

The application allows users to add opportunity details, select a deadline, save reference links and notes, and manage previously added opportunities.

---

## Aim

To develop a useful Android application named **CareerNexus** that helps students organize and manage different academic and career opportunities in one place.

---

## Tools and Technologies Required

* **Android Studio**
* **Kotlin**
* **Android SDK**
* **XML**
* **SQLite Database**
* **Android Emulator / Physical Android Device**
* **Git and GitHub**

---

## Project Objectives

### 1. Student Opportunity Management

CareerNexus allows students to store information about different opportunities instead of keeping them in separate notes or applications.

The application supports opportunity categories such as:

* Other
* Examination
* Internship
* Hackathon
* Scholarship
* Competition
* Workshop
* Seminar
* Job

These categories are provided through a Spinner in the Add Opportunity screen.

---

### 2. Add Opportunity

The application provides an **Add Opportunity** screen where users can enter important information about an opportunity.

The screen contains:

* Opportunity Title
* Opportunity Type
* Deadline Date
* Source
* Reference Link
* Notes
* Save Opportunity button

The XML layout uses **ConstraintLayout** and provides separate input fields for these details.

---

### 3. Opportunity Title

The user can enter the name/title of the opportunity.

For example:

```text
Google Summer Internship
Smart India Hackathon
Coding Competition
AWS Workshop
```

The application validates that the title is not empty before saving the opportunity.

---

### 4. Opportunity Type

A **Spinner** is used to select the type of opportunity.

Example:

```text
Internship
Hackathon
Scholarship
Competition
Workshop
Seminar
Job
```

An `ArrayAdapter` is used to display the opportunity types inside the Spinner.

---

### 5. Deadline Date Selection

CareerNexus provides a **DatePickerDialog** so that the user can select the deadline of an opportunity.

The selected date is displayed on the screen.

The application also converts the selected date into milliseconds and stores it as `deadlineMillis`.

Example:

```text
Deadline:
25/09/2026
```

---

### 6. Source Information

The user can specify where they found the opportunity.

For example:

```text
LinkedIn
College Group
Official Website
Telegram
Friend
```

The source is stored along with the opportunity details.

---

### 7. Reference Link

The user can enter the official or reference link related to the opportunity.

Example:

```text
https://example.com/internship
```

The application checks that a link has been entered before saving the opportunity.

---

### 8. Notes

Users can add additional information about an opportunity.

For example:

```text
Need to prepare resume before applying.
```

The notes field is optional. The XML layout provides a multiline input field with a minimum of two lines.

---

### 9. Save Opportunity

After entering the required information, the user can press **Save Opportunity**.

The application collects:

```text
Title
Type
Deadline
Source
Link
Notes
```

and sends these values to the database helper for insertion.

If the data is successfully saved, the application displays:

```text
Opportunity Saved!
```

---

### 10. Local Database

CareerNexus uses a `DatabaseHelper` class to store opportunity information locally.

The application calls:

```kotlin
databaseHelper.insertOpportunity(
    title = title,
    type = type,
    deadlineMillis = deadlineMillis,
    source = source,
    link = link,
    notes = notes
)
```

This allows opportunity information to remain available after the application is closed.

---

### 11. Edit Opportunity

CareerNexus also supports editing an existing opportunity.

When an opportunity ID is passed to the `AddOpportunity` activity, the application loads the corresponding opportunity and displays its existing information.

The user can then modify:

* Title
* Type
* Deadline
* Source
* Link
* Notes

---

### 12. Update Opportunity

After editing an opportunity, the application updates the existing database record using:

```kotlin
databaseHelper.updateOpportunity(
    id = editOpportunityId,
    title = title,
    type = type,
    deadlineMillis = deadlineMillis,
    source = source,
    link = link,
    notes = notes
)
```

After successful updating, the application displays:

```text
Opportunity Updated!
```

---

## Main Features

| Feature            | Description                                    |
| ------------------ | ---------------------------------------------- |
| Add Opportunity    | Add a new student opportunity                  |
| Opportunity Type   | Categorize opportunities                       |
| Deadline Picker    | Select opportunity deadline                    |
| Source             | Store where the opportunity was found          |
| Reference Link     | Store the related website/link                 |
| Notes              | Add additional information                     |
| Local Database     | Store opportunity data                         |
| Edit Opportunity   | Modify existing opportunity                    |
| Update Opportunity | Save changes to an existing record             |
| Input Validation   | Prevent saving incomplete required information |

---

## Application Flow

```text
                Start CareerNexus
                       |
                       v
                 Main Screen
                       |
                       v
              Add Opportunity
                       |
        +--------------+--------------+
        |              |              |
        v              v              v
     Enter Title   Select Type   Select Deadline
        |              |              |
        +--------------+--------------+
                       |
                       v
              Enter Source & Link
                       |
                       v
                 Add Notes
                       |
                       v
               Save Opportunity
                       |
                       v
                 DatabaseHelper
                       |
                       v
                Local Database
```

---

## Technologies Used

### Kotlin

Kotlin is used as the main programming language for implementing the application's activities and logic.

### XML

XML is used to design the Android user interface. The Add Opportunity screen uses `ConstraintLayout` for positioning UI components.

### SQLite Database

SQLite/local database storage is used through the `DatabaseHelper` class to store and update opportunity information.

### Android Components

The project uses Android components such as:

* `Activity`
* `Intent`
* `Spinner`
* `EditText`
* `TextView`
* `Button`
* `DatePickerDialog`
* `Toast`
* `Calendar`

The `AddOpportunity` activity initializes and uses these components to manage opportunity data.

---

## Input Validation

CareerNexus performs basic validation before saving an opportunity.

### Title Validation

```kotlin
if (title.isEmpty()) {
    etTitle.error = "Please enter opportunity title"
    etTitle.requestFocus()
    return@setOnClickListener
}
```

### Deadline Validation

```kotlin
if (selectedDate.isEmpty()) {
    Toast.makeText(
        this,
        "Please select a deadline date",
        Toast.LENGTH_SHORT
    ).show()

    return@setOnClickListener
}
```

### Link Validation

```kotlin
if (link.isEmpty()) {
    Toast.makeText(
        this,
        "Please enter link",
        Toast.LENGTH_SHORT
    ).show()

    return@setOnClickListener
}
```

These validations ensure that important information is entered before an opportunity is saved.

---

## Expected Learning Outcomes

After completing the CareerNexus project, the following concepts can be understood:

1. Development of an Android application using Kotlin.
2. Designing Android layouts using XML.
3. Working with `ConstraintLayout`.
4. Using `EditText`, `TextView`, `Button`, and `Spinner`.
5. Implementing `DatePickerDialog`.
6. Working with `Calendar` and date values.
7. Using `Intent` to pass data between activities.
8. Performing input validation.
9. Implementing local database operations.
10. Inserting and updating records using a database helper.
11. Managing Activity-based application flow.
12. Developing a practical student-focused Android application.

---

## Future Scope

The CareerNexus application can be further improved by adding:

* Automatic collection of opportunities from official websites and APIs.
* Search and filter functionality.
* Opportunity notifications.
* Deadline reminders.
* Bookmark/favorite opportunities.
* User-specific opportunity recommendations.
* Cloud database synchronization.
* Login and user profiles.
* Sorting opportunities by deadline.
* Opening reference links directly from the application.
* Filtering opportunities according to the student's selected field.

---

## Conclusion

**CareerNexus** is an Android-based student opportunity management application developed using Kotlin and Android Studio. It provides a structured way for students to add, categorize, store, and update information about internships, hackathons, scholarships, competitions, workshops, seminars, examinations, and jobs.

The project demonstrates important Android development concepts including XML UI design, Kotlin Activity programming, Spinner, DatePickerDialog, input validation, Intent-based navigation, and local database operations. The application can be extended in the future with automated opportunity collection, notifications, recommendations, and cloud-based features.
