package com.example.diagnal_assignment.dagger.modules

import com.example.diagnal_assignment.network.ApiInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



@Module
class ApplicationModule {



/*
ApiInterface instance is set @Singleton because in real case scenario,
it will Retrofit ApiInterface containing all APIs, and this instance will be required anywhere in Application
 */
@Provides
@Singleton
fun getApiInterface()=ApiInterface()





}