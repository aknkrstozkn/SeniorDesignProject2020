package com.example.seniordesignproject2020.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.example.seniordesignproject2020.R;

public class HomeFragment extends Fragment {
    ImageButton openCameraButton = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        openCameraButton = root.findViewById(R.id.open_camera);
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo Intent openCameraIntent = new Intent(getContext(), TakePicture.class);
                // todo getActivity().startActivity(openCameraIntent);
            }
        });

        scaleUp();
        return root;
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
