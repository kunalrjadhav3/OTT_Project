package com.example.diagnal_assignment.dagger.modules

import com.example.diagnal_assignment.utils.view.ViewUtil
import com.example.myapplication.di.scopes.ActivityScope
import dagger.Module
import dagger.Provides


@Module
class ActivityModule {
    /*
    ViewUtil instance is set to @ActivityScope because this instance will be required only in Views (Activity/Fragment)
    */
    @Provides
    @ActivityScope
    fun getViewUtil()= ViewUtil()

}