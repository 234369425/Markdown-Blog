package com.beheresoft.website.dict.pojo;

/**
 * @author Aladi
 */
public class HashCode {

    public static int asInt(String s) {
        if (s == null) {
            return 0;
        }
        char[] name = s.toCharArray();
        int hash = 0;
        if (name.length > 0) {
            for (int i = 0; i < name.length; i++) {
                hash = 31 * hash + name[i];
            }
        }
        return hash;
    }


    public static long asLong(String s) {
        if (s == null) {
            return 0L;
        }
        char[] name = s.toCharArray();
        long hash = 0;
        if (name.length > 0) {
            for (int i = 0; i < name.length; i++) {
                hash = 31 * hash + name[i];
            }
        }
        return hash;
    }

}
