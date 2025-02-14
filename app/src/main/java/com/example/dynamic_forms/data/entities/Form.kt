package com.example.dynamic_forms.data.entities

data class Form(
    val title: String,
    val fields: List<Field>,
    val sections: List<Section>,
)
