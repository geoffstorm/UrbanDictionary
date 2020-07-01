package com.gstormdev.urbandictionary.ui.main

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.gstormdev.urbandictionary.api.ListWrapper
import com.gstormdev.urbandictionary.api.Resource
import com.gstormdev.urbandictionary.api.UrbanDictionaryRestClient
import com.gstormdev.urbandictionary.entity.Definition
import com.gstormdev.urbandictionary.mock
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import retrofit2.Call

@RunWith(JUnit4::class)
class MainViewModelTest {

    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()
    private val restClient = mock(UrbanDictionaryRestClient::class.java)
    private val app = mock(Application::class.java)
    private lateinit var viewModel: MainViewModel

    @Before
    fun init() {
        // need to init after instant executor rule is established
        viewModel = MainViewModel(app, restClient)
    }

    @Test
    fun testNullSearchDoesNotTriggerApi() {
        val result = mock<Observer<Resource<List<Definition>>>>()
        viewModel.definitions.observeForever(result)
        viewModel.retrieveDefinitions(null)
        verify(restClient, never()).getDefinition("")
    }

    @Test
    fun testEmptySearchDoesNotTriggerApi() {
        val result = mock<Observer<Resource<List<Definition>>>>()
        viewModel.definitions.observeForever(result)
        viewModel.retrieveDefinitions("")
        verify(restClient, never()).getDefinition("")
    }

    @Test
    fun testSearchResultGoesToApi() {
        val result = mock<Observer<Resource<List<Definition>>>>()
        viewModel.definitions.observeForever(result)

        `when`(restClient.getDefinition("test")).thenReturn(mock<Call<ListWrapper<Definition>>>())

        viewModel.retrieveDefinitions("test")
        verify(restClient).getDefinition("test")
    }

    /*
    Other possible tests:
    - Verify _searchTermError LiveData is updated correctly in retrieveDefinitions()
    - Verify _emptyText LiveData is updated correctly on retrieval of results
    - Verify that the list is appropriately sorted when applySort() is called
     */
}