package com.example.dynamic_forms.model.data.local

import com.example.dynamic_forms.model.data.entities.Form

internal interface FormPreferences {
    fun saveFormToCache(filename: String, form: Form): Boolean
    fun getFormToCache(filename: String): String?
    fun saveIntInputValue(fieldId: String, value: Int)
    fun getIntInputValue(fieldId: String): Int?
    fun saveStringInputValue(fieldId: String, value: String)
    fun getStringInputValue(fieldId: String): String?
    fun saveDropdownValue(fieldId: String, selectedIndex: Int)
    fun getDropdownValue(fieldId: String): Int
    fun clearInputValues()
}
