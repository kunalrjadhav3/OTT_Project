package com.example.diagnal_assignment.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.diagnal_assignment.model.Content
import com.example.diagnal_assignment.model.ContentItems
import com.example.diagnal_assignment.model.Page
import com.example.diagnal_assignment.model.PageResponse
import com.example.diagnal_assignment.viewmodel.MainActivityVM
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.io.FileNotFoundException
import java.lang.Exception

@ExperimentalCoroutinesApi
class NetworkRepositoryTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiInterface: ApiInterface
    private val viewModel by lazy { MainActivityVM() }
    lateinit var successObserver: Observer<List<Content>>
    lateinit var errorObserver: Observer<String>
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this) // mock apiInterface instance
    }

    @Test
    fun getPageDataSuccess() {
        val response = PageResponse(
            Page(
                title = "Romantic Comedy",
                total_content_items = "54",
                page_num = "1",
                page_size = "20",
                content_items = ContentItems(
                    listOf(
                        Content(
                            name = "The Birds",
                            poster_image = "poster1.jpg"
                        )
                    )
                )
            )
        )
        Mockito.doReturn(response).`when`(apiInterface).readFileAsJson("PageOne.json", PageResponse::class.java) // set returnType on mocked object @apiInterface.readFileAsJson
        successObserver = mock(Observer::class.java) as Observer<List<Content>>
        viewModel.successData.observeForever(successObserver) // register Observer to MainActivityVM.successData LiveData
        viewModel.setNetworkRepository(apiInterface=apiInterface)
        viewModel.fetchPageData(
            scope = testScope,
            dispatcher = testDispatcher
        )
        Mockito.verify(successObserver).onChanged(response.page.content_items.content) // verify if MainActivityVM.successData.onChanged() is invoked with PageResponse
    }

    @Test
    fun getPageDataError() {
        Mockito.doReturn(FileNotFoundException()).`when`(apiInterface)
            .readFileAsJson("PageOne.json", PageResponse::class.java)
        errorObserver = mock(Observer::class.java) as Observer<String>
        viewModel.errorData.observeForever(errorObserver) //register Observer to MainActivityVM.errorData LiveData
        viewModel.setNetworkRepository(apiInterface=apiInterface)
        viewModel.fetchPageData(
            scope = testScope,
            dispatcher = testDispatcher
        )
        Mockito.verify(errorObserver).onChanged("java.io.FileNotFoundException") // verify if MainActivityVM.successData.onChanged() is invoked with FileNotFoundException
    }
}