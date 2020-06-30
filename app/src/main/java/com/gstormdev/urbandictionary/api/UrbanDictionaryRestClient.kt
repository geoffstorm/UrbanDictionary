package com.gstormdev.urbandictionary.api

import com.gstormdev.urbandictionary.entity.Definition
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UrbanDictionaryRestClient  {
    @GET("define")
    fun getDefinition(@Query("term") term: String): Call<ListResponse<Definition>>
}