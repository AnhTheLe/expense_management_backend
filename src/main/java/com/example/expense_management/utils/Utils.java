package com.example.expense_management.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    // Hàm chuyển đổi chuỗi ngày thành timestamp
    public static Date convertToTimestamp(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng của chuỗi ngày
            if(dateStr.isEmpty())
                return Date.from(new Timestamp(System.currentTimeMillis()).toInstant());
            Date date = sdf.parse(dateStr);

            // Sử dụng Calendar để thiết lập thời gian
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 17); // Thiết lập giờ là 17h

            // In ra đối tượng Date đã chuyển đổi với thời gian là 17h
            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            // Xử lý nếu có lỗi chuyển đổi
        }
        return null;
    }
}
