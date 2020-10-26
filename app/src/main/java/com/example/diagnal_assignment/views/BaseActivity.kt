package com.example.diagnal_assignment.views

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.diagnal_assignment.dagger.components.ActivityComponent
import com.example.diagnal_assignment.dagger.components.DaggerActivityComponent
import com.example.diagnal_assignment.dagger.modules.ActivityModule
import com.example.diagnal_assignment.utils.view.ViewUtil
import javax.inject.Inject

open class BaseActivity:AppCompatActivity() {

    protected var activityComponent: ActivityComponent?=null
    @Inject
    protected lateinit var viewUtil: ViewUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    initDependency()
    }


    /*
    Inject @com.example.myapplication.di.scopes.ActivityScope dependencies
    */
    private fun initDependency() {
        activityComponent =
            DaggerActivityComponent.builder().activityModule(ActivityModule()).build()
        activityComponent!!.inject(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        activityComponent = null // @ActivityScope dependencies free for garbage collection on Activity onDestroy
    }
}