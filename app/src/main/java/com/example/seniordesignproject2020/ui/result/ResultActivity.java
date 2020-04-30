package com.example.seniordesignproject2020.ui.result;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.seniordesignproject2020.core.scan_types.ScanType;
import com.example.seniordesignproject2020.util.ShareUtil;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {

    Scan scan;
    boolean is_saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        is_saved = false;
        scan = getIntent().getParcelableExtra("scan");
        if(scan == null) {
            finish();
            return;
        }

        ImageView image = findViewById(R.id.image);
        TextView date = findViewById(R.id.date);
        TextView result = findViewById(R.id.label);
        final ImageButton save = findViewById(R.id.save);
        ImageButton share = findViewById(R.id.share);

        image.setImageURI(Uri.parse(scan.image_location));
        date.setText(new Date(scan.date * 1000).toString());
        result.setText(scan.scan_result);
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                DataBase.getInstance(ResultActivity.this).add_red_scan(scan.scan_type, scan);
                Snackbar.make(view, "Saved to the database", Snackbar.LENGTH_LONG).show();
                save.setOnClickListener(null);
                save.setImageAlpha(128);
                is_saved = true;
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtil.shareScan(ResultActivity.this,
                        scan.image_location, scan.scan_result);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(!is_saved)
        {
            File file = new File(Uri.parse(scan.image_location).getPath());
            if(file.exists())
                file.delete();
        }

    }
}
