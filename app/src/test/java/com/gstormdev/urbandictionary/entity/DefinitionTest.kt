package com.gstormdev.urbandictionary.entity

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DefinitionTest {

    @Test
    fun testFormattedDefinition() {
        val singleBracket = Definition("", "te[st", 0, 0, "")
        assertEquals("test", singleBracket.getDisplayFormattedDefinition())

        val singleBracketPair = Definition("", "this [is a] test", 0, 0, "")
        assertEquals("this is a test", singleBracketPair.getDisplayFormattedDefinition())

        val multipleBrackets = Definition("", "[][te]][s]]]]][]t[][][][]", 0, 0, "")
        assertEquals("test", multipleBrackets.getDisplayFormattedDefinition())

        val noBrackets = Definition("", "test", 0, 0, "")
        assertEquals("test", noBrackets.getDisplayFormattedDefinition())
    }
}