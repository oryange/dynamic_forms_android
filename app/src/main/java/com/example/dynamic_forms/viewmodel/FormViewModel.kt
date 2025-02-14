package com.example.dynamic_forms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dynamic_forms.data.entities.Form
import com.example.dynamic_forms.data.repository.FormRepository
import com.example.dynamic_forms.util.FILE_FORM_ONE
import com.example.dynamic_forms.util.FILE_FORM_TWO
import com.example.dynamic_forms.util.FORM_ONE

class FormViewModel(private val repository: FormRepository) : ViewModel() {


    private val _formLiveData = MutableLiveData<Form>()
    val formLiveData: LiveData<Form> get() = _formLiveData

    fun getFormSelected(formKey: String) {
        val filename = if (formKey == FORM_ONE) FILE_FORM_ONE else FILE_FORM_TWO
        _formLiveData.postValue(repository.getFormFromJson(filename))
    }

}