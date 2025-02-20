package com.example.dynamic_forms.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.dynamic_forms.model.data.entities.Field
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.model.data.entities.Section
import com.example.dynamic_forms.model.data.local.FormPreferences
import com.example.dynamic_forms.model.data.repository.FormDataSource
import com.google.gson.Gson
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FormViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: FormViewModel
    private val repository: FormDataSource = mockk(relaxed = true)
    private val formPreferences: FormPreferences = mockk(relaxed = true)
    private val gson = Gson()
    private val observer: Observer<Form> = mockk(relaxed = true)

    private val filename = "test_form"
    private val initialForm =
        Form(title = "Test Form", fields = mutableListOf(), sections = mutableListOf())
    private val newField =
        Field(type = "text", label = "New Field", name = "name", required = true, uuid = "123")
    private val newSection = Section(uuid = "456", title = "New Section")

    @Before
    fun setUp() {
        viewModel = FormViewModel(repository, formPreferences)
        viewModel.formLiveData.observeForever(observer)
    }

    @Test
    fun `getFormSelected should load from cache if available`() {
        every { formPreferences.getFormToCache(filename) } returns gson.toJson(initialForm)

        viewModel.getFormSelected(filename)

        verify { observer.onChanged(initialForm) }
        verify(exactly = 0) { repository.getForm(filename) }
    }

    @Test
    fun `getFormSelected should fetch from repository if cache is empty`() {
        every { formPreferences.getFormToCache(filename) } returns null
        every { repository.getForm(filename) } returns initialForm
        every { formPreferences.saveFormToCache(filename, any()) } returns true

        viewModel.getFormSelected(filename)

        verify { observer.onChanged(initialForm) }
        verify { formPreferences.saveFormToCache(filename, initialForm) }
    }

    @Test
    fun `removeFieldToFormInCache should remove field and update cache`() {
        val formWithField = initialForm.copy(fields = mutableListOf(newField))
        every { formPreferences.getFormToCache(filename) } returns gson.toJson(formWithField)
        every { formPreferences.saveFormToCache(filename, any()) } returns true

        val result = viewModel.removeFieldToFormInCache(filename, removeField = newField)

        assertTrue(result)
        verify { formPreferences.saveFormToCache(filename, any()) }
        verify { observer.onChanged(match { it.fields.isEmpty() }) }
    }

    @Test
    fun `removeFieldToFormInCache should remove section and update cache`() {
        val formWithSection = initialForm.copy(sections = mutableListOf(newSection))
        every { formPreferences.getFormToCache(filename) } returns gson.toJson(formWithSection)
        every { formPreferences.saveFormToCache(filename, any()) } returns true

        val result = viewModel.removeFieldToFormInCache(filename, removeSection = newSection)

        assertTrue(result)
        verify { formPreferences.saveFormToCache(filename, any()) }
        verify { observer.onChanged(match { it.sections.isEmpty() }) }
    }

    @Test
    fun `removeFieldToFormInCache should return false when field or section is not found`() {
        every { formPreferences.getFormToCache(filename) } returns gson.toJson(initialForm)

        val result = viewModel.removeFieldToFormInCache(filename, removeField = newField)

        assertFalse(result)
        verify(exactly = 0) { formPreferences.saveFormToCache(any(), any()) }
    }

    @Test
    fun `save and get input values`() {
        val fieldId = "123"
        val value = "test_value"

        every { formPreferences.saveStringInputValue(filename, fieldId, value) } just Runs
        every { formPreferences.getStringInputValue(filename, fieldId) } returns value

        viewModel.saveInputValue(filename, fieldId, value)
        val result = viewModel.getInputValue(filename, fieldId)

        assertEquals(value, result)
    }

    @Test
    fun `clearForm should call clearInputValues`() {
        every { formPreferences.clearInputValues(filename) } just Runs

        viewModel.clearForm(filename)

        verify { formPreferences.clearInputValues(filename) }
    }
}
