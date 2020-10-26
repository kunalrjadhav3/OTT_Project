package com.example.diagnal_assignment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diagnal_assignment.utils.LoadingStatusType

open class BaseViewModel:ViewModel() {
    val loadingstatusLiveData by lazy { MutableLiveData<LoadingStatusType>() }
}