package com.example.seniordesignproject2020.ui.train;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seniordesignproject2020.R;
import com.example.seniordesignproject2020.core.Scan;
import com.example.seniordesignproject2020.core.database.DataBase;
import com.example.seniordesignproject2020.core.scan_types.Train;
import com.example.seniordesignproject2020.ui.result.ResultActivity;
import com.example.seniordesignproject2020.util.ShareUtil;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.Date;

public class TrainResultActivity extends AppCompatActivity {

    Train train;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_result);

        train = getIntent().getParcelableExtra("train");
        if(train == null) {
            finish();
            return;
        }

        TextView result = findViewById(R.id.train_label);
        final ImageButton save = findViewById(R.id.train_save);

        result.setText(train.base_color.toString() + " | "
                + train.test_color.toString() + " \n "
                + train.label);
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                DataBase.getInstance(TrainResultActivity.this).add_train_table(train);
                Snackbar.make(view, "Saved to the database", Snackbar.LENGTH_LONG).show();
                save.setOnClickListener(null);
                save.setImageAlpha(128);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
