package com.example.diagnal_assignment.utils

sealed class LoadingStatusType {
    class Loading:LoadingStatusType()
    class Loaded:LoadingStatusType()
}