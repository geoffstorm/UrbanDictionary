package com.gstormdev.urbandictionary.di

import com.gstormdev.urbandictionary.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule  {
    @ContributesAndroidInjector
    abstract fun contributesMainFragment(): MainFragment
}