package com.orangehrm.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.orangehrm.utils.ConfigReader;

import java.io.File;

public class ExtentReportManager {

    private static ExtentReports extent;

    private ExtentReportManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = ConfigReader.getInstance().getExtentReportPath();
            // Ensure parent directory exists
            new File(reportPath).getParentFile().mkdirs();

            ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
            reporter.config().setTheme(Theme.STANDARD);
            reporter.config().setDocumentTitle("OrangeHRM Test Report");
            reporter.config().setReportName("Selenium Java TestNG — OrangeHRM");
            reporter.config().setEncoding("utf-8");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Application", "OrangeHRM Demo");
            extent.setSystemInfo("Framework", "Selenium Java + TestNG");
            extent.setSystemInfo("Browser",
                    ConfigReader.getInstance().getBrowser());
            extent.setSystemInfo("Environment",
                    ConfigReader.getInstance().getBaseUrl());
        }
        return extent;
    }
}
