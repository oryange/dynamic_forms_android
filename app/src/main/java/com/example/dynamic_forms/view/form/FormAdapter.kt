package com.example.dynamic_forms.view.form

import android.R
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamic_forms.model.data.entities.Field
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.model.data.entities.Option
import com.example.dynamic_forms.model.data.entities.Section
import com.example.dynamic_forms.util.FieldType
import com.example.dynamic_forms.viewmodel.FormViewModel

internal class FormAdapter(private val form: Form, private val viewModel: FormViewModel) :
    RecyclerView.Adapter<FormAdapter.FormViewHolder>() {

    private val itemList: MutableList<Any> = mutableListOf()

    init {
        populateItemList()
    }

    private fun populateItemList() {
        form.fields.forEach { field ->
            if (itemList.none { it is Field && it.uuid == field.uuid }) {
                itemList.add(field)
            }
        }
        form.sections.forEach { section ->
            if (itemList.none { it is Section && it.uuid == section.uuid }) {
                itemList.add(section)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = itemList[position]
        return when (item) {
            is Section -> TYPE_SECTION
            is Field -> when (item.type) {
                FieldType.DESCRIPTION.pathname -> TYPE_DESCRIPTION
                FieldType.DROPDOWN.pathname -> TYPE_DROPDOWN
                FieldType.NUMBER.pathname -> TYPE_NUMBER
                else -> TYPE_TEXT
            }

            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        val layout = LinearLayout(parent.context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        return FormViewHolder(layout, viewType)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val item = itemList[position]
        when (item) {
            is Field -> holder.bindField(item)
            is Section -> holder.bindSection(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class FormViewHolder(private val layout: LinearLayout, private val viewType: Int) :
        RecyclerView.ViewHolder(layout) {

        fun bindField(field: Field) {
            layout.removeAllViews()

            val label = TextView(layout.context).apply {
                text = field.label
                textSize = 18f
            }
            layout.addView(label)

            val view = when (viewType) {
                TYPE_DESCRIPTION -> createDescriptionView(field.label)
                TYPE_DROPDOWN -> createDropdownView(field, field.options)
                TYPE_NUMBER -> createNumberInput(field)
                TYPE_TEXT -> createTextInput(field)
                else -> throw IllegalArgumentException("Unknown view type")
            }

            layout.addView(view)
        }

        fun bindSection(section: Section) {
            layout.removeAllViews()

            val title = TextView(layout.context).apply {
                text = HtmlCompat.fromHtml(section.title, HtmlCompat.FROM_HTML_MODE_COMPACT)
                textSize = 20f
            }
            layout.addView(title)
        }

        private fun createDescriptionView(description: String): TextView {
            return TextView(layout.context).apply {
                text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
        }

        private fun createDropdownView(field: Field, options: List<Option>?): Spinner {
            val spinner = Spinner(layout.context)
            val optionLabels = options?.map { it.label } ?: listOf()
            val adapter = ArrayAdapter(
                layout.context,
                R.layout.simple_spinner_dropdown_item,
                optionLabels
            )
            spinner.adapter = adapter
            spinner.setSelection(viewModel.getDropdownValue(field.uuid))

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.saveDropdownValue(fieldId = field.uuid, selectedIndex = position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            return spinner
        }

        private fun createNumberInput(field: Field): EditText {
            val inputValue =
                viewModel.getIntInputValue(field.uuid).takeIf { it != 0 }?.toString() ?: ""
            return EditText(layout.context).apply {
                inputType = InputType.TYPE_CLASS_NUMBER
                hint = field.label
                setText(inputValue)

                addTextChangedListener { editable ->
                    editable.toString().toIntOrNull()?.let { value ->
                        viewModel.saveIntInputValue(field.uuid, value)
                    }
                }
            }
        }

        private fun createTextInput(field: Field): EditText {
            return EditText(layout.context).apply {
                hint = field.label
                setText(viewModel.getInputValue(field.uuid))

                addTextChangedListener { editable ->
                    viewModel.saveInputValue(fieldId = field.uuid, value = editable.toString())
                }
            }
        }
    }

    private companion object {
        const val TYPE_DESCRIPTION = 0
        const val TYPE_DROPDOWN = 1
        const val TYPE_NUMBER = 2
        const val TYPE_TEXT = 3
        const val TYPE_SECTION = 4
    }
}
