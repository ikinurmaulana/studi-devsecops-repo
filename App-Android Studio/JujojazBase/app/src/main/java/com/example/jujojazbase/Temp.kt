package com.example.jujojazbase;


import android.app.Activity
import json.*;

open class Temp{
    companion object {
        open var data = JSONObject();
        open var anyActivity: Activity = Activity()
        open var notificationTitle = ""
        open var notificationText = ""
        open var notificationChannel = "Jujojaz App"
    }
}