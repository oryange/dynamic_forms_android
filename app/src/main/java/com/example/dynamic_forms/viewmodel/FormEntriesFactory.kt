package com.example.dynamic_forms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dynamic_forms.model.data.local.FormSharedPreferences
import com.example.dynamic_forms.model.data.repository.AssetFormRepository

internal class FormEntriesFactory(
    private val assetFormRepository: AssetFormRepository,
    private val formSharedPreferences: FormSharedPreferences,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormEntriesViewModel::class.java)) {
            return FormEntriesViewModel(assetFormRepository, formSharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown FormEntriesViewModel class")
    }
}
