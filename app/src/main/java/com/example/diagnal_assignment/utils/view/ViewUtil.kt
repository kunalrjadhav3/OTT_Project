package com.example.diagnal_assignment.utils.view

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import com.example.diagnal_assignment.utils.LoadingStatusType
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class ViewUtil {


    internal fun disableUserTouch(
        activity: Activity, type: LoadingStatusType, progressBar: View
    ) {
        when (type) {
            is LoadingStatusType.Loading -> {
                activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                progressBar.visibility = View.VISIBLE

            }
            is LoadingStatusType.Loaded -> {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                progressBar.visibility = View.GONE
            }
        }
    }

    internal inline fun detectKeyboardChanges(container: View, context: Context, crossinline callBack:(Boolean)->Unit) {
        container.getViewTreeObserver()
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val heightDiff: Int =
                        container.getRootView().getHeight() - container.getHeight()
                    val isKeyboardOpen = if (heightDiff > dpToPx(context, 200F))
                        true
                    else
                        false
                    callBack(isKeyboardOpen)
                }
            })

    }

    private fun dpToPx(context: Context, valueInDp: Float): Float {
        val metrics: DisplayMetrics = context.getResources().getDisplayMetrics()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
    }


    internal fun displaySnackbar(rootView: View, message: String) {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }
}