package com.example.freela.util.helpers

import com.google.android.material.textfield.TextInputEditText

class Helpers {
    fun isInputValid(inputFields: List<TextInputEditText>): Boolean {
        var isValid = true

        for (inputText in inputFields) {
            if (inputText.text.isNullOrBlank()) {
                inputText.error = "Campo obrigatório"
                isValid = false
            } else {
                inputText.error = null
            }
        }

        return isValid
    }
}