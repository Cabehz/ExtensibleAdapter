package com.cf.lib.extensible;

/**
 *
 * Created by enniu on 16/8/1.
 */
public class Utils {
    public static int getTagInteger(Object tag) {
        int val = 0;

        if(tag != null && tag instanceof Integer) {
            val = (int) tag;
        }

        return val;
    }
}
