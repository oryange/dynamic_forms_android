package com.example.dynamic_forms.data

import com.example.dynamic_forms.model.data.entities.Field
import com.example.dynamic_forms.model.data.entities.Form
import com.example.dynamic_forms.model.data.entities.Option
import com.example.dynamic_forms.model.data.entities.Section
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DataClassTest {

    private val gson = Gson()

    @Test
    fun `should create Form correctly`() {
        val form = Form(
            title = "Test Form",
            fields = listOf(),
            sections = listOf()
        )

        assertEquals("Test Form", form.title)
        assertTrue(form.fields.isEmpty())
        assertTrue(form.sections.isEmpty())
    }

    @Test
    fun `should create Field correctly`() {
        val field = Field(
            type = "text",
            label = "Name",
            name = "name",
            required = true,
            uuid = "1234",
            options = listOf(Option(label = "Option 1", value = "1"))
        )

        assertEquals("text", field.type)
        assertEquals("Name", field.label)
        assertEquals("name", field.name)
        assertTrue(field.required)
        assertEquals("1234", field.uuid)
        assertNotNull(field.options)
        assertEquals(1, field.options?.size)
        assertEquals("Option 1", field.options?.first()?.label)
    }

    @Test
    fun `should create Section correctly`() {
        val section = Section(
            title = "Personal Info",
            from = 1,
            to = 10,
            index = 0,
            uuid = "5678"
        )

        assertEquals("Personal Info", section.title)
        assertEquals(1, section.from)
        assertEquals(10, section.to)
        assertEquals(0, section.index)
        assertEquals("5678", section.uuid)
    }

    @Test
    fun `should copy Field correctly`() {
        val field = Field(
            type = "text",
            label = "Email",
            name = "email",
            required = true,
            uuid = "9999"
        )

        val copiedField = field.copy(label = "New Email")

        assertNotEquals(field, copiedField)
        assertEquals("New Email", copiedField.label)
        assertEquals("Email", field.label)
    }

    @Test
    fun `should serialize and deserialize Form correctly`() {
        val form = Form(
            title = "Sample Form",
            fields = listOf(
                Field(
                    type = "text",
                    label = "First Name",
                    name = "first_name",
                    required = true,
                    uuid = "abc123"
                )
            ),
            sections = listOf(
                Section(
                    title = "Main Section",
                    uuid = "sec456"
                )
            )
        )

        val json = gson.toJson(form)
        val deserializedForm = gson.fromJson(json, Form::class.java)

        assertEquals(form, deserializedForm)
    }
}
