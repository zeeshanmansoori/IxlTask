package com.example.ixltask.utils

import android.util.Patterns
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import com.example.ixltask.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputEditText.setEditTextListener(
    textInputLayout: TextInputLayout,
    doOnTextChange: ((String?) -> Unit)? = null,
    defaultEmptyMsg: String? = null,
) {

    val inputTypePassword =
        EditorInfo.TYPE_CLASS_TEXT.or(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
    val inputTypeEmail =
        EditorInfo.TYPE_CLASS_TEXT.or(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)

    doOnTextChanged { text, _, _, _ ->

        if (!text.isNullOrBlank() &&
            inputType == inputTypeEmail &&
            !Patterns.EMAIL_ADDRESS.matcher(text).matches()
        )
            textInputLayout.error = context.getString(R.string.enter_valid_email)
        else if (inputType == EditorInfo.TYPE_CLASS_PHONE && text?.length != 10)
            textInputLayout.error = context.getString(R.string.enter_10_digit_phone_number)
//        else if (
//            inputType == inputTypePassword &&
//            (text ?: "").length < 8
//        )
//            textInputLayout.error = context?.getString(R.string.must_be_at_least)

        else if (text.isNullOrBlank())
            textInputLayout.error = defaultEmptyMsg ?: context.getString(R.string.cant_be_empty)
        else {
            textInputLayout.error = null
            textInputLayout.isErrorEnabled = false
        }

        doOnTextChange?.invoke(if (text != null && text.isBlank()) null else text?.toString())
    }

    setOnEditorActionListener { _, _, _ ->

        val returnVal =
            if (inputType == EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS && !Patterns.EMAIL_ADDRESS.matcher(
                    text ?: ""
                ).matches()
            ) {
                textInputLayout.error = context.getString(R.string.enter_valid_email)
                true
            } else if (inputType == EditorInfo.TYPE_CLASS_PHONE && text?.length != 10) {
                textInputLayout.error = context.getString(R.string.enter_10_digit_phone_number)
                true
            }
//            else if (
//                !skippPasswordCheck &&
//                inputType == inputTypePassword &&
//                (prevEditText == null || prevEditText.inputType != inputTypePassword) &&
//                (text ?: "").length < 8
//            ) {
//                textInputLayout.error = context?.getString(R.string.must_be_at_least)
//                true
//            }
//            else if (!skippPasswordCheck &&
//                inputType == inputTypePassword &&
//                prevEditText?.inputType == inputTypePassword &&
//                text?.toString()?.equals(prevEditText.text?.toString()) == false
//            ) {
//                textInputLayout.error = context?.getString(R.string.password_error)
//                true
//            }

            else if (text.isNullOrBlank()) {
                textInputLayout.error = context.getString(R.string.cant_be_empty)
                true
            } else {
                textInputLayout.error = null
                textInputLayout.isErrorEnabled = false
                false
            }

        returnVal
    }
}