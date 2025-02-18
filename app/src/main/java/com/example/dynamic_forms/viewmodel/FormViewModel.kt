package com.example.dynamic_forms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.model.data.local.FormPreferences
import com.example.dynamic_forms.model.data.repository.FormDataSource
import com.google.gson.Gson

internal class FormViewModel(
    private val repository: FormDataSource,
    private val formSharedPreferences: FormPreferences
) : ViewModel() {

    private val gson = Gson()
    private val _formLiveData = MutableLiveData<Form>()
    val formLiveData: LiveData<Form> get() = _formLiveData

    fun getFormSelected(filename: String) {
        val fromCache = formSharedPreferences.getFormToCache(filename)

        if (fromCache != null) {
            val form = gson.fromJson(fromCache, Form::class.java)
            _formLiveData.postValue(form)
        } else {
            val form = repository.getForm(filename)
            _formLiveData.postValue(form)
            formSharedPreferences.saveFormToCache(filename, form)
        }
    }

    fun saveInputValue(filename: String, fieldId: String, value: String) {
        formSharedPreferences.saveStringInputValue(filename, fieldId, value)
    }

    fun saveIntInputValue(filename: String, fieldId: String, value: Int) {
        formSharedPreferences.saveIntInputValue(filename, fieldId, value)
    }

    fun saveDropdownValue(filename: String, fieldId: String, selectedIndex: Int) {
        formSharedPreferences.saveDropdownValue(filename, fieldId, selectedIndex)
    }

    fun getInputValue(filename: String, fieldId: String): String {
        return formSharedPreferences.getStringInputValue(filename, fieldId) ?: ""
    }

    fun getIntInputValue(filename: String, fieldId: String): Int {
        return formSharedPreferences.getIntInputValue(filename, fieldId) ?: 0
    }

    fun getDropdownValue(filename: String, fieldId: String): Int {
        return formSharedPreferences.getDropdownValue(filename, fieldId)
    }

    fun clearForm(filename: String) {
        formSharedPreferences.clearInputValues(filename)
    }
}