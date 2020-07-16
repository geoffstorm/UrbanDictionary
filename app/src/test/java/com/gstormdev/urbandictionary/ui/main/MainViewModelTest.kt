package com.gstormdev.urbandictionary.ui.main

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.gstormdev.urbandictionary.MainCoroutineRule
import com.gstormdev.urbandictionary.api.Resource
import com.gstormdev.urbandictionary.data.DefinitionRepository
import com.gstormdev.urbandictionary.entity.Definition
import com.gstormdev.urbandictionary.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainViewModelTest {

    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val mainCoroutineRule = MainCoroutineRule()

    private val repo = mock(DefinitionRepository::class.java)
    private val app = mock(Application::class.java)
    private lateinit var viewModel: MainViewModel

    @Before
    fun init() {
        // need to init after instant executor rule is established
        viewModel = MainViewModel(app, repo)
    }

    @Test
    fun testNullSearchDoesNotTriggerApi() = runBlockingTest {
        val result = mock<Observer<Resource<List<Definition>>>>()
        viewModel.definitions.observeForever(result)
        viewModel.retrieveDefinitions(null)
        verify(repo, never()).getDefinitions("")
    }

    @Test
    fun testEmptySearchDoesNotTriggerApi() = runBlockingTest {
        val result = mock<Observer<Resource<List<Definition>>>>()
        viewModel.definitions.observeForever(result)
        viewModel.retrieveDefinitions("")
        verify(repo, never()).getDefinitions("")
    }

    @Test
    fun testSearchResultGoesToApi() = runBlockingTest {
        val result = mock<Observer<Resource<List<Definition>>>>()
        viewModel.definitions.observeForever(result)

        `when`(repo.getDefinitions("test")).thenReturn(Resource.success(emptyList()))

        viewModel.retrieveDefinitions("test")
        verify(repo).getDefinitions("test")
    }

    /*
    Other possible tests:
    - Verify _searchTermError LiveData is updated correctly in retrieveDefinitions()
    - Verify _emptyText LiveData is updated correctly on retrieval of results
    - Verify that the list is appropriately sorted when applySort() is called
     */
}