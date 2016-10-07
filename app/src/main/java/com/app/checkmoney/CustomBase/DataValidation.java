package com.app.checkmoney.CustomBase;

import java.util.regex.Pattern;

/**
 * Created by Mhwan on 2016. 9. 9..
 */
public class DataValidation {
    public static boolean isValidLength(String text, int minLength, int maxLength){
        if (text != null && text.length()>= minLength && text.length() <= maxLength)
            return true;
        return false;
    }
    public static boolean isEmptyString(String text){
        if (text == null || text.isEmpty() || text.length()== 0)
            return true;
        return false;
    }

    public static boolean isValidValue(String value, Type type){
        if (type.equals(Type.EMAIL))
            return Pattern.matches(EMAIL_REGEX, value);

        else if (type.equals(Type.PHONE_NUMBER))
            return Pattern.matches(PHONE_NUMBER_REGEX, value);

        return false;
    }
    public enum Type { EMAIL, PHONE_NUMBER }
    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final String PHONE_NUMBER_REGEX = "01(?:0|1|[6-9])(?:\\d{3}|\\d{4})(\\d{4})";

}
