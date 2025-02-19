package com.example.dynamic_forms.view.entries

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
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
    private lateinit var formSelected: String
    private lateinit var fab: FloatingActionButton
    private lateinit var fabBack: FloatingActionButton
    private lateinit var fieldTypeSpinner: Spinner
    private lateinit var fieldLabel: EditText
    private lateinit var fieldName: EditText
    private lateinit var fieldRequired: CheckBox
    private lateinit var addButton: Button

    private val viewModel: FormEntriesViewModel by viewModels {
        FormEntriesFactory(FormSharedPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_entries)

        setupView()
        setupFab()
        setupAddButton()

        formSelected = intent.getStringExtra(FORM_KEY).toString()
    }

    private fun setupView() {
        fab = findViewById(R.id.fab)
        fabBack = findViewById(R.id.fab_back)
        fieldTypeSpinner = findViewById(R.id.field_type)
        fieldLabel = findViewById(R.id.field_label)
        fieldName = findViewById(R.id.field_name)
        fieldRequired = findViewById(R.id.field_required)
        addButton = findViewById(R.id.btn_add_field)
    }

    private fun setupAddButton() {
        addButton.setOnClickListener {
            val type =
                fieldTypeSpinner.selectedItem.toString()
            val label = fieldLabel.text.toString()
            val name = fieldName.text.toString()
            val required = fieldRequired.isChecked
// todo() implement this:
//            val newSection = Section(
//
//            )
            val newField = Field(
                type = type,
                label = label,
                name = name,
                required = required,
                uuid = "${formSelected}_input_${UUID.randomUUID()}",
                options = null
            )

            viewModel.addFieldToFormInCache(formSelected, newField)
        }
    }

    private fun setupFab() {
        fabBack.setOnClickListener { finish() }
    }
}
