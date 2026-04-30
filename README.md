# 🤖 Selenium Automation Framework

> A production-level test automation framework built with **Java**, **Selenium WebDriver**, and **TestNG** — designed for scalability, maintainability, and rich reporting.

---

## 📌 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [Features](#features)
- [Author](#author)

---

## Overview

This framework provides a robust, reusable foundation for end-to-end UI test automation. It follows industry best practices including the **Page Object Model (POM)** design pattern, centralized configuration, and automated reporting — making it easy to scale across multiple test suites and environments.

---

## 🛠️ Tech Stack

| Technology        | Purpose                          |
|-------------------|----------------------------------|
| Java              | Core programming language        |
| Selenium WebDriver| Browser automation               |
| TestNG            | Test execution & management      |
| Maven             | Build & dependency management    |
| ExtentReports     | HTML test reporting              |
| JUnit XML         | CI-compatible reporting format   |
| Git + GitHub      | Version control                  |

---

## 📁 Project Structure

```
automation-framework/
├── src/
│   ├── main/java/
│   │   └── com/automationframework/
│   │       ├── pages/          # Page Object classes
│   │       ├── utils/          # Utility/helper classes
│   │       └── config/         # Configuration readers
│   └── test/java/
│       └── com/automationframework/
│           └── tests/          # Test classes (e.g. LoginTest)
├── test-output/
│   ├── index.html              # TestNG default report
│   ├── emailable-report.html   # Emailable summary report
│   ├── junitreports/           # JUnit XML reports
│   └── testng-results.xml      # Full TestNG results
├── testng.xml                  # Test suite configuration
└── pom.xml                     # Maven dependencies
```

---

## 🚀 Getting Started

### Prerequisites

- Java JDK 8 or higher
- Maven 3.6+
- Google Chrome (or Firefox)
- ChromeDriver matching your browser version

### Installation

```bash
# Clone the repository
git clone https://github.com/Shivaninagula11/selenium-automation-framework.git

# Navigate to the project directory
cd selenium-automation-framework

# Install dependencies
mvn clean install -DskipTests
```

---

## ▶️ Running Tests

### Run all tests via Maven

```bash
mvn test
```

### Run a specific TestNG suite

```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run directly via TestNG XML

Right-click `testng.xml` in your IDE → **Run As → TestNG Suite**

---

## 📊 Test Reports

After test execution, reports are generated in the `test-output/` folder:

| Report | Location | Description |
|--------|----------|-------------|
| TestNG Report | `test-output/index.html` | Full interactive report |
| Emailable Report | `test-output/emailable-report.html` | Shareable summary |
| JUnit XML | `test-output/junitreports/` | For CI/CD pipelines |
| Extent Report | `test-output/` | Visual HTML dashboard |

Open any `.html` file in a browser to view results.

---

## ✨ Features

- ✅ **Page Object Model (POM)** — clean separation of test logic and UI interactions
- ✅ **TestNG integration** — parallel execution, grouping, and data-driven testing support
- ✅ **Rich HTML Reports** — screenshot capture on failure, pass/fail/skip tracking
- ✅ **Configurable** — easily switch browsers, URLs, and environments
- ✅ **CI/CD Ready** — JUnit XML output compatible with Jenkins, GitHub Actions, and more
- ✅ **Scalable Structure** — modular design for easy addition of new test modules

---

## 👩‍💻 Author

**Shivani Nagula**  
📧 [GitHub Profile](https://github.com/Shivaninagula11)

---

> ⭐ If you find this project useful, consider giving it a star on GitHub!