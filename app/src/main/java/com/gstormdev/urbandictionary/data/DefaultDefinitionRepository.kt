package com.gstormdev.urbandictionary.data

import com.gstormdev.urbandictionary.api.Resource
import com.gstormdev.urbandictionary.entity.Definition
import javax.inject.Inject
import javax.inject.Named

class DefaultDefinitionRepository @Inject constructor(
        @Named("remote") private val remoteDataSource: DefinitionDataSource
) : DefinitionRepository {
    override suspend fun getDefinitions(term: String): Resource<List<Definition>> {
        return remoteDataSource.getDefinitions(term)
    }
}