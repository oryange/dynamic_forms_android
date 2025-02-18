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

    override fun saveStringInputValue(filename: String, fieldId: String, value: String) {
        editor.putString("${filename}_input_$fieldId", value).apply()
    }

    override fun getIntInputValue(filename: String, fieldId: String): Int? {
        return sharedPreferences.getInt("${filename}_input_$fieldId", 0)
    }

    override fun saveIntInputValue(filename: String, fieldId: String, value: Int) {
        editor.putInt("${filename}_input_$fieldId", value).apply()
    }

    override fun getStringInputValue(filename: String, fieldId: String): String? {
        return sharedPreferences.getString("${filename}_input_$fieldId", null)
    }

    override fun saveDropdownValue(filename: String, fieldId: String, selectedIndex: Int) {
        editor.putInt("${filename}_dropdown_$fieldId", selectedIndex).apply()
    }

    override fun getDropdownValue(filename: String, fieldId: String): Int {
        return sharedPreferences.getInt("${filename}_dropdown_$fieldId", 0)
    }

    override fun getAllInputValuesFromCache(filename: String) = sharedPreferences.all

    override fun clearInputValues(filename: String) {
        val keysToRemove = getAllInputValuesFromCache(filename).keys.filter {
            it?.contains("${filename}_input_") == true || it.contains(
                "${filename}_dropdown_"
            )
        }

        keysToRemove.forEach { editor.remove(it) }
        editor.commit()
    }
}
