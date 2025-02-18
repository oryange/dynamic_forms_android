package com.example.dynamic_forms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.model.data.local.FormPreferences
import com.example.dynamic_forms.model.data.repository.FormDataSource
import com.google.gson.Gson

internal class FormEntriesViewModel(
    private val repository: FormDataSource,
    private val formSharedPreferences: FormPreferences
) : ViewModel() {
    private val gson = Gson()
    private val _formLiveData = MutableLiveData<Form>()
    val formLiveData: LiveData<Form> get() = _formLiveData

    private fun getAllInputValues(filename: String) =
        formSharedPreferences.getAllInputValuesFromCache(filename)

    fun getFilteredFormSelected(filename: String) {
        val fromCache = formSharedPreferences.getFormToCache(filename)
        val allowedValues = getAllInputValues(filename)

        val form = if (fromCache != null) {
            gson.fromJson(fromCache, Form::class.java)
        } else {
            repository.getForm(filename).also {
                formSharedPreferences.saveFormToCache(filename, it)
            }
        }

        val filteredForm = form.copy(
            fields = form.fields.filter { field ->
                allowedValues?.keys?.contains("${filename}_input_${field.uuid}") == true || allowedValues?.keys?.contains(
                    "${filename}_dropdown_${field.uuid}"
                ) == true
            },
            sections = form.sections.filter { field ->
                allowedValues?.keys?.contains("_${field.uuid}") == true
            }
        )

        _formLiveData.postValue(filteredForm)
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
}