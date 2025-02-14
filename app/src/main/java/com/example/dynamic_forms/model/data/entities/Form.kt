package com.example.dynamic_forms.model.data.entities

data class Form(
    val title: String,
    val fields: List<Field>,
    val sections: List<Section>,
)
