package com.example.dynamic_forms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dynamic_forms.model.data.local.FormSharedPreferences
import com.example.dynamic_forms.model.data.repository.AssetFormRepository

internal class FormFactory(
    private val assetFormRepository: AssetFormRepository,
    private val formSharedPreferences: FormSharedPreferences,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormViewModel::class.java)) {
            return FormViewModel(assetFormRepository, formSharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown FormViewModel class")
    }
}
