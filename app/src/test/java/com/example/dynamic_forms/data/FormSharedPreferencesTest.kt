package com.example.dynamic_forms.data

import android.content.Context
import android.content.SharedPreferences
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.model.data.local.FormSharedPreferences
import com.example.dynamic_forms.util.FORM_KEY_SHARED_PREFERENCES
import com.google.gson.Gson
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FormSharedPreferencesTest {

    private lateinit var formPreferences: FormSharedPreferences
    private val context: Context = mockk()
    private val sharedPreferences: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk(relaxed = true)
    private val gson = Gson()

    private val filename = "test_form"
    private val fieldId = "field_123"
    private val form = Form(title = "Test Form", fields = listOf(), sections = listOf())

    @Before
    fun setUp() {
        every {
            context.getSharedPreferences(
                FORM_KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE
            )
        } returns sharedPreferences
        every { sharedPreferences.edit() } returns editor
        formPreferences = FormSharedPreferences(context)
    }

    @Test
    fun `saveFormToCache should store form as JSON`() {
        every { editor.putString(filename, any()) } returns editor
        every { editor.apply() } just Runs

        val result = formPreferences.saveFormToCache(filename, form)

        assertTrue(result)
        verify { editor.putString(filename, gson.toJson(form)) }
        verify { editor.apply() }
    }

    @Test
    fun `getFormToCache should retrieve stored form JSON`() {
        every { sharedPreferences.getString(filename, null) } returns gson.toJson(form)

        val result = formPreferences.getFormToCache(filename)

        assertEquals(gson.toJson(form), result)
    }

    @Test
    fun `saveStringInputValue should store string value`() {
        every { editor.putString("${filename}_input_$fieldId", "test_value") } returns editor

        formPreferences.saveStringInputValue(filename, fieldId, "test_value")

        verify { editor.putString("${filename}_input_$fieldId", "test_value") }
        verify { editor.apply() }
    }

    @Test
    fun `getStringInputValue should retrieve stored string value`() {
        every {
            sharedPreferences.getString(
                "${filename}_input_$fieldId",
                null
            )
        } returns "test_value"

        val result = formPreferences.getStringInputValue(filename, fieldId)

        assertEquals("test_value", result)
    }

    @Test
    fun `saveIntInputValue should store integer value`() {
        every { editor.putInt("${filename}_input_$fieldId", 10) } returns editor

        formPreferences.saveIntInputValue(filename, fieldId, 10)

        verify { editor.putInt("${filename}_input_$fieldId", 10) }
        verify { editor.apply() }
    }

    @Test
    fun `getIntInputValue should retrieve stored integer value`() {
        every { sharedPreferences.getInt("${filename}_input_$fieldId", 0) } returns 10

        val result = formPreferences.getIntInputValue(filename, fieldId)

        assertEquals(10, result)
    }

    @Test
    fun `clearInputValues should remove all form-related keys`() {
        val mockKeys = setOf("${filename}_input_$fieldId", "${filename}_dropdown_$fieldId")
        every { sharedPreferences.all } returns mockKeys.associateWith { "test" }
        every { editor.remove(any()) } returns editor

        formPreferences.clearInputValues(filename)

        mockKeys.forEach {
            verify { editor.remove(it) }
        }
        verify { editor.apply() }
    }
}
