package com.example.diagnal_assignment.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.diagnal_assignment.model.Content
import com.example.diagnal_assignment.model.PageResponse
import com.example.diagnal_assignment.network.ApiInterface
import com.example.diagnal_assignment.network.NetworkRepository
import com.example.diagnal_assignment.utils.LoadingStatusType
import com.example.diagnal_assignment.utils.ResponseType
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class MainActivityVM : BaseViewModel() {

    internal val successData by lazy { MutableLiveData<List<Content>>() }
    internal val errorData by lazy { MutableLiveData<String>() }
     private lateinit var networkRepository : NetworkRepository
    internal var pageNum = 1
    internal val originalList: ArrayList<Content> by lazy { ArrayList<Content>() }
    private var mLoading = false

    internal fun setNetworkRepository(apiInterface: ApiInterface){
        networkRepository=NetworkRepository(apiInterface)
    }

    internal fun fetchPageData(isIncrementPage:Boolean=false,scope:CoroutineScope=viewModelScope,dispatcher: CoroutineDispatcher=Dispatchers.IO) {
        if(!mLoading){ // restrict network fetch on user scroll, while loading state on UI
            if(isIncrementPage) // isIncrementPage=false on loading first page
                pageNum+=1
            mLoading=true
            loadingstatusLiveData.postValue(LoadingStatusType.Loading())
            scope.launch{
                networkRepository.getPageData(pageNum = pageNum,dispatcher = dispatcher) {
                    when (it) {
                        is ResponseType.Response -> when (val response = it.response) {
                                is PageResponse -> {
                                    val responseContent = response.page.content_items.content
                                    originalList.addAll(responseContent)
                                    successData.postValue(responseContent)
                                }
                            }

                        is ResponseType.Error -> errorData.postValue(it.message)
                    }
                    mLoading=false
                    loadingstatusLiveData.postValue(LoadingStatusType.Loaded())
                }
           }
        }



    }

    /*
    Rx operator switchMap is used to unsubscribe to earlier Observables, and observe new one when user enters a new text in searchBar
    * */
    internal fun resgisterSearchStringSubscription(stringObservable: Observable<String>): Observable<MutableList<Content>> {
         return stringObservable.debounce(300, TimeUnit.MILLISECONDS).switchMap { getModifiedObservable(it) }
    }


    /*
    @text -> user input on searchBar EditText
     */
    private fun getModifiedObservable(text: String): Observable<MutableList<Content>>? {
        return Observable.create(object : ObservableOnSubscribe<MutableList<Content>> {
            override fun subscribe(e: ObservableEmitter<MutableList<Content>>) {
                val searchResultMatchList = mutableListOf<Content>()
                if (text.isBlank() || text.length<=2)
                    searchResultMatchList.addAll(originalList) // Restrict user search if text.length<=2
                else
                        searchResultMatchList.addAll(originalList.filter { it.name.contains(text,true) })
                e.onNext(searchResultMatchList)
                e.onComplete()

            }
        }).subscribeOn(Schedulers.io())
    }
}