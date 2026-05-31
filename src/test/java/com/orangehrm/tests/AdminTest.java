package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.admin.AddUserPage;
import com.orangehrm.pages.admin.AdminPage;
import com.orangehrm.testdata.UserData;
import com.orangehrm.testdata.UserData.User;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AdminTest extends BaseTest {

    private AdminPage adminPage;
    private AddUserPage addUserPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        loginAsAdmin();
        adminPage = pom().getAdminPage();
        addUserPage = pom().getAddUserPage();
        adminPage.goTo();
    }

    private User createUser() {
        User user = UserData.generateUser();
        adminPage.clickAdd();
        addUserPage.selectUserRole(user.role);
        addUserPage.selectStatus(user.status);
        addUserPage.fillEmployeeName(user.employeeNameHint);
        addUserPage.fillUsername(user.username);
        addUserPage.fillPassword(user.password);
        addUserPage.save();
        return user;
    }

    // ─── READ ─────────────────────────────────────────────────────────────────

    @Test(groups = {"smoke", "admin"}, description = "User list should load with at least one record")
    public void shouldLoadUserListWithRecords() {
        int count = adminPage.getRowCount();
        Assert.assertTrue(count > 0, "User list should have at least one record, but found: " + count);
    }

    @Test(groups = {"smoke", "admin"}, description = "Clicking Add should navigate to the Add User form")
    public void shouldNavigateToAddUserPage() {
        adminPage.clickAdd();
        Assert.assertTrue(addUserPage.isLoaded(), "Add User form should be loaded and visible");
    }

    // ─── CREATE ───────────────────────────────────────────────────────────────

    @Test(groups = {"smoke", "admin"}, description = "Should create a new user successfully and show success toast")
    public void shouldAddNewUserSuccessfully() {
        createUser();
        String message = addUserPage.getSuccessMessage();
        Assert.assertTrue(message.contains("Successfully Saved"),
                "Success toast should contain 'Successfully Saved', actual: " + message);
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    @Test(groups = {"admin"}, description = "Should delete a user and confirm they no longer appear in search")
    public void shouldDeleteUserSuccessfully() {
        User user = createUser();

        adminPage.goTo();
        adminPage.searchByUsername(user.username);
        adminPage.clickDeleteOnRow(0);
        adminPage.confirmDelete();

        adminPage.searchByUsername(user.username);
        Assert.assertTrue(adminPage.isNoRecordsFound(),
                "Deleted user '" + user.username + "' should not appear in search results");
    }

    // ─── SEARCH ───────────────────────────────────────────────────────────────

    @Test(groups = {"smoke", "admin"}, description = "Searching by a known username should return results")
    public void shouldSearchByUsernameAndFindResults() {
        adminPage.searchByUsername(UserData.KNOWN_USERNAME);
        int count = adminPage.getRowCount();
        Assert.assertTrue(count > 0,
                "Search for '" + UserData.KNOWN_USERNAME + "' should return results");
    }

    @Test(groups = {"smoke", "admin"}, description = "Searching for a non-existent username should show 'No Records Found'")
    public void shouldShowNoRecordsForInvalidUsernameSearch() {
        adminPage.searchByUsername(UserData.NONEXISTENT_USERNAME);
        Assert.assertTrue(adminPage.isNoRecordsFound(),
                "Searching for '" + UserData.NONEXISTENT_USERNAME
                        + "' should show 'No Records Found'");
    }

    @Test(groups = {"smoke", "admin"}, description = "Resetting the search should restore the full user list")
    public void shouldResetSearchAndShowAllUsers() {
        adminPage.searchByUsername(UserData.NONEXISTENT_USERNAME);
        adminPage.resetSearch();
        int count = adminPage.getRowCount();
        Assert.assertTrue(count > 0, "After reset, user list should be restored with records");
    }
}
