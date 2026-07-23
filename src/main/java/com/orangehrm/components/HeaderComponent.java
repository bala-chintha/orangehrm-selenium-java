package com.orangehrm.components;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderComponent extends BasePage {

    private final By loggedInUser = By.cssSelector(".oxd-userdropdown-name");

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    public String captureLoggedInUserFirstName () {

        String fullDisplayName = getText(loggedInUser);
        String firstName = fullDisplayName.trim().split("\\s+")[0];

        // Remove role if present
        return firstName;
    }
}