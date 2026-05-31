package com.orangehrm.testdata;

public class UserData {

    // ─── Dynamic data ─────────────────────────────────────────────────────────

    public static User generateUser() {
        long ts = System.currentTimeMillis();
        return new User(
                "testuser" + ts,
                "Admin@123456",
                "ESS",
                "Enabled",
                "quan"
        );
    }

    // ─── Static fixtures ──────────────────────────────────────────────────────

    public static final String KNOWN_USERNAME = "Admin";
    public static final String NONEXISTENT_USERNAME = "xyzinvaliduser000";


    public static class User {
        public final String username;
        public final String password;
        public final String role;
        public final String status;
        public final String employeeNameHint;

        public User(String username, String password, String role,
                    String status, String employeeNameHint) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.status = status;
            this.employeeNameHint = employeeNameHint;
        }
    }
}
