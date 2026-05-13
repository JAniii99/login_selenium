# 🧪 Selenium OrangeHRM Login Automation Demo

> **Portfolio project** showcasing Selenium WebDriver + TestNG automation on a live demo site.

[![Selenium Tests](https://github.com/YOUR_USERNAME/selenium-login-demo/actions/workflows/selenium-tests.yml/badge.svg)](https://github.com/YOUR_USERNAME/selenium-login-demo/actions/workflows/selenium-tests.yml)

---

## 🎯 What This Project Tests

| Test Case | Scenario | Expected Result |
|-----------|----------|-----------------|
| TC_001 | Login with **valid** credentials (`Admin` / `admin123`) | ✅ Redirects to Dashboard |
| TC_002 | Login with **invalid** password | ✅ Shows "Invalid credentials" alert |
| TC_003 | Login with **empty** fields | ✅ Shows "Required" validation messages |

**Live site under test:** https://opensource-demo.orangehrmlive.com/web/index.php/auth/login

---

## 🛠️ Tech Stack

- **Java 11**
- **Selenium WebDriver 4.18**
- **TestNG 7.9** — test runner & assertions
- **WebDriverManager** — auto ChromeDriver setup
- **Maven** — build & dependency management
- **GitHub Actions** — CI/CD & live demo runner

---

## 📁 Project Structure

```
selenium-login-demo/
├── src/
│   └── test/
│       ├── java/
│       │   └── tests/
│       │       └── LoginTest.java        ← All test cases
│       └── resources/
│           └── testng.xml               ← TestNG suite config
├── .github/
│   └── workflows/
│       └── selenium-tests.yml           ← GitHub Actions CI
├── pom.xml                              ← Maven dependencies
└── README.md
```

---

## ▶️ Run Locally

### Prerequisites
- Java 11+
- Maven 3.6+
- Google Chrome installed

### Steps

```bash
# 1. Clone the repo
git clone https://github.com/YOUR_USERNAME/selenium-login-demo.git
cd selenium-login-demo

# 2. Run all tests
mvn test

# 3. View report
open target/surefire-reports/index.html
```

---

## 🔴 Live Demo (GitHub Actions)

Click the badge above or go to:
```
https://github.com/YOUR_USERNAME/selenium-login-demo/actions
```
→ Click **"Run workflow"** → See tests execute live in CI!

---

## 👤 Author

**Your Name** — [Portfolio](https://yourportfolio.com) | [LinkedIn](https://linkedin.com/in/yourprofile)
