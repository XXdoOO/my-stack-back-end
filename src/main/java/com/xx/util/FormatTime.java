package com.xx.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FormatTime {
    public static long timestampToLong(Timestamp timestamp) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String str = dateFormat.format(timestamp);

        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(str).getTime();
    }

    public static Timestamp longToTimestamp(long timestamp) {
        return new Timestamp(timestamp);
    }
}
