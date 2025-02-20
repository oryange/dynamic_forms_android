package com.example.dynamic_forms.data


import android.content.Context
import android.content.res.AssetManager
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.model.data.repository.AssetFormRepository
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream

class AssetFormRepositoryTest {

    private lateinit var repository: AssetFormRepository
    private val context: Context = mockk()
    private val assetManager: AssetManager = mockk()
    private val fileName = "test_form.json"
    private val sampleJson = """{"title":"Test Form","fields":[],"sections":[]}"""
    private val expectedForm = Gson().fromJson(sampleJson, Form::class.java)

    @Before
    fun setUp() {
        every { context.assets } returns assetManager
        repository = AssetFormRepository(context)
    }

    @Test
    fun `getForm should return correct Form from JSON`() {
        val inputStream = ByteArrayInputStream(sampleJson.toByteArray())
        every { assetManager.open(fileName) } returns inputStream

        val result = repository.getForm(fileName)

        assertEquals(expectedForm, result)
        verify { assetManager.open(fileName) }
    }
}
