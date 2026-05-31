package com.orangehrm.components;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;


public class Sidebar extends BasePage {

    private static final Map<String, String[]> MENU = new HashMap<>();

    static {
        MENU.put("admin", new String[]{"Admin", "viewSystemUsers"});
        MENU.put("pim", new String[]{"PIM", "viewEmployeeList"});
        MENU.put("leave", new String[]{"Leave", "viewLeaveList"});
        MENU.put("myInfo", new String[]{"My Info", "viewPersonalDetails"});
        MENU.put("dashboard", new String[]{"Dashboard", "dashboard"});
    }

    public Sidebar(WebDriver driver) {
        super(driver);
    }

    public void navigateTo(String moduleKey) {
        String[] entry = MENU.get(moduleKey.toLowerCase());
        if (entry == null) {
            throw new IllegalArgumentException("Unknown sidebar module: '" + moduleKey + "'. "
                    + "Valid keys: " + String.join(", ", MENU.keySet()));
        }

        String linkText = entry[0];
        String urlFragment = entry[1];

        By linkLocator = By.xpath("//nav//span[normalize-space()='" + linkText + "']/ancestor::a | "
                + "//aside//span[normalize-space()='" + linkText + "']/ancestor::a");

        click(linkLocator);
        waitForUrlContains(urlFragment);
    }
}
