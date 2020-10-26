package com.example.myapplication.di.scopes

import javax.inject.Scope

/*
Custom Dagger scope
*/

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope {
}