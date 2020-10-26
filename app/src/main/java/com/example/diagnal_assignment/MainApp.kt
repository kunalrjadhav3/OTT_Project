package com.example.diagnal_assignment

import android.app.Application
import com.example.diagnal_assignment.dagger.components.ApplicationComponent
import com.example.diagnal_assignment.dagger.components.DaggerApplicationComponent
import com.example.diagnal_assignment.dagger.modules.ApplicationModule
import com.example.diagnal_assignment.network.ApiInterface

import javax.inject.Inject

class MainApp : Application() {



    companion object{
        internal  lateinit var appContext: MainApp
    }


    @Inject
    lateinit var apiInterface: ApiInterface
    override fun onCreate() {
        super.onCreate()
        appContext =this
        initDagger()
    }

    /**
     * initialize dependencies with Singleton scope
     */
    private fun initDagger(): ApplicationComponent {
        var applicationComponent = DaggerApplicationComponent.builder().applicationModule(
            ApplicationModule()
        )
            .build()
        applicationComponent.inject(this)
        return applicationComponent
    }
}