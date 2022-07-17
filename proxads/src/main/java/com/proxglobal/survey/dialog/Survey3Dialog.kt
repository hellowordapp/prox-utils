package com.proxglobal.survey.dialog

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.proxglobal.proxads.databinding.DialogSurvey3Binding
import com.proxglobal.survey.ConfigSurvey

class Survey3Dialog(
    context: Context,
    private val binding: DialogSurvey3Binding,
    private val config: ConfigSurvey
) : SurveyDialog(context, binding.root) {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        binding.txtQuestion.text = config.question_1
        binding.chkOption1.text = config.option_1_1
        binding.chkOption2.text = config.option_1_2
        binding.chkOption3.text = config.option_1_3
        binding.chkOption4.text = config.option_1_4
    }

    override fun onSubmit(): Boolean {
        return if (!binding.chkOption1.isChecked
            && !binding.chkOption2.isChecked
            && !binding.chkOption3.isChecked
            && !binding.chkOption4.isChecked
        ) {
            false
        } else {
            var answer = ""
            if (binding.chkOption1.isChecked) answer += "${binding.chkOption1.text}, "
            if (binding.chkOption2.isChecked) answer += "${binding.chkOption2.text}, "
            if (binding.chkOption3.isChecked) answer += "${binding.chkOption3.text}, "
            if (binding.chkOption4.isChecked) answer += "${binding.chkOption4.text}"

            val bundle = Bundle()
            bundle.putString("event_type", "click_submit")
            bundle.putString("question_1", binding.txtQuestion.text.toString())
            bundle.putString("answer_1", answer)
            firebaseAnalytics.logEvent("prox_survey", bundle)
            true
        }
    }
}