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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.example.dynamic_forms.data.Field
import com.example.dynamic_forms.data.Form
import com.example.dynamic_forms.data.Option
import com.example.dynamic_forms.data.repository.FormRepository
import com.example.dynamic_forms.util.FILE_FORM_ONE
import com.example.dynamic_forms.util.FILE_FORM_TWO
import com.example.dynamic_forms.util.FORM_KEY
import com.example.dynamic_forms.util.FORM_ONE

class FormActivity : AppCompatActivity() {

    private val formRepository: FormRepository = FormRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val parseForm = formRepository.getFormFromJson(context = this, fileName = getFormSelected())
        val scrollView = ScrollView(this)
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(32, 32, 32, 32)

        val titulo = TextView(this)
        titulo.text = parseForm.title
        titulo.textSize = 22f
        titulo.setPadding(0, 0, 0, 20)
        layout.addView(titulo)

        setForm(parseForm, layout)
        scrollView.addView(layout)
        setContentView(scrollView)
    }


    private fun setForm(formulario: Form, layout: LinearLayout) {
        formulario.fields.forEach { campo ->
            addFieldToLayout(campo, layout)
        }

        formulario.sections.forEach { section ->
            val sectionTitle = createSectionTitle(section.title)
            layout.addView(sectionTitle)

            for (i in section.from..section.to) {
                formulario.fields.getOrNull(i)?.let { campo ->
                    addFieldToLayout(campo, layout)
                }
            }
        }
    }

    private fun addFieldToLayout(campo: Field, layout: LinearLayout) {
        val label = createLabel(campo.label)
        layout.addView(label)

        val view = when (campo.type) {
            "description" -> createDescriptionView(campo.label)
            "dropdown" -> createDropdownView(campo.options)
            "number" -> createNumberInput(campo.label)
            else -> createTextInput(campo.label)
        }
        layout.addView(view)
    }


    private fun createSectionTitle(htmlText: String): TextView {
        val sectionTitle = TextView(this)
        sectionTitle.text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        sectionTitle.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            20f
        ) // Ajuste o tamanho conforme necessário
        sectionTitle.setPadding(0, 16, 0, 8) // Adicione um pouco de espaçamento
        return sectionTitle
    }

    private fun createLabel(text: String): TextView {
        val label = TextView(this)
        label.text = text
        return label
    }

    private fun createDescriptionView(text: String): TextView {
        val descricao = TextView(this)
        descricao.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        return descricao
    }

    private fun createDropdownView(options: List<Option>?): Spinner {
        val spinner = Spinner(this)
        val opcoes = options?.map { it.label } ?: listOf()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcoes)
        spinner.adapter = adapter
        return spinner
    }

    private fun createNumberInput(hint: String): EditText {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.hint = hint
        return input
    }

    private fun createTextInput(hint: String): EditText {
        val input = EditText(this)
        input.hint = hint
        return input
    }


    private fun getFormSelected(): String {
        val extra = intent.getStringExtra(FORM_KEY)
        return if (extra == FORM_ONE) FILE_FORM_ONE else FILE_FORM_TWO
    }
}