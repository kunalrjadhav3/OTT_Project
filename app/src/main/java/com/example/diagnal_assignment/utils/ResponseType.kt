package com.example.diagnal_assignment.utils

sealed class ResponseType {
    data class Response(val response: Any):ResponseType()
    data class Error(val message:String):ResponseType()
}