package com.example.dynamic_forms.model.data.entities

internal data class Field(
    val type: String,
    val label: String,
    val name: String,
    val required: Boolean,
    val uuid: String,
    val options: List<Option>? = null,
)
