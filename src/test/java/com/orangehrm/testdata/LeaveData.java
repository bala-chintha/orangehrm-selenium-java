package com.orangehrm.testdata;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LeaveData {

    // Format matches OrangeHRM's date input placeholder: yyyy-dd-mm (year-day-month)
    private static final DateTimeFormatter ORANGEHRM_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-dd-MM");

    public static final String LEAVE_TYPE = "CAN - Personal";
    public static final String APPLY_DATE = getFutureWorkingDate(45);
    public static final String DELETE_DATE = getFutureWorkingDate(60);

    public static String getFutureWorkingDate(int workingDays) {
        LocalDate date = LocalDate.now();
        int counted = 0;
        while (counted < workingDays) {
            date = date.plusDays(1);
            DayOfWeek day = date.getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                counted++;
            }
        }
        return date.format(ORANGEHRM_FORMAT);
    }
}
