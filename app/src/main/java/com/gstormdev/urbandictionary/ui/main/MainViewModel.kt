package com.gstormdev.urbandictionary.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.gstormdev.urbandictionary.Event
import com.gstormdev.urbandictionary.R
import com.gstormdev.urbandictionary.api.Loading
import com.gstormdev.urbandictionary.api.Resource
import com.gstormdev.urbandictionary.api.Success
import com.gstormdev.urbandictionary.data.DefinitionRepository
import com.gstormdev.urbandictionary.entity.Definition
import com.gstormdev.urbandictionary.testing.OpenForTesting
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@OpenForTesting
class MainViewModel @Inject constructor(private val app: Application, private val repository: DefinitionRepository) : ViewModel() {

    enum class SortOrder {
        THUMBS_UP,
        THUMBS_DOWN,
        DEFAULT
    }

    // Keep mutable LiveData private, we don't want this set outside of this class
    private val _definitions = MutableLiveData<Resource<List<Definition>>>(Success(emptyList()))
    private val _searchTermError = MutableLiveData<Event<String?>>(null)
    private val _emptyText = MutableLiveData<String?>(app.getString(R.string.empty_search))

    // Expose an immutable LiveData for outside consumption
    val definitions: LiveData<Resource<List<Definition>>> = _definitions
    val searchTermError: LiveData<Event<String?>> = _searchTermError
    val emptyText: LiveData<String?> = _emptyText

    private var searchTerm: String = ""
    private var sortOrder = SortOrder.DEFAULT

    fun retrieveDefinitions(term: String?) {
        if (!term.isNullOrBlank()) {
            _searchTermError.postValue(null)
            val sanitizedTerm = term.toLowerCase(Locale.getDefault()).trim()
            searchTerm = sanitizedTerm
            fetchDefinitions(searchTerm)
        } else {
            _searchTermError.postValue(Event(app.getString(R.string.search_term_error)))
        }
    }

    private fun fetchDefinitions(term: String) {
        // Set state to loading, but don't get rid of current definitions
        _definitions.postValue(Loading())
        // Reset sort order to default for the list coming in
        sortOrder = SortOrder.DEFAULT
        viewModelScope.launch {
            val definitions = repository.getDefinitions(term)
            if (definitions is Success && definitions.data.isEmpty()) {
                // TODO need to find a way to properly get the search term into the Fragment so that
                // we don't have to compose the String here, and then we can get rid of the Application reference
                _emptyText.postValue(app.getString(R.string.empty_search_results, searchTerm))
            } else {
                _emptyText.postValue(null)
            }
            _definitions.postValue(definitions)
        }
    }

    fun applySort(sortOrder: SortOrder) {
        if (this.sortOrder == sortOrder) {
            // If the sorting is already set, exit early
            return
        }

        this.sortOrder = sortOrder
        val definitions = (_definitions.value as? Success)?.data ?: emptyList()

        if (definitions.isNullOrEmpty()) {
            // If there is nothing to sort, exit early
            return
        }

        val sortedDefinitions = when (sortOrder) {
            SortOrder.THUMBS_UP -> definitions.sortedByDescending { it.thumbs_up }
            SortOrder.THUMBS_DOWN -> definitions.sortedByDescending { it.thumbs_down }
            else -> null  // Don't apply a sort
        }

        sortedDefinitions?.let {
            // Only post changes if we have a list to post
            _definitions.postValue(Success(it))
        }
    }
}