package com.gstormdev.urbandictionary.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gstormdev.urbandictionary.ui.main.MainViewModel
import com.gstormdev.urbandictionary.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}