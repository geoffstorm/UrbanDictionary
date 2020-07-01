package com.gstormdev.urbandictionary.entity

data class Definition(
    val defid: String,  // Definition Id
    val definition: String,
    val thumbs_up: Int,
    val thumbs_down: Int,
    val word: String // The word being defined
) {
    /**
     * The definition can contain square brackets linking words to other definitions
     * This will remove those for simpler display
     */
    fun getDisplayFormattedDefinition() = definition.replace(Regex("[\\[\\]]"), "")
}