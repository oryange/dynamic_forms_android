package com.example.dynamic_forms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dynamic_forms.model.data.local.FormSharedPreferences

internal class FormEntriesFactory(
    private val formSharedPreferences: FormSharedPreferences,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormEntriesViewModel::class.java)) {
            return FormEntriesViewModel(formSharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown FormEntriesViewModel class")
    }
}
