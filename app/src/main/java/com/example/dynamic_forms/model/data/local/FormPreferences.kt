package com.example.dynamic_forms.model.data.local

import com.example.dynamic_forms.model.data.entities.Form

internal interface FormPreferences {
    fun saveFormToCache(filename: String, form: Form): Boolean
    fun getFormToCache(filename: String): String?
    fun getAllInputValuesFromCache(filename: String): Map<String, *>?
    fun saveIntInputValue(filename: String, fieldId: String, value: Int)
    fun getIntInputValue(filename: String, fieldId: String): Int?
    fun saveStringInputValue(filename: String, fieldId: String, value: String)
    fun getStringInputValue(filename: String, fieldId: String): String?
    fun saveDropdownValue(filename: String, fieldId: String, selectedIndex: Int)
    fun getDropdownValue(filename: String, fieldId: String): Int
    fun clearInputValues(filename: String)
}
