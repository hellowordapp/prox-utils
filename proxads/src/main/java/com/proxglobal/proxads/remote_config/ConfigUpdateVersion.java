package com.proxglobal.proxads.remote_config;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ConfigUpdateVersion {
    @SerializedName("status") public boolean status;
    @SerializedName("version_code") public int versionCode;
    @SerializedName("version_name") public String versionName;
    @SerializedName("title") public String title;
    @SerializedName("message") public String message;
    @SerializedName("version_code_required") public int[] versionCodeRequired;
    @SerializedName("required") public boolean isRequired ;
    @SerializedName("new_package") public String newPackage;
}
