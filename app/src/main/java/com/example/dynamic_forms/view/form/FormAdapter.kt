package com.example.dynamic_forms.view.form

import android.R
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
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

internal class FormAdapter(
    private val filename: String,
    private val form: Form,
    private val viewModel: FormViewModel
) :
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

            val linearLayoutForField = LinearLayout(layout.context).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(8, 8, 8, 8)
            }

            val label = TextView(layout.context).apply {
                text = field.label
                textSize = 18f
                setPadding(8, 8, 8, 8)
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    weight = 1f
                    bottomMargin = 16
                }
            }

            val removeButton = buttonRemoveItem(field = field).apply {
                layoutParams = LinearLayout.LayoutParams(48, 48).apply {
                    marginStart = 16
                    topMargin = 8
                }
            }

            linearLayoutForField.addView(label)
            linearLayoutForField.addView(removeButton)
            layout.addView(linearLayoutForField)

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

            val linearLayoutForSection = LinearLayout(layout.context).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(8, 8, 8, 8)
            }

            val title = TextView(layout.context).apply {
                text = HtmlCompat.fromHtml(section.title, HtmlCompat.FROM_HTML_MODE_COMPACT)
                textSize = 22f
                setPadding(16, 16, 16, 16)
                setBackgroundColor(0xFFEEEEEE.toInt())
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    weight = 1f
                    topMargin = 32
                    bottomMargin = 16
                }
            }

            val removeButton = buttonRemoveItem(section = section).apply {
                layoutParams = LinearLayout.LayoutParams(48, 48).apply {
                    marginStart = 16
                    topMargin = 8
                }
            }

            linearLayoutForSection.addView(title)
            linearLayoutForSection.addView(removeButton)
            layout.addView(linearLayoutForSection)
        }


        private fun createDescriptionView(description: String): TextView {
            return TextView(layout.context).apply {
                text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
                textSize = 16f
                setPadding(8, 8, 8, 8)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        private fun createDropdownView(field: Field, options: List<Option>?): Spinner {
            val spinner = Spinner(layout.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 16
                }
            }

            val optionLabels = options?.map { it.label } ?: listOf()
            val adapter = ArrayAdapter(
                layout.context,
                R.layout.simple_spinner_dropdown_item,
                optionLabels
            )
            spinner.adapter = adapter
            spinner.setSelection(
                viewModel.getDropdownValue(
                    filename = filename,
                    fieldId = field.uuid
                )
            )

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.saveDropdownValue(
                        filename = filename,
                        fieldId = field.uuid,
                        selectedIndex = position
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            return spinner
        }

        private fun createNumberInput(field: Field): EditText {
            val inputValue =
                viewModel.getIntInputValue(filename = filename, fieldId = field.uuid)
                    .takeIf { it != 0 }?.toString() ?: ""
            return EditText(layout.context).apply {
                inputType = InputType.TYPE_CLASS_NUMBER
                hint = field.label
                setText(inputValue)
                setPadding(16, 16, 16, 16)
                setBackgroundResource(android.R.drawable.edit_text)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 16
                }

                addTextChangedListener { editable ->
                    editable.toString().toIntOrNull()?.let { value ->
                        viewModel.saveIntInputValue(
                            filename = filename,
                            fieldId = field.uuid,
                            value = value
                        )
                    }
                }
            }
        }

        private fun createTextInput(field: Field): EditText {
            return EditText(layout.context).apply {
                hint = field.label
                setText(viewModel.getInputValue(filename = filename, fieldId = field.uuid))
                setPadding(16, 16, 16, 16)
                setBackgroundResource(android.R.drawable.edit_text)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 16
                }

                addTextChangedListener { editable ->
                    viewModel.saveInputValue(
                        filename = filename,
                        fieldId = field.uuid,
                        value = editable.toString()
                    )
                }
            }
        }

        private fun buttonRemoveItem(field: Field? = null, section: Section? = null): ImageButton {
            return ImageButton(layout.context).apply {
                layoutParams = LinearLayout.LayoutParams(48, 48).apply {
                    marginStart = 16
                }
                setImageResource(R.drawable.ic_delete)
                setColorFilter(0xFF888888.toInt())
                setOnClickListener {
                    viewModel.removeFieldToFormInCache(
                        filename = filename,
                        removeField = field,
                        removeSection = section
                    )
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
