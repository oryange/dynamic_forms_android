package com.example.dynamic_forms.data.entities

data class Field(
    val type: String,
    val label: String,
    val name: String,
    val required: Boolean,
    val uuid: String,
    val options: List<Option>? = null,
)
