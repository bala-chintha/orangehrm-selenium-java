package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DashboardTest extends BaseTest {

    private DashboardPage dashboardPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        loginAsAdmin();
        dashboardPage = pom().getDashboardPage();
        dashboardPage.goTo();
    }

    // ─── Tests ────────────────────────────────────────────────────────────────

    @Test(groups = {"smoke", "dashboard"}, description = "Dashboard heading should be visible after login")
    public void shouldDisplayDashboardHeading() {
        Assert.assertTrue(dashboardPage.isDashboardHeadingVisible(),
                "Dashboard heading (h6) should be visible");
    }

    @Test(groups = {"smoke", "dashboard"}, description = "Logged-in username should be displayed in the top bar")
    public void shouldDisplayLoggedInUsername() {
        String username = dashboardPage.getUserName();
        Assert.assertNotNull(username, "Username should not be null");
        Assert.assertTrue(username.length() > 0, "Username should not be empty");
    }

    @Test(groups = {"smoke", "dashboard"}, description = "Clicking PIM in the sidebar should navigate to the employee list")
    public void shouldNavigateToPimViaSidebar() {
        dashboardPage.sidebar.navigateTo("pim");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("viewEmployeeList"),
                "URL should contain 'viewEmployeeList' after clicking PIM sidebar link");
    }

    @Test(groups = {"smoke", "dashboard"}, description = "Clicking Leave in the sidebar should navigate to the leave list")
    public void shouldNavigateToLeaveViaSidebar() {
        dashboardPage.sidebar.navigateTo("leave");
        Assert.assertTrue(
                getDriver().getCurrentUrl().contains("viewLeaveList"),
                "URL should contain 'viewLeaveList' after clicking Leave sidebar link");
    }

    @Test(groups = {"smoke", "dashboard"}, description = "Clicking Admin in the sidebar should navigate to system users")
    public void shouldNavigateToAdminViaSidebar() {
        dashboardPage.sidebar.navigateTo("admin");
        Assert.assertTrue(
                getDriver().getCurrentUrl().contains("viewSystemUsers"),
                "URL should contain 'viewSystemUsers' after clicking Admin sidebar link");
    }

    @Test(groups = {"smoke", "dashboard"}, description = "Logout should redirect back to the login page")
    public void shouldLogoutSuccessfully() {
        dashboardPage.logout();
        Assert.assertTrue(getDriver().getCurrentUrl().contains("auth/login"),
                "URL should contain 'auth/login' after logout");
    }
}
