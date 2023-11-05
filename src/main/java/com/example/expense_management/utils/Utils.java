package com.example.expense_management.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    // Hàm chuyển đổi chuỗi ngày thành timestamp
    public static Date convertToTimestamp(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng của chuỗi ngày
            if(dateStr.isEmpty())
                return Date.from(new Timestamp(System.currentTimeMillis()).toInstant());
            return sdf.parse(dateStr); // Chuyển đổi thành timestamp
        } catch (ParseException e) {
            e.printStackTrace();
            // Xử lý nếu có lỗi chuyển đổi
        }
        return null;
    }
}
