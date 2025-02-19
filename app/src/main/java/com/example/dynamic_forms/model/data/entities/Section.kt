package com.example.dynamic_forms.model.data.entities

internal data class Section(
    val title: String,
    val from: Int? = null,
    val to: Int? = null,
    val index: Int? = null,
    val uuid: String,
)
