package com.example.dynamic_forms.view.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dynamic_forms.databinding.ActivityHomeBinding
import com.example.dynamic_forms.util.FILE_FORM_ONE
import com.example.dynamic_forms.util.FILE_FORM_TWO
import com.example.dynamic_forms.util.FORM_KEY
import com.example.dynamic_forms.view.entries.FormEntriesActivity

class HomeActivity : AppCompatActivity() {

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners() {
        binding.buttonFormOne.setOnClickListener { onClickForm(FILE_FORM_ONE) }
        binding.buttonFormTwo.setOnClickListener { onClickForm(FILE_FORM_TWO) }
    }

    private fun onClickForm(form: String) {
        intent = Intent(this, FormEntriesActivity::class.java)
        intent.putExtra(FORM_KEY, form)
        startActivity(intent)
    }
}