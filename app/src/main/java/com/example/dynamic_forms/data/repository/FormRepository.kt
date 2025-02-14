package com.example.dynamic_forms.data.repository

import android.content.Context
import com.example.dynamic_forms.data.Form
import com.google.gson.Gson

class FormRepository(context: Context) {
    private fun loadJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    fun getFormFromJson(context: Context, fileName: String): Form {
        val json = loadJsonFromAssets(context = context, fileName = fileName)
        return Gson().fromJson(json, Form::class.java)
    }
}