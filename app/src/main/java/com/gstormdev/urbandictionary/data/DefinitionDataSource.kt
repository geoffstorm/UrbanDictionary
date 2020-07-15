package com.gstormdev.urbandictionary.data

import com.gstormdev.urbandictionary.api.Resource
import com.gstormdev.urbandictionary.entity.Definition

interface DefinitionDataSource {
    suspend fun getDefinitions(term: String): Resource<List<Definition>>
}