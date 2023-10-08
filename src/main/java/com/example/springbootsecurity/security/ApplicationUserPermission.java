package com.example.springbootsecurity.security;

public enum ApplicationUserPermission {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write");

    private final String Permission;


    ApplicationUserPermission(String permission) {
        Permission = permission;
    }

    public String getPermission() {
        return Permission;
    }
}
