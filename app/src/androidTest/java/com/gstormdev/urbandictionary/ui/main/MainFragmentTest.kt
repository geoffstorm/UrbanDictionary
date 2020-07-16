package com.gstormdev.urbandictionary.ui.main

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gstormdev.urbandictionary.Event
import com.gstormdev.urbandictionary.R
import com.gstormdev.urbandictionary.api.Loading
import com.gstormdev.urbandictionary.api.Resource
import com.gstormdev.urbandictionary.entity.Definition
import com.gstormdev.urbandictionary.util.TaskExecutorWithIdlingResourceRule
import com.gstormdev.urbandictionary.util.ViewModelUtil
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {

    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()

    private lateinit var viewModel: MainViewModel
    private val definitions = MutableLiveData<Resource<List<Definition>>>()
    private val searchTermError = MutableLiveData<Event<String?>>()
    private val emptyText = MutableLiveData<String?>()

    @Before
    fun init() {
        viewModel = mock(MainViewModel::class.java)
        `when`(viewModel.definitions).thenReturn(definitions)
        `when`(viewModel.searchTermError).thenReturn(searchTermError)
        `when`(viewModel.emptyText).thenReturn(emptyText)
        launchFragmentInContainer(themeResId = R.style.Theme_UrbanDictionary) {
            MainFragment().apply {
                viewModelFactory = ViewModelUtil.createFor(viewModel)
            }
        }
    }

    @Test
    fun testProgressIsDisplayedWhenSearching() {
        onView(withId(R.id.progress)).check(matches(not(isDisplayed())))
        definitions.postValue(Loading())
        onView(withId(R.id.progress)).check(matches(isDisplayed()))
    }

    @Test
    fun testEmptyText() {
        onView(withId(R.id.tv_empty)).check(matches(not(isDisplayed())))
        emptyText.postValue("test")
        onView(withId(R.id.tv_empty)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_empty)).check(matches(withText("test")))
        emptyText.postValue(null)
        onView(withId(R.id.tv_empty)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testSearchTermError() {
        searchTermError.postValue(Event("test"))
        onView(withText("test")).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    /*
    Other possible tests:
    - Test loading of actual results, that they display on screen
    - Test sorting of results, that they are reordered
    - Test hiding of the keyboard
     */
}