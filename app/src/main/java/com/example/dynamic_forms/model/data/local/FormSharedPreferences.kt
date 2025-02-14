package com.example.dynamic_forms.model.data.local

import android.content.Context
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.util.FORM_KEY_SHARED_PREFERENCES
import com.google.gson.Gson

class FormSharedPreferences(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(FORM_KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private val gson = Gson()

    fun saveFormToCache(filename: String, form: Form): Boolean {
        editor.putString(filename, gson.toJson(form)).apply()
        return true
    }

    fun getFormToCache(filename: String) = sharedPreferences.getString(filename, null)
}
