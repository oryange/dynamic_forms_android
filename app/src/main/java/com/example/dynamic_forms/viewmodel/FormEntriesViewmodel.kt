package com.example.dynamic_forms.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dynamic_forms.model.data.entities.Field
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.model.data.entities.Section
import com.example.dynamic_forms.model.data.local.FormPreferences
import com.google.gson.Gson

internal class FormEntriesViewModel(
    private val formSharedPreferences: FormPreferences
) : ViewModel() {
    private val gson = Gson()

    fun addFieldToFormInCache(
        filename: String,
        newField: Field? = null,
        newSection: Section? = null
    ): Boolean {
        val formJson = formSharedPreferences.getFormToCache(filename) ?: return false
        val form = gson.fromJson(formJson, Form::class.java)

        val updatedFields = form.fields.toMutableList()
        val updatedSections = form.sections.toMutableList()

        if (newField != null) {
            updatedFields.add(0, newField)
        }
        if (newSection != null) {
            updatedSections.add(0, newSection)
        }

        val updatedForm = form.copy(fields = updatedFields, sections = updatedSections)
        return formSharedPreferences.saveFormToCache(filename, updatedForm)
    }
}