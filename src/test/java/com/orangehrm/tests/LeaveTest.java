package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.leave.ApplyLeavePage;
import com.orangehrm.pages.leave.LeavePage;
import com.orangehrm.testdata.LeaveData;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class LeaveTest extends BaseTest {

    private LeavePage leavePage;
    private ApplyLeavePage applyLeavePage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        loginAsAdmin();
        leavePage = pom().getLeavePage();
        applyLeavePage = pom().getApplyLeavePage();
        leavePage.cancelAllLeaves();
    }


    private void applyLeave(String date) {
        leavePage.goTo();
        leavePage.clickApply();
        applyLeavePage.selectLeaveType(LeaveData.LEAVE_TYPE);
        applyLeavePage.setFromDate(date);
        applyLeavePage.setToDate(date);
        applyLeavePage.apply();
    }


    @Test(groups = {"smoke", "leave"}, description = "Clicking Apply should navigate to the Apply Leave form")
    public void shouldNavigateToApplyLeavePage() {
        leavePage.goTo();
        leavePage.clickApply();
        Assert.assertTrue(applyLeavePage.isLoaded(), "Apply Leave form should be loaded after clicking Apply");
    }

    @Test(groups = {"smoke", "leave"}, description = "Leave list page should load without errors (may have 0 records)")
    public void shouldLoadLeaveListPage() {
        leavePage.goTo();
        int count = leavePage.getRowCount();
        Assert.assertTrue(count >= 0,
                "Leave list should load without errors; row count was: " + count);
    }

    @Test(groups = {"smoke", "leave"}, description = "Applying leave should show a success toast")
    public void shouldApplyLeaveSuccessfully() {
        applyLeave(LeaveData.APPLY_DATE);
        String message = applyLeavePage.getSuccessMessage();
        Assert.assertTrue(message.contains("Successfully Saved"),
                "Success toast should contain 'Successfully Saved', actual: " + message);
    }

    @Test(groups = {"smoke", "leave"}, description = "Cancelling a leave request should update its status to 'Cancelled'")
    public void shouldCancelLeaveRequestSuccessfully() {
        applyLeave(LeaveData.DELETE_DATE);

        leavePage.goTo();
        leavePage.cancelFirstLeave();

        // After cancellation the row status should read "Cancelled"
        String status = leavePage.getLeaveStatusOnRow(0);
        Assert.assertTrue(status.contains("Cancelled"),
                "Leave status should be 'Cancelled', actual: " + status);
    }
}
