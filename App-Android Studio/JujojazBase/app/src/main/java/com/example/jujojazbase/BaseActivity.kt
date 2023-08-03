package com.example.jujojazbase;

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent

import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import json.*;

open class BaseActivity : AppCompatActivity() {
    var aes: AES = AES();
    var andLib: AndroidLib = AndroidLib();
    var activity: Activity? = null;
    companion object {
        var fullImage: MutableList<ImageView> = mutableListOf();
        var clipboard: ClipboardManager? = null;
        var version = "-1";
    }

//    fun homeActivity(context: Context, data: JSONObject){
//        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.fadein)
//        anim.duration = 500
//        anim.start();
//        var homeIntent = Intent(this, HomeActivity::class.java);
//        activity!!.startActivity(homeIntent);
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            200 -> {
                if (grantResults.size > 0) {
                    baseActivity();
                }
            }
            else -> {
                var dialog = andLib!!.dialog("Permissions denied", "in order IBA Banking running, please accept the permissions.");
                var listener0: DialogInterface.OnClickListener = object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        andLib!!.exitAppFull();
                    }
                }
                dialog.setPositiveButton("Okay, Restart app please.", listener0);
                dialog.show();
            }
        }
    }


    fun fileName(callName: String): String {
        return Configuration.listFile.get(callName)!!;
    }

    fun baseActivity(){
        setContentView(R.layout.activity_loading_main__portrait);
        Handler().postDelayed({
            BaseActivity.clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?;
            var loginIntent = Intent(this, Auth::class.java);
            var homeIntent = Intent(this, HomeActivity::class.java);

            try {
                var loginDat = JSONObject(andLib.readFile(fileName("dat")));
                var net = object : Auth() {
                    override fun loginApiSucceed(data: JSONObject) {
                        try {

                            if (data.get("rc") == "00") {
                                activity!!.startActivity(homeIntent);
                            } else {
                                activity!!.startActivity(loginIntent);
                            }
                        } catch (e: Exception) {
                            println(e.printStackTrace());
                        }
                    }

//                    override fun loginApiError(msg: String) {
//                        var intent = Intent(
//                            this@BaseActivity,
//                            DisconnectedActivity::class.java
//                        );
//                        activity!!.startActivity(intent);
//                    }

//                    override fun loginApiFail(msg: String) {
//                        activity!!.startActivity(loginIntent);
//                    }
                }
//                net.andLib = this.andLib!!;
                net.loginApi(loginDat.get("userID") as String, loginDat.get("pass") as String, true);

            } catch (e: JSONException) {
                activity!!.startActivity(loginIntent);
            }
        }, 3000);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        activity = this@BaseActivity;
        andLib = AndroidLib(this, this@BaseActivity, this@BaseActivity);
        andLib!!.requestPermisssions();
    }
}
