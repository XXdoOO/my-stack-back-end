package com.xx.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FormatTime {
    public static long timestampToLong(Timestamp timestamp) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String str = dateFormat.format(timestamp);

        return new SimpleDateFormat("yyyyMMddhhmmss").parse(str).getTime();
    }
}
