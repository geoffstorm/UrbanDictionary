package com.gstormdev.urbandictionary.data.remote

import com.gstormdev.urbandictionary.api.Error
import com.gstormdev.urbandictionary.api.Resource
import com.gstormdev.urbandictionary.api.Success
import com.gstormdev.urbandictionary.api.UrbanDictionaryRestClient
import com.gstormdev.urbandictionary.data.DefinitionDataSource
import com.gstormdev.urbandictionary.entity.Definition
import retrofit2.awaitResponse
import java.lang.Exception
import javax.inject.Inject

class RemoteDefinitionDataSource @Inject constructor(private val restClient: UrbanDictionaryRestClient) : DefinitionDataSource {
    override suspend fun getDefinitions(term: String): Resource<List<Definition>> {
        return try {
            val response = restClient.getDefinition(term).awaitResponse()
            if (response.isSuccessful) {
                // Request succeeded (status 2xx)
                Success(response.body()?.list ?: emptyList())
            } else {
                // Request succeeded with server error (4xx, 5xx)
                val msg = response.message()
                val errorMsg = if (msg.isNullOrEmpty()) response.errorBody()?.toString() else msg
                Error(errorMsg ?: "unknown error")
            }
        } catch (e: Exception) {
            // Request failed (e.g. no network connection)
            Error(e.message ?: "unknown error")
        }
    }
}