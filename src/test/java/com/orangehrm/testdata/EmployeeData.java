package com.orangehrm.testdata;

public class EmployeeData {

    // ─── Dynamic data ─────────────────────────────────────────────────────────

    public static Employee generateEmployee() {
        long ts = System.currentTimeMillis();
        return new Employee("Test", "Auto", "User" + ts);
    }

    // ─── Static fixtures ──────────────────────────────────────────────────────

    public static final String KNOWN_FIRST_NAME = "John";
    public static final String NONEXISTENT_NAME = "zzzNoSuchEmployee000";


    public static class Employee {
        public final String firstName;
        public final String middleName;
        public final String lastName;

        public Employee(String firstName, String middleName, String lastName) {
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return firstName + " " + middleName + " " + lastName;
        }
    }
}
