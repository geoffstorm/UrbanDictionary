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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(app: Application, private val restClient: UrbanDictionaryRestClient) : AndroidViewModel(app) {
    // Keep mutable LiveData private, we don't want this set outside of this class
    private var _definitions = MutableLiveData<Resource<List<Definition>>>(Resource.loading(null))
    private val _searchTermError = MutableLiveData<String?>(null)

    // Expose an immutable LiveData for outside consumption
    var definitions: LiveData<Resource<List<Definition>>> = _definitions
    val searchTermError: LiveData<String?> = _searchTermError

    private var searchTerm: String = ""

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
        restClient.getDefinition(term).enqueue(object : Callback<ListWrapper<Definition>> {
            override fun onResponse(
                call: Call<ListWrapper<Definition>>,
                response: Response<ListWrapper<Definition>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    _definitions.postValue(Resource.success(body?.list))
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
}