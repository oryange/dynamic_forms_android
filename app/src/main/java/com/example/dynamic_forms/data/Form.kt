package com.example.dynamic_forms.data

data class Form(
    val title: String,
    val fields: List<Field>,
    val sections: List<Section>,
)
