package com.proxglobal.lib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proxglobal.survey.ProxSurveyConfig

class MainActivity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        ProxSurveyConfig().showSurveyIfNecessary(this, BuildConfig.DEBUG)
    }
}