package com.example.dynamic_forms.model.data.repository

import android.content.Context
import com.example.dynamic_forms.model.data.entities.Form
import com.google.gson.Gson

internal class AssetFormRepository(private val context: Context) : FormDataSource {
    private fun loadJsonFromAssets(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    override fun getForm(fileName: String): Form {
        val json = loadJsonFromAssets(fileName = fileName)
        return Gson().fromJson(json, Form::class.java)
    }
}