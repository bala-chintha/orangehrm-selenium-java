package com.orangehrm.listeners;

import com.orangehrm.base.BaseTest;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY = Integer.parseInt(System.getProperty("retry.count", "2"));
    private static final long BASE_BACKOFF_MS = 3000;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            long backoff = BASE_BACKOFF_MS * (1L << (retryCount - 1));
            System.out.println("[RETRY] Retrying test: " + result.getName() + " — attempt " + retryCount + " of " + MAX_RETRY + " (waiting " + backoff + "ms before retry)");
            try {
                Thread.sleep(backoff);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            BaseTest.isRetryAttempt.set(true);
            return true;
        }
        return false;
    }
}