package com.proxglobal.survey.dialog

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.proxglobal.proxads.databinding.DialogSurvey5Binding
import com.proxglobal.survey.ConfigSurvey

class Survey5Dialog(
    context: Context,
    private val binding: DialogSurvey5Binding,
    private val config: ConfigSurvey
) : SurveyDialog(context, binding.root) {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        binding.txtQuestion1.text = config.question_1
        binding.chkOption11.text = config.option_1_1
        binding.chkOption12.text = config.option_1_2
        binding.chkOption13.text = config.option_1_3

        binding.txtQuestion2.text = config.question_2
        binding.chkOption21.text = config.option_2_1
        binding.chkOption22.text = config.option_2_2
        binding.chkOption23.text = config.option_2_3
    }

    override fun onSubmit(): Boolean {
        return if ((!binding.chkOption11.isChecked
                    && !binding.chkOption12.isChecked
                    && !binding.chkOption13.isChecked)
            || (!binding.chkOption21.isChecked
                    && !binding.chkOption22.isChecked
                    && !binding.chkOption23.isChecked)
        ) {
            false
        } else {
            var answer_1 = ""
            if (binding.chkOption11.isChecked) answer_1 += "${binding.chkOption11.text}, "
            if (binding.chkOption12.isChecked) answer_1 += "${binding.chkOption12.text}, "
            if (binding.chkOption13.isChecked) answer_1 += "${binding.chkOption13.text}"

            var answer_2 = ""
            if (binding.chkOption21.isChecked) answer_2 += "${binding.chkOption21.text}, "
            if (binding.chkOption22.isChecked) answer_2 += "${binding.chkOption22.text}, "
            if (binding.chkOption23.isChecked) answer_2 += "${binding.chkOption23.text}"

            val bundle = Bundle()
            bundle.putString("event_type", "click_submit")
            bundle.putString("question_1", binding.txtQuestion1.text.toString())
            bundle.putString("answer_1", answer_1)
            bundle.putString("question_2", binding.txtQuestion2.text.toString())
            bundle.putString("answer_2", answer_2)
            firebaseAnalytics.logEvent("prox_survey", bundle)
            true
        }
    }
}