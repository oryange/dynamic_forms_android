package com.example.dynamic_forms.view.entries

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dynamic_forms.R
import com.example.dynamic_forms.model.data.entities.Field
import com.example.dynamic_forms.model.data.entities.Section
import com.example.dynamic_forms.model.data.local.FormSharedPreferences
import com.example.dynamic_forms.util.FORM_KEY
import com.example.dynamic_forms.viewmodel.FormEntriesFactory
import com.example.dynamic_forms.viewmodel.FormEntriesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.UUID

internal class FormEntriesActivity : AppCompatActivity() {

    private val formSelected: String by lazy { intent.getStringExtra(FORM_KEY).orEmpty() }

    private val viewModel: FormEntriesViewModel by viewModels {
        FormEntriesFactory(FormSharedPreferences(this))
    }

    private lateinit var fieldTypeSpinner: Spinner
    private lateinit var fieldLabel: EditText
    private lateinit var fieldRequired: CheckBox
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_entries)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        findViewById<FloatingActionButton>(R.id.fab_back).setOnClickListener { finish() }

        fieldTypeSpinner = findViewById(R.id.field_type)
        fieldLabel = findViewById(R.id.field_label)
        fieldRequired = findViewById(R.id.field_required)
        addButton = findViewById(R.id.btn_add_field)
    }

    private fun setupListeners() {
        addButton.setOnClickListener { addFieldToForm() }
    }

    private fun addFieldToForm() {
        val type = fieldTypeSpinner.selectedItem.toString()
        val label = fieldLabel.text.toString().trim()
        val name = fieldLabel.text.toString().trim()
        val required = fieldRequired.isChecked

        if (label.isEmpty() || name.isEmpty()) {
            showToast("Label and name cannot be empty!")
            return
        }

        val wasAdded = if (type == "section") {
            addSection(name)
        } else {
            addField(type, label, name, required)
        }

        showToast(if (wasAdded) "Entry added successfully!" else "Error adding entry!")
    }

    private fun addSection(name: String): Boolean {
        val newSection = Section(
            title = name,
            uuid = "${formSelected}_input_${UUID.randomUUID()}"
        )
        return viewModel.addFieldToFormInCache(
            filename = formSelected,
            newSection = newSection
        )
    }

    private fun addField(type: String, label: String, name: String, required: Boolean): Boolean {
        val newField = Field(
            type = type,
            label = label,
            name = name,
            required = required,
            uuid = "${formSelected}_input_${UUID.randomUUID()}",
            options = null
        )
        return viewModel.addFieldToFormInCache(
            filename = formSelected,
            newField = newField
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
