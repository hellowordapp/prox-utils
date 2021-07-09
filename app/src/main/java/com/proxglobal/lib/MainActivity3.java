package com.proxglobal.lib;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.proxglobal.rate.ProxRateDialog;
import com.proxglobal.rate.ProxRateDialog.Config;
import com.proxglobal.rate.RatingDialogListener;

public class MainActivity3 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Config config = new Config();

        config.setListener(new RatingDialogListener() {
            @Override
            public void onSubmitButtonClicked(int rate, String comment) {
                Toast.makeText(MainActivity3.this, "Submit", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLaterButtonClicked() {
                Toast.makeText(MainActivity3.this, "Later", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChangeStar(int rate) {
                Toast.makeText(MainActivity3.this, "Star change", Toast.LENGTH_SHORT).show();
            }
        });

        ProxRateDialog.init(MainActivity3.this, config);

        findViewById(R.id.btn_show_rate).setOnClickListener(v -> {
            ProxRateDialog.showAlways(getSupportFragmentManager());
        });
    }
}
