package com.example.seniordesignproject2020.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.example.seniordesignproject2020.R;
import com.example.seniordesignproject2020.core.CSVWriter;
import com.example.seniordesignproject2020.core.database.DataBase;
import com.example.seniordesignproject2020.core.scan_types.Train;
import com.example.seniordesignproject2020.ui.train.TrainActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HomeFragment extends Fragment {
    ImageButton openCameraButton = null;

    ConstraintLayout train_layout;
    EditText label_text;
    ImageButton open_train_camera;

    Button button_create_csv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        train_layout = root.findViewById(R.id.train_layout);
        train_layout.setVisibility(View.GONE);

        label_text = root.findViewById(R.id.edit_text_label);
        open_train_camera = root.findViewById(R.id.open_train_camera);
        openCameraButton = root.findViewById(R.id.open_camera);

        open_train_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCameraIntent = new Intent(getContext(), TrainActivity.class);
                openCameraIntent.putExtra("label", Double.parseDouble(label_text.getText().toString()));
                getActivity().startActivity(openCameraIntent);
            }
        });


        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCameraIntent = new Intent(getContext(), CameraActivity.class);
                getActivity().startActivity(openCameraIntent);

            }
        });

        button_create_csv = root.findViewById(R.id.button_create_csv);
        button_create_csv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    exportDB();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        scaleUp();
        return root;
    }

    private void exportDB() throws IOException {

        File exportDir = new File(getContext().getFilesDir(), "csv");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "train_data.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

            String[] column_names = DataBase.getInstance(getContext()).get_train_table_column_names();
            csvWrite.writeNext(column_names);
            Train[] trains = DataBase.getInstance(getContext()).get_train_table();
            for(Train train : trains)
            {
                String[] item = { String.valueOf(train.base_color.red)
                    , String.valueOf(train.base_color.green)
                    , String.valueOf(train.base_color.blue)
                    , String.valueOf(train.test_color.red)
                    , String.valueOf(train.test_color.green)
                    , String.valueOf(train.test_color.blue)
                    , String.valueOf(train.label)};

                csvWrite.writeNext(item);
            }

            csvWrite.close();
        }
        catch(Exception e)
        {
            throw e;
        }
    }

    @Override
    public void onStop() {
        openCameraButton.clearAnimation();
        super.onStop();
    }

    private void doScale(float scale, Runnable onEnd) {
        openCameraButton.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(400)
                .setInterpolator(new FastOutSlowInInterpolator())
                .withEndAction(onEnd)
                .start();
    }

    private void scaleUp() {
        doScale(1.2f, new Runnable() {
            @Override
            public void run() {
                scaleDown();
            }
        });
    }

    private void scaleDown() {
        doScale(1.0f, new Runnable() {
            @Override
            public void run() {
                scaleUp();
            }
        });
    }
}
