package me.kafein.elitegenerator.generator.feature.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarSerializer {

    final private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static long serialize(final long var) {
        final Date date = new Date();
        return (date.getTime() + (var * 1000));
    }

    public static long deserialize(final long var) {
        Date date = new Date(var);
        final Date nowDate = new Date();
        return (date.getTime() - nowDate.getTime()) / 1000;
    }

    public static String nowDate() {
        return dateFormat.format(new Date());
    }

}
