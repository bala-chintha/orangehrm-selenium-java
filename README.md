# OrangeHRM Selenium Java TestNG Framework


A Production-quality UI automation framework for [OrangeHRM Demo](https://opensource-demo.orangehrmlive.com)
built with **Selenium 4 + Java 21 + TestNG**.

---

## Framework Architecture

```
orangehrm-selenium-java/
│
├── .github/workflows/ci.yml         ← GitHub Actions CI (headless Chrome)
├── pom.xml                          ← Maven dependencies & build config
│
├── src/main/java/com/orangehrm/
│   ├── base/
│   │   └── BasePage.java            ← All reusable WebDriver helpers
│   ├── components/
│   │   └── Sidebar.java             ← Left-hand navigation component
│   ├── pages/
│   │   ├── LoginPage.java
│   │   ├── DashboardPage.java
│   │   ├── pim/
│   │   │   ├── PimPage.java         ← Employee list
│   │   │   └── AddEmployeePage.java
│   │   ├── admin/
│   │   │   ├── AdminPage.java      ← System Users list
│   │   │   └── AddUserPage.java
│   │   └── leave/
│   │       ├── LeavePage.java      ← My Leave List
│   │       └── ApplyLeavePage.java
│   ├── factory/
│   │   └── PageObjectManager.java   ← Lazy POM factory
│   └── utils/
│       ├── ConfigReader.java        ← config.properties + -D override support
│       └── PageLoadHelper.java      ← Multi-layer Vue.js page-load strategy
│
├── src/test/java/com/orangehrm/
│   ├── base/
│   │   └── BaseTest.java            ← ThreadLocal driver, loginAsAdmin(), teardown
│   ├── listeners/
│   │   ├── ExtentReportManager.java ← Singleton report instance
│   │   └── TestListener.java        ← Pass/Fail/Screenshot → ExtentReports
│   ├── testdata/
│   │   ├── EmployeeData.java
│   │   ├── UserData.java
│   │   └── LeaveData.java
│   └── tests/
│       ├── LoginTest.java
│       ├── DashboardTest.java
│       ├── PimTest.java
│       ├── AdminTest.java
│       └── LeaveTest.java
│
└── src/test/resources/
    ├── config.properties            ← Environment config (URL, browser, timeouts)
    ├── testng.xml                   ← Full test suite
    └── testng-smoke.xml             ← Smoke-only suite
```

---

## Prerequisites

| Tool | Minimum Version | Check Command |
|------|-----------------|---------------|
| Java JDK | 21+ | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| Google Chrome | Latest | WebDriverManager auto-downloads the matching driver |

> **No manual ChromeDriver download needed** — `WebDriverManager` handles it automatically.

---

## Quick Start

### 1. Clone

```bash
git clone https://github.com/bala-chintha/orangehrm-selenium-java.git
cd orangehrm-selenium-java
```

### 2. Run full suite

```bash
mvn test
```

### 3. Run smoke tests only

```bash
mvn test -Psmoke
```

### 4. Run headless (CI mode)

```bash
mvn test -Pci
# or
mvn test -Dheadless=true
```

### 5. Run a specific test class

```bash
mvn test -Dtest=LoginTest
mvn test -Dtest=PimTest
```

### 6. Run a specific group

```bash
mvn test -Dgroups=smoke
mvn test -Dgroups=pim,admin
```

### 7. Switch browser

```bash
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

---

## Test Coverage

| Module | Test Class | Test Cases |
|--------|-----------|------------|
| Login | `LoginTest` | Valid login, invalid credentials, empty submit |
| Dashboard | `DashboardTest` | Heading visible, username displayed, sidebar navigation ×3, logout |
| PIM | `PimTest` | Load list, navigate to add, add, edit, delete, search, no-results, reset |
| Admin | `AdminTest` | Load list, navigate to add, add user, delete user, search, no-results, reset |
| Leave | `LeaveTest` | Navigate to apply, load list, apply leave, cancel leave |

**Total: ~28 automated UI test cases**

---

## Reports

After a run, open the HTML report:

```
reports/ExtentReport.html
```

Failure screenshots are saved to:

```
reports/screenshots/<testName>_<timestamp>.png
```

---

## Configuration

All settings are in `src/test/resources/config.properties`:

```properties
base.url=https://opensource-demo.orangehrmlive.com
admin.username=Admin
admin.password=admin123
browser=chrome
headless=false
explicit.wait=45
page.load.timeout=90
screenshot.dir=reports/screenshots
extent.report.path=reports/ExtentReport.html
```
Override any property on the command line: `-Dbrowser=firefox -Dheadless=true`

---

## Design Decisions

| Decision | Rationale |
|----------|-----------|
| **ThreadLocal WebDriver** | Safe for parallel execution — each thread has its own browser instance |
| **No implicit wait** | Mixing implicit + explicit waits causes unreliable, compounding timeouts |
| **Multi-layer page-load** | OrangeHRM is a Vue SPA; `PageLoadHelper` waits for DOM → Vue → spinner → element |
| **PageObjectManager (lazy)** | Pages are instantiated on first use — keeps memory clean |
| **@BeforeMethod driver setup** | Fresh browser per test — zero state bleed between tests |
| **WebDriverManager** | Zero-config driver management |
| **ExtentReports listener** | Fully decoupled from test code; screenshots auto-embed on failure |

---

## Common Issues

| Problem | Fix |
|---------|-----|
| `NoSuchElementException` on dropdowns | OrangeHRM uses Vue custom selects; `selectFromDropdown()` in BasePage handles them |
| Date input not accepted | Format is `yyyy-DD-MM`. `LeaveData.getFutureWorkingDate()` returns correct format |
| Tests fail on slow network | Increase `explicit.wait` and `page.load.timeout` in `config.properties` |
| Tests fail in CI headless | Use `-Pci` profile; `--headless=new` flag is set automatically |
| WebDriverManager download fails | Set `wdm.proxyHost` / `wdm.proxyPort` JVM args for corporate proxies |
