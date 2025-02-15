package com.example.dynamic_forms.model.data.repository

import com.example.dynamic_forms.model.data.entities.Form

internal interface FormDataSource {
    fun getForm(fileName: String): Form
}
