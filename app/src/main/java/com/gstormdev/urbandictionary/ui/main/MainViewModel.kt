package com.gstormdev.urbandictionary.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gstormdev.urbandictionary.R
import com.gstormdev.urbandictionary.api.ListWrapper
import com.gstormdev.urbandictionary.api.Resource
import com.gstormdev.urbandictionary.api.UrbanDictionaryRestClient
import com.gstormdev.urbandictionary.entity.Definition
import com.gstormdev.urbandictionary.testing.OpenForTesting
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

@OpenForTesting
class MainViewModel @Inject constructor(app: Application, private val restClient: UrbanDictionaryRestClient) : AndroidViewModel(app) {

    private enum class SortOrder {
        THUMBS_UP,
        THUMBS_DOWN,
        DEFAULT
    }
    // Keep mutable LiveData private, we don't want this set outside of this class
    private val _definitions = MutableLiveData<Resource<List<Definition>>>(Resource.success(null))
    private val _searchTermError = MutableLiveData<String?>(null)
    private val _emptyText = MutableLiveData<String?>(getApplication<Application>().getString(R.string.empty_search))

    // Expose an immutable LiveData for outside consumption
    var definitions: LiveData<Resource<List<Definition>>> = _definitions
    val searchTermError: LiveData<String?> = _searchTermError
    val emptyText: LiveData<String?> = _emptyText

    private var searchTerm: String = ""
    private var sortOrder = SortOrder.DEFAULT

    fun retrieveDefinitions(term: String?) {
        if (!term.isNullOrBlank()) {
            _searchTermError.postValue(null)
            val sanitizedTerm = term.toLowerCase(Locale.getDefault()).trim()
            if (sanitizedTerm != searchTerm) {
                searchTerm = sanitizedTerm
                fetchDefinitions(searchTerm)
            }
        } else {
            _searchTermError.postValue(getApplication<Application>().getString(R.string.search_term_error))
        }
    }

    // This should be done in a separate class, following the Single Responsibility Principle
    // Shortcut taken due to time constraints
    private fun fetchDefinitions(term: String) {
        // Set state to loading, but don't get rid of current definitions
        _definitions.postValue(Resource.loading(_definitions.value?.data))
        // Reset sort order to default for the list coming in
        sortOrder = SortOrder.DEFAULT
        restClient.getDefinition(term).enqueue(object : Callback<ListWrapper<Definition>> {
            override fun onResponse(
                call: Call<ListWrapper<Definition>>,
                response: Response<ListWrapper<Definition>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()?.list
                    if (body.isNullOrEmpty()) {
                        _emptyText.postValue(getApplication<Application>().getString(R.string.empty_search_results, searchTerm))
                    } else {
                        _emptyText.postValue(null)
                    }
                    _definitions.postValue(Resource.success(body))
                } else {
                    val msg = response.errorBody()?.string()
                    val errorMsg = if (msg.isNullOrEmpty()) response.message() else msg
                    _definitions.postValue(Resource.error(errorMsg ?: "unknown error", null))
                }
            }

            override fun onFailure(call: Call<ListWrapper<Definition>>, t: Throwable) {
                _definitions.postValue(Resource.error(t.message ?: "unknown error", null))
            }
        })
    }

    fun applySort() {
        applySort(_definitions.value?.data)
    }

    // Extremely simple sort that just toggles between two states
    // Ideally, there should be some indication to the user about which sort is being applied
    // However, I left this out for simplicity, and how subjective the UX could be
    private fun applySort(definitions: List<Definition>?) {
        if (definitions.isNullOrEmpty()) {
            // If there is nothing to sort, exit early
            return
        }

        computeNextSortOrder()

        val sortedDefinitions = when (sortOrder) {
            SortOrder.THUMBS_UP -> definitions.sortedByDescending { it.thumbs_up }
            SortOrder.THUMBS_DOWN -> definitions.sortedByDescending { it.thumbs_down }
            else -> null  // Don't apply a sort
        }

        sortedDefinitions?.let {
            // Only post changes if we have a list to post
            _definitions.postValue(Resource.success(it))
        }
    }

    // We are assuming that the sort button simply toggles between THUMBS_UP and THUMBS_DOWN sorting
    // Default sorting only exists when a new list is retrieved
    private fun computeNextSortOrder() {
        sortOrder = when (sortOrder) {
            SortOrder.DEFAULT -> SortOrder.THUMBS_UP
            SortOrder.THUMBS_UP -> SortOrder.THUMBS_DOWN
            SortOrder.THUMBS_DOWN -> SortOrder.THUMBS_UP
        }
    }
}