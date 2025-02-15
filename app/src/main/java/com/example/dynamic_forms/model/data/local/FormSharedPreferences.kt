package com.example.dynamic_forms.model.data.local

import android.content.Context
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.util.FORM_KEY_SHARED_PREFERENCES
import com.google.gson.Gson

internal class FormSharedPreferences(context: Context) : FormPreferences {
    private val sharedPreferences =
        context.getSharedPreferences(FORM_KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private val gson = Gson()

    override fun saveFormToCache(filename: String, form: Form): Boolean {
        editor.putString(filename, gson.toJson(form)).apply()
        return true
    }

    override fun getFormToCache(filename: String) = sharedPreferences.getString(filename, null)

    override fun saveStringInputValue(fieldId: String, value: String) {
        editor.putString("input_$fieldId", value).apply()
    }

    override fun getIntInputValue(fieldId: String): Int? {
        return sharedPreferences.getInt("input_$fieldId", 0)
    }

    override fun saveIntInputValue(fieldId: String, value: Int) {
        editor.putInt("input_$fieldId", value).apply()
    }

    override fun getStringInputValue(fieldId: String): String? {
        return sharedPreferences.getString("input_$fieldId", null)
    }

    override fun saveDropdownValue(fieldId: String, selectedIndex: Int) {
        editor.putInt("dropdown_$fieldId", selectedIndex).apply()
    }

    override fun getDropdownValue(fieldId: String): Int {
        return sharedPreferences.getInt("dropdown_$fieldId", 0)
    }

    override fun clearInputValues() {
        val keysToRemove =
            sharedPreferences.all.keys.filter { it.startsWith("input_") || it.startsWith("dropdown_") }
        keysToRemove.forEach { editor.remove(it) }
        editor.apply()
    }
}
