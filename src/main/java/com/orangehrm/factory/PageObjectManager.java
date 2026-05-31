package com.orangehrm.factory;

import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.pim.PimPage;
import com.orangehrm.pages.pim.AddEmployeePage;
import com.orangehrm.pages.admin.AdminPage;
import com.orangehrm.pages.admin.AddUserPage;
import com.orangehrm.pages.leave.LeavePage;
import com.orangehrm.pages.leave.ApplyLeavePage;
import org.openqa.selenium.WebDriver;

public class PageObjectManager {

    private final WebDriver driver;

    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private PimPage pimPage;
    private AddEmployeePage addEmployeePage;
    private AdminPage adminPage;
    private AddUserPage addUserPage;
    private LeavePage leavePage;
    private ApplyLeavePage applyLeavePage;

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage getLoginPage() {
        if (loginPage == null) loginPage = new LoginPage(driver);
        return loginPage;
    }

    public DashboardPage getDashboardPage() {
        if (dashboardPage == null) dashboardPage = new DashboardPage(driver);
        return dashboardPage;
    }

    public PimPage getPimPage() {
        if (pimPage == null) pimPage = new PimPage(driver);
        return pimPage;
    }

    public AddEmployeePage getAddEmployeePage() {
        if (addEmployeePage == null) addEmployeePage = new AddEmployeePage(driver);
        return addEmployeePage;
    }

    public AdminPage getAdminPage() {
        if (adminPage == null) adminPage = new AdminPage(driver);
        return adminPage;
    }

    public AddUserPage getAddUserPage() {
        if (addUserPage == null) addUserPage = new AddUserPage(driver);
        return addUserPage;
    }

    public LeavePage getLeavePage() {
        if (leavePage == null) leavePage = new LeavePage(driver);
        return leavePage;
    }

    public ApplyLeavePage getApplyLeavePage() {
        if (applyLeavePage == null) applyLeavePage = new ApplyLeavePage(driver);
        return applyLeavePage;
    }
}
