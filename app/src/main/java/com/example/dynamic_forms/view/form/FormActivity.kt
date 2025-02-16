package com.example.dynamic_forms.view.form

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamic_forms.model.data.local.FormSharedPreferences
import com.example.dynamic_forms.model.data.repository.AssetFormRepository
import com.example.dynamic_forms.util.FORM_KEY
import com.example.dynamic_forms.viewmodel.FormFactory
import com.example.dynamic_forms.viewmodel.FormViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FormActivity : AppCompatActivity() {

    private val viewModel: FormViewModel by viewModels {
        FormFactory(AssetFormRepository(this), FormSharedPreferences(this))
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: FormAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupFab()

        intent.getStringExtra(FORM_KEY)?.let { viewModel.getFormSelected(it) }
        setContentView(createLayout())
    }

    private fun createLayout(): FrameLayout {
        return FrameLayout(this).apply {
            addView(setupRecyclerView())
            addView(setupFab())
        }
    }

    private fun setupRecyclerView(): RecyclerView {
        recyclerView = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@FormActivity)
        }
        return recyclerView
    }

    private fun setupFab(): FloatingActionButton {
        fab = FloatingActionButton(this).apply {
            setImageResource(android.R.drawable.ic_menu_delete)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 48
                bottomMargin = 48
                gravity = Gravity.END or Gravity.BOTTOM
            }
            setOnClickListener { clearForm() }
        }
        return fab
    }

    private fun setupObservers() {
        viewModel.formLiveData.observe(this) { form ->
            adapter = FormAdapter(form, viewModel)
            recyclerView.adapter = adapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearForm() {
        viewModel.clearForm()
        adapter.notifyDataSetChanged()
    }
}
