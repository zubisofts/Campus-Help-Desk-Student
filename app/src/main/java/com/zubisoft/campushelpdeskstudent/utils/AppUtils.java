package com.zubisoft.campushelpdeskstudent.utils;

import android.graphics.Color;

public class AppUtils {

    public static int getStatusColor(String status){
        if(status.equals("pending")){
            return Color.parseColor("#ffa000");
        }else if(status.equals("processing")){
            return Color.parseColor("#8e24aa");
        }else{
            return Color.parseColor("#43a047");
        }
    }
}
