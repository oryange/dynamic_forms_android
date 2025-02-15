package com.example.dynamic_forms.model.data.local

import com.example.dynamic_forms.model.data.entities.Form

internal interface FormPreferences {
    fun saveFormToCache(filename: String, form: Form): Boolean
    fun getFormToCache(filename: String): String?
    fun saveInputValue(fieldId: String, value: String)
    fun getInputValue(fieldId: String): String?
    fun saveDropdownValue(fieldId: String, selectedIndex: Int)
    fun getDropdownValue(fieldId: String): Int
    fun clearInputValues()
}
