package com.example.dynamic_forms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.model.data.local.FormPreferences
import com.example.dynamic_forms.model.data.repository.FormRepository
import com.example.dynamic_forms.util.FILE_FORM_ONE
import com.example.dynamic_forms.util.FILE_FORM_TWO
import com.example.dynamic_forms.util.FORM_ONE
import com.google.gson.Gson

internal class FormViewModel(
    private val repository: FormRepository,
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
            val form = repository.getFormFromJson(filename)
            _formLiveData.postValue(form)
            formSharedPreferences.saveFormToCache(filename, form)
        }
    }
}