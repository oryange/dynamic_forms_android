package com.example.dynamic_forms.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dynamic_forms.data.repository.FormRepository
import com.example.dynamic_forms.viewmodel.FormViewModel

class FormFactory(
    private val formRepository: FormRepository,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormViewModel::class.java)) {
            return FormViewModel(formRepository) as T
        }
        throw IllegalArgumentException("Unknown FormViewModel class")
    }
}
