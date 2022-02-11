package com.izhar.melsha;

import android.content.Context;

public class Utils {
    public String getUrl(Context context){
        return context.getSharedPreferences("url", Context.MODE_PRIVATE).getString("url", "");
    }
}
