package com.example.diagnal_assignment.utils

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import com.example.diagnal_assignment.MainApp
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

fun getImageFromName(name:String):Drawable{
val appContext=MainApp.appContext
   val drawablepath="@drawable/${name.replace(".jpg","")}"
try {
 return  appContext.resources.getDrawable(appContext.getResources().getIdentifier(drawablepath, null, appContext.packageName))
}catch (error:Exception){
   throw error
}
}



