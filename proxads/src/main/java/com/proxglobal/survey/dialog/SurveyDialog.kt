package com.proxglobal.survey.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.Toast
import com.proxglobal.proxads.databinding.DialogSurveyBinding

abstract class SurveyDialog(context: Context, private val layout: View) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DialogSurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.80).toInt()
        window!!.setLayout(width, height)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)

        binding.layoutMain.addView(layout)

        binding.btnNotNow.setOnClickListener {
            onNotNow()
            cancel()
        }

        binding.btnSubmit.setOnClickListener {
            if (onSubmit()) {
                binding.layoutSubmitted.root.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    cancel()
                }, 1000)
            }else{
                Toast.makeText(context, "Please complete the information and then click the \"Submit\" button.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    abstract fun onSubmit(): Boolean
    abstract fun onNotNow()
}