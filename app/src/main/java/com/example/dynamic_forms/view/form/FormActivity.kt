package com.example.dynamic_forms.view.form

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamic_forms.R
import com.example.dynamic_forms.model.data.local.FormSharedPreferences
import com.example.dynamic_forms.model.data.repository.AssetFormRepository
import com.example.dynamic_forms.util.FORM_KEY
import com.example.dynamic_forms.viewmodel.FormFactory
import com.example.dynamic_forms.viewmodel.FormViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FormActivity : AppCompatActivity() {
    private lateinit var formSelected: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var title: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: FormAdapter

    private val viewModel: FormViewModel by viewModels {
        FormFactory(AssetFormRepository(this), FormSharedPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        setupRecyclerView()
        setupFab()
        setupObservers()
        formSelected = intent.getStringExtra(FORM_KEY).toString()
        setupTitle()
        viewModel.getFormSelected(formSelected)
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)
        title = findViewById(R.id.form_title)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupFab() {
        fab.setOnClickListener { clearForm() }
    }

    private fun setupTitle() {
        title.text = "Form selected ${formSelected}"
    }

    private fun setupObservers() {
        viewModel.formLiveData.observe(this) { form ->
            adapter = FormAdapter(formSelected, form, viewModel)
            recyclerView.adapter = adapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearForm() {
        viewModel.clearForm(formSelected)
        adapter.notifyDataSetChanged()
    }
}
