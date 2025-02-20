package com.example.dynamic_forms.viewmodel

import com.example.dynamic_forms.model.data.entities.Field
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.model.data.entities.Section
import com.example.dynamic_forms.model.data.local.FormPreferences
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FormEntriesViewModelTest {

    private lateinit var viewModel: FormEntriesViewModel
    private val formPreferences: FormPreferences = mockk(relaxed = true)
    private val gson = Gson()
    private val filename = "test_form"
    private val initialForm = Form(title = "dynamic form", fields = listOf(), sections = listOf())
    private val newField =
        Field(type = "text", label = "New Field", name = "name", required = true, uuid = "123")
    private val newSection = Section(uuid = "456", title = "New Section")

    @Before
    fun setUp() {
        viewModel = FormEntriesViewModel(formPreferences)
    }

    @Test
    fun `addFieldToFormInCache should add a field to form`() {
        every { formPreferences.getFormToCache(filename) } returns gson.toJson(initialForm)
        every { formPreferences.saveFormToCache(filename, any()) } returns true

        val result = viewModel.addFieldToFormInCache(filename, newField)

        assertTrue(result)
        verify { formPreferences.saveFormToCache(filename, any()) }
    }

    @Test
    fun `addFieldToFormInCache should correctly update form with a new field`() {
        every { formPreferences.getFormToCache(filename) } returns gson.toJson(initialForm)
        every { formPreferences.saveFormToCache(filename, any()) } returns true

        val formSlot = slot<Form>()
        every { formPreferences.saveFormToCache(filename, capture(formSlot)) } returns true

        val result = viewModel.addFieldToFormInCache(filename, newField)

        assertTrue(result)
        verify { formPreferences.saveFormToCache(filename, any()) }

        val updatedForm = formSlot.captured
        assertTrue(updatedForm.fields.contains(newField))
    }

    @Test
    fun `addFieldToFormInCache should add a section to form`() {
        every { formPreferences.getFormToCache(filename) } returns gson.toJson(initialForm)
        every { formPreferences.saveFormToCache(filename, any()) } returns true

        val result = viewModel.addFieldToFormInCache(filename, newSection = newSection)

        assertTrue(result)
        verify { formPreferences.saveFormToCache(filename, any()) }
    }

    @Test
    fun `addFieldToFormInCache should add both field and section`() {
        every { formPreferences.getFormToCache(filename) } returns gson.toJson(initialForm)
        every { formPreferences.saveFormToCache(filename, any()) } returns true

        val result = viewModel.addFieldToFormInCache(filename, newField, newSection)

        assertTrue(result)
        verify { formPreferences.saveFormToCache(filename, any()) }
    }

    @Test
    fun `addFieldToFormInCache should return false when form is not found in cache`() {
        every { formPreferences.getFormToCache(filename) } returns null

        val result = viewModel.addFieldToFormInCache(filename, newField)

        assertFalse(result)
        verify(exactly = 0) { formPreferences.saveFormToCache(any(), any()) }
    }
}
