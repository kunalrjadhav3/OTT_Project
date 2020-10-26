package com.example.diagnal_assignment.dagger.components

import com.example.diagnal_assignment.dagger.modules.ActivityModule
import com.example.diagnal_assignment.views.BaseActivity
import com.example.diagnal_assignment.views.MainActivity
import com.example.myapplication.di.scopes.ActivityScope
import dagger.Component


@ActivityScope
@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    /*
    * baseActivity -> consumer of @ActivityScope dependency
    */
    fun inject(baseActivity: BaseActivity)
}