package com.gstormdev.urbandictionary

import org.mockito.Mockito

/**
 * A Kotlin friendly mock that handles generics
 */
inline fun <reified T> mock(): T = Mockito.mock(T::class.java)