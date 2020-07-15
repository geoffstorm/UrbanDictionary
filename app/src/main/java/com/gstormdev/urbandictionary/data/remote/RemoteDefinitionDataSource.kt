package com.gstormdev.urbandictionary.data.remote

import com.gstormdev.urbandictionary.api.Resource
import com.gstormdev.urbandictionary.api.UrbanDictionaryRestClient
import com.gstormdev.urbandictionary.data.DefinitionDataSource
import com.gstormdev.urbandictionary.entity.Definition
import retrofit2.awaitResponse
import javax.inject.Inject

class RemoteDefinitionDataSource @Inject constructor(private val restClient: UrbanDictionaryRestClient) : DefinitionDataSource {
    override suspend fun getDefinitions(term: String): Resource<List<Definition>> {
        val response = restClient.getDefinition(term).awaitResponse()
        return if (response.isSuccessful) {
            Resource.success(response.body()?.list ?: emptyList())
        } else {
            val msg = response.message()
            val errorMsg = if (msg.isNullOrEmpty()) response.errorBody()?.toString() else msg
            Resource.error(errorMsg ?: "unknown error", null)
        }
    }
}