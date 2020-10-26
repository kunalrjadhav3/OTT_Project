package com.example.diagnal_assignment.network

import com.example.diagnal_assignment.model.PageResponse
import com.example.diagnal_assignment.utils.ResponseType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class NetworkRepository(val apiInterface: ApiInterface) {
    /*
    *@param dispatcher - Dispatcher.IO for Device / TestCoroutineDispatcher for Unit test
    * */
    suspend inline fun getPageData(
        pageNum: Int,
        dispatcher: CoroutineDispatcher,
        crossinline callback: (ResponseType) -> Unit
    ) {
        withContext(dispatcher) {
            val pageLabel = when (pageNum) {
                1 -> "PageOne.json"
                2 -> "PageTwo.json"
                3 -> "PageThree.json"
                else -> "" // Case when User trying to scroll page more then available Json files
            }

            when (val response = apiInterface.readFileAsJson(pageLabel, PageResponse::class.java)) {
                is Exception -> callback(ResponseType.Error(response.toString()))
                is PageResponse -> callback(ResponseType.Response(response))
            }

        }

    }
}