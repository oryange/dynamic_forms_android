package com.example.dynamic_forms.data.repository

import android.content.Context
import com.example.dynamic_forms.data.entities.Form
import com.google.gson.Gson

class FormRepository(private val context: Context) {
    private fun loadJsonFromAssets(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    fun getFormFromJson(fileName: String): Form {
        val json = loadJsonFromAssets(fileName = fileName)
        return Gson().fromJson(json, Form::class.java)
    }
}