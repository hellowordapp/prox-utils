package com.proxglobal.survey

import com.google.gson.Gson
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.proxglobal.proxads.databinding.*
import com.proxglobal.survey.dialog.*

class ProxSurveyConfig {
    private var fetchTime = (12 * 60 * 60).toLong()

    fun setReleaseFetchTime(fetchTime: Long) {
        this.fetchTime = fetchTime
    }

    fun showSurveyIfNecessary(activity: AppCompatActivity, isDebug: Boolean) {
        val config = FirebaseRemoteConfig.getInstance()
        var minFetch = fetchTime
        if (isDebug) {
            minFetch = 0
        }
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(minFetch).build()
        config.setConfigSettingsAsync(configSettings)
        config.fetchAndActivate().addOnCompleteListener(activity) { task: Task<Boolean?> ->
            if (!task.isSuccessful) return@addOnCompleteListener

            val json = config.getString("config_survey")
            val result = Gson().fromJson(json, ConfigSurvey::class.java)
            if (result.status) {
                showDialogSurvey(activity, result)
            }
        }
    }

    private fun showDialogSurvey(activity: Activity, config: ConfigSurvey) {
        val dialog: SurveyDialog = when(config.style){
            1 -> Survey1Dialog(activity, DialogSurvey1Binding.inflate(activity.layoutInflater), config)
            2 -> Survey2Dialog(activity, DialogSurvey2Binding.inflate(activity.layoutInflater), config)
            3 -> Survey3Dialog(activity, DialogSurvey3Binding.inflate(activity.layoutInflater), config)
            4 -> Survey4Dialog(activity, DialogSurvey4Binding.inflate(activity.layoutInflater), config)
            5 -> Survey5Dialog(activity, DialogSurvey5Binding.inflate(activity.layoutInflater), config)
            6 -> Survey6Dialog(activity, DialogSurvey6Binding.inflate(activity.layoutInflater), config)
            7 -> Survey7Dialog(activity, DialogSurvey7Binding.inflate(activity.layoutInflater), config)
            8 -> Survey8Dialog(activity, DialogSurvey8Binding.inflate(activity.layoutInflater), config)
            else -> return
        }
        dialog.show()
    }
}