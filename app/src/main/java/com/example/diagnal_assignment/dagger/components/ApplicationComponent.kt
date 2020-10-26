package com.example.diagnal_assignment.dagger.components

import com.example.diagnal_assignment.MainApp
import com.example.diagnal_assignment.dagger.modules.ApplicationModule
import dagger.Component
import javax.inject.Singleton




@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    /*
    * mainApp -> consumer of @Singleton dependency
    */
    fun inject(mainApp: MainApp)
}