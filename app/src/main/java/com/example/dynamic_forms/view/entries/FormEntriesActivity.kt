package com.example.dynamic_forms.view.entries

import android.content.Intent
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
import com.example.dynamic_forms.view.form.FormActivity
import com.example.dynamic_forms.viewmodel.FormEntriesFactory
import com.example.dynamic_forms.viewmodel.FormEntriesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FormEntriesActivity : AppCompatActivity() {
    private lateinit var formSelected: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var fabBack: FloatingActionButton
    private lateinit var textEmptyList: TextView
    private lateinit var adapter: FormEntriesAdapter

    private val viewModel: FormEntriesViewModel by viewModels {
        FormEntriesFactory(AssetFormRepository(this), FormSharedPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_entries)

        setupRecyclerView()
        setupFab()
        setupObservers()

        formSelected = intent.getStringExtra(FORM_KEY).toString()
        viewModel.getFilteredFormSelected(formSelected)
    }


    override fun onResume() {
        super.onResume()
        viewModel.getFilteredFormSelected(formSelected)
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)
        fabBack = findViewById(R.id.fab_back)
        textEmptyList = findViewById(R.id.empty_list_message)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupObservers() {
        viewModel.formLiveData.observe(this) { form ->
            adapter = FormEntriesAdapter(formSelected, form, viewModel)
            recyclerView.adapter = adapter

            if (form.fields.isEmpty()) {
                textEmptyList.visibility = TextView.VISIBLE
            } else {
                textEmptyList.visibility = TextView.GONE
            }
        }
    }

    private fun setupFab() {
        fab.setOnClickListener { onClickForm(formSelected) }
        fabBack.setOnClickListener { finish() }
    }

    private fun onClickForm(form: String) {
        intent = Intent(this, FormActivity::class.java)
        intent.putExtra(FORM_KEY, form)
        startActivity(intent)
    }
}