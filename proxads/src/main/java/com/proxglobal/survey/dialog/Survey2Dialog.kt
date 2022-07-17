package com.proxglobal.survey.dialog

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.proxglobal.proxads.databinding.DialogSurvey2Binding
import com.proxglobal.survey.ConfigSurvey

class Survey2Dialog(
    context: Context,
    private val binding: DialogSurvey2Binding,
    private val config: ConfigSurvey
) : SurveyDialog(context, binding.root) {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        binding.txtQuestion1.text = config.question_1
        binding.chkOption1.text = config.option_1_1
        binding.chkOption2.text = config.option_1_2
        binding.chkOption3.text = config.option_1_3
        binding.chkOption4.text = config.option_1_4

        binding.txtQuestion2.text = config.question_2
        binding.txtAnswer2.hint = config.hint_answer_2
    }

    override fun onSubmit(): Boolean {
        return if ((!binding.chkOption1.isChecked
                    && !binding.chkOption2.isChecked
                    && !binding.chkOption3.isChecked
                    && !binding.chkOption4.isChecked)
            || binding.txtAnswer2.text.trim().isEmpty()
        ) {
            false
        } else {
            var answer_1 = ""
            if (binding.chkOption1.isChecked) answer_1 += "${binding.chkOption1.text}, "
            if (binding.chkOption2.isChecked) answer_1 += "${binding.chkOption2.text}, "
            if (binding.chkOption3.isChecked) answer_1 += "${binding.chkOption3.text}, "
            if (binding.chkOption4.isChecked) answer_1 += "${binding.chkOption4.text}"

            val bundle = Bundle()
            bundle.putString("event_type", "click_submit")
            bundle.putString("question_1", binding.txtQuestion1.text.toString())
            bundle.putString("answer_1", answer_1)
            bundle.putString("question_2", binding.txtQuestion2.text.toString())
            bundle.putString("answer_2", binding.txtAnswer2.text.trim().toString())
            firebaseAnalytics.logEvent("prox_survey", bundle)
            true
        }
    }
}