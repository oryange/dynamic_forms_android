package com.example.dynamic_forms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.model.data.local.FormPreferences
import com.example.dynamic_forms.model.data.repository.AssetFormRepository
import com.example.dynamic_forms.util.FILE_FORM_ONE
import com.example.dynamic_forms.util.FILE_FORM_TWO
import com.example.dynamic_forms.util.FORM_ONE
import com.google.gson.Gson

internal class FormViewModel(
    private val repository: AssetFormRepository,
    private val formSharedPreferences: FormPreferences
) : ViewModel() {

    private val gson = Gson()
    private val _formLiveData = MutableLiveData<Form>()
    val formLiveData: LiveData<Form> get() = _formLiveData

    fun getFormSelected(formKey: String) {
        val filename = if (formKey == FORM_ONE) FILE_FORM_ONE else FILE_FORM_TWO
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

    fun saveInputValue(fieldId: String, value: String) {
        formSharedPreferences.saveStringInputValue(fieldId, value)
    }

    fun saveIntInputValue(fieldId: String, value: Int) {
        formSharedPreferences.saveIntInputValue(fieldId, value)
    }

    fun saveDropdownValue(fieldId: String, selectedIndex: Int) {
        formSharedPreferences.saveDropdownValue(fieldId, selectedIndex)
    }

    fun getInputValue(fieldId: String): String {
        return formSharedPreferences.getStringInputValue(fieldId) ?: ""
    }

    fun getIntInputValue(fieldId: String): Int {
        return formSharedPreferences.getIntInputValue(fieldId) ?: 0
    }

    fun getDropdownValue(fieldId: String): Int {
        return formSharedPreferences.getDropdownValue(fieldId)
    }

    fun clearInputValues() {
        formSharedPreferences.clearInputValues()
    }
}