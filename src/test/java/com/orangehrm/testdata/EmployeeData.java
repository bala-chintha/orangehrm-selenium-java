package com.orangehrm.testdata;

import java.util.concurrent.ThreadLocalRandom;

public class EmployeeData {

    // ─── Dynamic data ─────────────────────────────────────────────────────────

    public static Employee generateEmployee() {
        long ts = System.currentTimeMillis();

        return new Employee("Test", "Auto", "User" + ts, generateRandomEmployeeId());
    }

    public static String generateRandomEmployeeId() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 10000);
        return String.format("%04d", randomNum);
    }
    // ─── Static fixtures ──────────────────────────────────────────────────────

    public static final String KNOWN_FIRST_NAME = "John";
    public static final String NONEXISTENT_NAME = "zzzNoSuchEmployee000";


    public static class Employee {
        public final String firstName;
        public final String middleName;
        public final String lastName;
        public final String employeeId;

        public Employee(String firstName, String middleName, String lastName, String employeeId) {
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
            this.employeeId = employeeId;
        }

        @Override
        public String toString() {
            return firstName + " " + middleName + " " + lastName;
        }
    }
}
