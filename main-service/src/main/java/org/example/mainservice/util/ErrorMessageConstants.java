package org.example.mainservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessageConstants {
    public static final String MSG_USER_NOT_FOUND = "User with id '%s' not found";
    public static final String MSG_ADMIN_NOT_FOUND = "Admin with id '%s' not found";
    public static final String MSG_RESIDENCE_NOT_FOUND = "Residence with id '%s' not found";
    public static final String MSG_RESIDENCE_NEWS_NOT_FOUND = "Residence news with id '%s' not found";
    public static final String MSG_USER_FORBIDDEN = "You do not have access to this user";
    public static final String MSG_ADMIN_FORBIDDEN = "You do not have access to this admin";
    public static final String MSG_RESIDENCE_FORBIDDEN = "You do not have access to residence with id '%s'";
    public static final String MSG_RESIDENCE_NEWS_FORBIDDEN = "You do not have access to residence news with id '%s'";
    public static final String MSG_ADMIN_EMAIL_EXISTS = "User with email '%s' already exists";
    public static final String MSG_USER_EMAIL_EXISTS = "User with email '%s' already exists";
    public static final String MSG_CREATE_USER_ERROR = "Error creating user";
    public static final String MSG_SERVICE_REQUEST_NOT_FOUND = "Service request with id '%s' not found";
    public static final String MSG_GET_EVENTS_FROM_KUDAGO_NOT_AVAILABLE = "Failed to get events nearby";
    public static final String MSG_SERVICE_REQUEST_FORBIDDEN = "You do not have access to this service request";
}
