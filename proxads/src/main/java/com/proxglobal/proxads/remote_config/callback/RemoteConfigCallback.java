package com.proxglobal.proxads.remote_config.callback;

import com.proxglobal.proxads.remote_config.ConfigUpdateVersion;

public interface RemoteConfigCallback {
    void onRequireUpdateConfig(ConfigUpdateVersion config);
    void onOptionsUpdateConfig(ConfigUpdateVersion config);
    void onNull();
}
