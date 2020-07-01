package com.gstormdev.urbandictionary

import android.app.Application

/**
 * We use a separate app for tests to prevent initializing dependency injection
 */
class TestApp : Application()