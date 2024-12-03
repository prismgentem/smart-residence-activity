package org.example.smartresidenceactivity.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessageConstants {
    public static final String MSG_USER_NOT_FOUND = "User with id '%s' not found";
    public static final String MSG_ADMIN_NOT_FOUND = "Admin with id '%s' not found";
    public static final String MSG_RESIDENCE_NOT_FOUND = "Residence with id '%s' not found";
    public static final String MSG_RESIDENCE_NEWS_NOT_FOUND = "Residence news with id '%s' not found";
    public static final String MSG_USER_FORBIDDEN = "You do not have access to this user";
    public static final String MSG_ADMIN_FORBIDDEN = "You do not have access to this admin";
    public static final String MSG_RESIDENCE_FORBIDDEN = "You do not have access to this residence";
    public static final String MSG_RESIDENCE_NEWS_FORBIDDEN = "You do not have access to this residence news";

}
