package com.example.medtracker

import android.app.Application
import android.content.Context

class YourApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        val sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
    }

    companion object {
        private lateinit var context: YourApplicationClass

        fun getContext(): Context {
            return context
        }
    }
}
