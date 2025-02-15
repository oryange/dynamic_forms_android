package com.example.dynamic_forms.view.form

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamic_forms.model.data.local.FormSharedPreferences
import com.example.dynamic_forms.model.data.repository.AssetFormRepository
import com.example.dynamic_forms.util.FORM_KEY
import com.example.dynamic_forms.viewmodel.FormFactory
import com.example.dynamic_forms.viewmodel.FormViewModel

class FormActivity : AppCompatActivity() {
    private val viewModel: FormViewModel by viewModels {
        FormFactory(AssetFormRepository(this), FormSharedPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this)

        intent.getStringExtra(FORM_KEY)?.let { viewModel.getFormSelected(it) }

        viewModel.formLiveData.observe(this) { form ->
            val adapter = FormAdapter(form, viewModel)
            recyclerView.adapter = adapter
        }

        setContentView(recyclerView)
    }
}
