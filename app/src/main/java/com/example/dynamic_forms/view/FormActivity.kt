package com.example.dynamic_forms.view

import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.dynamic_forms.R
import com.example.dynamic_forms.data.entities.Field
import com.example.dynamic_forms.data.entities.Form
import com.example.dynamic_forms.data.entities.Option
import com.example.dynamic_forms.data.repository.FormRepository
import com.example.dynamic_forms.util.FORM_KEY
import com.example.dynamic_forms.util.FieldType
import com.example.dynamic_forms.viewmodel.FormViewModel

class FormActivity : AppCompatActivity() {
    private val viewModel: FormViewModel by viewModels {
        FormFactory(FormRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scrollView = ScrollView(this)
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(32, 32, 32, 32)

        intent.getStringExtra(FORM_KEY)?.let { viewModel.getFormSelected(it) }

        viewModel.formLiveData.observe(this) { form ->
            layout.addView(createTitleView(form))
            populateForm(layout, form)
        }

        scrollView.addView(layout)
        setContentView(scrollView)
    }

    private fun createTitleView(form: Form): TextView {
        return TextView(this).apply {
            text = form.title
            textSize = 26f
            setTextColor(ContextCompat.getColor(this@FormActivity, R.color.purple_200))
            setPadding(0, 20, 0, 20)
        }
    }


    private fun populateForm(layout: LinearLayout, form: Form) {
        form.fields.forEach { field ->
            addFieldToLayout(field, layout)
        }

        form.sections.forEach { section ->
            val sectionTitle = createSectionTitle(section.title)
            layout.addView(sectionTitle)

            for (i in section.from..section.to) {
                form.fields.getOrNull(i)?.let { campo ->
                    addFieldToLayout(campo, layout)
                }
            }
        }
    }

    private fun addFieldToLayout(field: Field, layout: LinearLayout) {
        val label = createLabel(field.label)
        layout.addView(label)

        val view = when (field.type) {
            FieldType.DESCRIPTION.pathname -> createDescriptionView(field.label)
            FieldType.DROPDOWN.pathname -> createDropdownView(field.options)
            FieldType.NUMBER.pathname -> createNumberInput(field.label)
            else -> createTextInput(field.label)
        }
        layout.addView(view)
    }


    private fun createSectionTitle(htmlText: String): TextView {
        return TextView(this).apply {
            text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f)
            setPadding(0, 16, 0, 8)
        }

    }

    private fun createLabel(text: String): TextView {
        return TextView(this).apply {
            this.text = text
        }
    }

    private fun createDescriptionView(description: String): TextView {
        return TextView(this).apply {
            text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun createDropdownView(options: List<Option>?): Spinner {
        val spinner = Spinner(this)
        val option = options?.map { it.label } ?: listOf()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, option)
        spinner.adapter = adapter
        return spinner
    }

    private fun createNumberInput(hint: String): EditText {
        return EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            this.hint = hint
        }
    }

    private fun createTextInput(hint: String): EditText {
        return EditText(this).apply {
            this.hint = hint
        }
    }
}
