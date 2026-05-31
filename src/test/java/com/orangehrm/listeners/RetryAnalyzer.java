package com.orangehrm.listeners;

import com.orangehrm.utils.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY = Integer.parseInt(
            System.getProperty("retry.count", "2"));

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            System.out.println("[RETRY] Retrying test: "
                    + result.getName() + " — attempt " + retryCount + " of " + MAX_RETRY);
            return true;
        }
        return false;
    }
}