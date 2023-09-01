package com.svalero.gameshop_aa1_multimedia.util;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value){
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTomestamp(Date date){
        return date == null ? null : date.getTime();
    }
}
