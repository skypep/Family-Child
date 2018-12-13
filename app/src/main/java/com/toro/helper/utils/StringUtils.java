package com.toro.helper.utils;

import android.content.Context;

import com.toro.helper.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Create By liujia
 * on 2018/10/22.
 **/
public class StringUtils {

    public static boolean isEmpty(String string) {
        if(string == null || string.length() < 1 || string.equals("null") || string.equals("NULL")) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static String md5(String string) {
        if (isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getChiniseNumber(Context context,int number) {
        switch (number) {
            case 0:
                return context.getString(R.string.number_0);
            case 1:
                return context.getString(R.string.number_1);
            case 2:
                return context.getString(R.string.number_2);
            case 3:
                return context.getString(R.string.number_3);
            case 4:
                return context.getString(R.string.number_4);
            case 5:
                return context.getString(R.string.number_5);
            case 6:
                return context.getString(R.string.number_6);
            case 7:
                return context.getString(R.string.number_7);
            case 8:
                return context.getString(R.string.number_8);
            case 9:
                return context.getString(R.string.number_9);
        }
        return "";
    }
}
