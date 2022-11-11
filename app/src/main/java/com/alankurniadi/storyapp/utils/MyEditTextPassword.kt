package com.alankurniadi.storyapp.utils

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.alankurniadi.storyapp.R

class MyEditTextPassword : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.label_password)
    }

    private fun init() {
        addTextChangedListener(onTextChanged = { text, _, _, _ ->
            if (text!!.length < 6) {
                error = context.getString(R.string.label_error_message_password)
            }
        })
    }
}
