package com.example.dynamic_forms.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dynamic_forms.model.data.entities.Field
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.model.data.local.FormPreferences
import com.google.gson.Gson

internal class FormEntriesViewModel(
    private val formSharedPreferences: FormPreferences
) : ViewModel() {
    private val gson = Gson()


    fun addFieldToFormInCache(filename: String, newField: Field): Boolean {
        val formJson = formSharedPreferences.getFormToCache(filename)
        if (formJson != null) {
            val form = gson.fromJson(formJson, Form::class.java)

            val updatedFields = form.fields.toMutableList()
            updatedFields.add(0, newField)

            val updatedForm = form.copy(fields = updatedFields)

            return formSharedPreferences.saveFormToCache(filename, updatedForm)
        }

        return false
    }
}