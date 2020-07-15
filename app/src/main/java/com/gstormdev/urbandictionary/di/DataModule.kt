package com.gstormdev.urbandictionary.di

import com.gstormdev.urbandictionary.data.DefaultDefinitionRepository
import com.gstormdev.urbandictionary.data.DefinitionDataSource
import com.gstormdev.urbandictionary.data.DefinitionRepository
import com.gstormdev.urbandictionary.data.remote.RemoteDefinitionDataSource
import dagger.Binds
import dagger.Module
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class DataModule  {
    @Singleton
    @Binds
    abstract fun bindDefinitionRepository(repository: DefaultDefinitionRepository): DefinitionRepository

    @Singleton
    @Binds
    @Named("remote")
    abstract fun bindRemoteDefinitionDataSource(dataSource: RemoteDefinitionDataSource): DefinitionDataSource
}