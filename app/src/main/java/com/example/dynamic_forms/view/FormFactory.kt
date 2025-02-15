package com.example.dynamic_forms.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dynamic_forms.model.data.local.FormSharedPreferences
import com.example.dynamic_forms.model.data.repository.FormRepository
import com.example.dynamic_forms.viewmodel.FormViewModel

internal class FormFactory(
    private val formRepository: FormRepository,
    private val formSharedPreferences: FormSharedPreferences,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormViewModel::class.java)) {
            return FormViewModel(formRepository, formSharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown FormViewModel class")
    }
}
