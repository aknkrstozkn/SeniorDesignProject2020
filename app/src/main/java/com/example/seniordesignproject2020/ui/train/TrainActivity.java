package com.example.seniordesignproject2020.ui.train;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.example.seniordesignproject2020.R;
import com.example.seniordesignproject2020.core.Circle;
import com.example.seniordesignproject2020.core.Color;
import com.example.seniordesignproject2020.core.scan_types.Train;
import com.example.seniordesignproject2020.ui.home.CameraActivity;
import com.example.seniordesignproject2020.ui.result.ResultActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class TrainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    public final static int EXPERIMENT_SURFACE_ID = 1;
    public final static int TEST_SURFACE_ID = 2;
    public Train train;

    JavaCameraView javaCameraView;
    View loading;
    Mat mRGBA, mRGBAT;
    Calendar caledar;
    public static Mat circles_;

    Button button_train;

    protected double label;

    static boolean is_task_working = false;
    public static Circle[] circles;

    static {
        if (OpenCVLoader.initDebug()) {
            Log.d("OpenCV-ERROR", "OpenCVLoader is initialized.");
        } else {
            Log.d("OpenCV-ERROR", "Cant initialize OpenCVLoader.");
        }
    }

    public Mat circle_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        label = getIntent().getDoubleExtra("label", 0.0);
        train = new Train();
        circles = new Circle[2];
        circle_frame = new Mat();
        is_task_working = false;

        javaCameraView = (JavaCameraView) findViewById(R.id.train_cameraView);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.GONE);

        button_train = findViewById(R.id.train_button);
        button_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_task_working = true;
                loading.setVisibility(View.VISIBLE);
                button_train.setVisibility(View.GONE);
                new AsyncUploadTrain().execute(circle_frame);
            }
        });

        circles_ = new Mat();

        javaCameraView.disableView();
        javaCameraView.enableView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (javaCameraView != null)
            javaCameraView.disableView();
    }

    public HashMap<Integer, ArrayList<double[]>> circle_coordinates() {
        ArrayList<double[]> coordinates_list = new ArrayList<>();
        HashMap<Integer, ArrayList<double[]>> circle_coordinates = new HashMap<>();

        Arrays.sort(circles, (a, b) -> (int) (a.center_y - b.center_y));

        int surface_id = 0;
        for (Circle circle : circles) {
            double rect_start_x = circle.center_x - circle.radius;
            double rect_start_y = circle.center_y - circle.radius;

            double rect_end_x = circle.center_x + circle.radius;
            double rect_end_y = circle.center_y + circle.radius;

            for (double x = rect_start_x; x <= rect_end_x; ++x) {
                for (double y = rect_start_y; y <= rect_end_y; ++y) {
                    double distance_x = x - circle.center_x;
                    double distance_y = y - circle.center_y;

                    double distance_sqr = distance_x * distance_x + distance_y * distance_y;
                    if (distance_sqr <= (circle.radius * circle.radius)) {
                        //double[] clr = {255.0, 0.0, 0.0, 0.0};
                        //mRGBAT.put((int)y, (int)x, clr);
                        coordinates_list.add(new double[]{y, x});
                    }
                }
            }

            circle_coordinates.put(++surface_id, coordinates_list);
            coordinates_list = new ArrayList<>();
        }

        return circle_coordinates;
    }

    public Color[] color_of_surfaces(Mat frame) {
        Color[] colors = new Color[2];
        HashMap<Integer, ArrayList<double[]>> coordinates_of_circles = circle_coordinates();

        for (int i = EXPERIMENT_SURFACE_ID; i <= TEST_SURFACE_ID; ++i) {
            double[] red_colors = new double[coordinates_of_circles.get(i).size()];
            double[] green_colors = new double[coordinates_of_circles.get(i).size()];
            double[] blue_colors = new double[coordinates_of_circles.get(i).size()];
            int index = 0;

            for (double[] coordinate : coordinates_of_circles.get(i)) {
                double[] colors_of_coordinate = frame.get((int) coordinate[0], (int) coordinate[1]);
                Log.d("colors_of_coordinate", colors_of_coordinate.toString());
                red_colors[index] = colors_of_coordinate[0];
                green_colors[index] = colors_of_coordinate[1];
                blue_colors[index] = colors_of_coordinate[2];

                ++index;
            }
            Arrays.sort(red_colors);
            Arrays.sort(green_colors);
            Arrays.sort(blue_colors);

            if (red_colors.length % 2 == 1) {
                int median_red = (int) red_colors[(red_colors.length - 1) / 2];
                int median_green = (int) green_colors[(green_colors.length - 1) / 2];
                int median_blue = (int) blue_colors[(blue_colors.length - 1) / 2];
                colors[i - 1] = new Color(median_red, median_green, median_blue);
            } else {
                int median_red = (int) ((red_colors[red_colors.length / 2] + red_colors[(red_colors.length / 2) - 1]) / 2);
                int median_green = (int) ((green_colors[green_colors.length / 2] + green_colors[(green_colors.length / 2) - 1]) / 2);
                int median_blue = (int) ((blue_colors[blue_colors.length / 2] + blue_colors[(blue_colors.length / 2) - 1]) / 2);
                colors[i - 1] = new Color(median_red, median_green, median_blue);
            }
        }
        return colors;
    }

    private class AsyncUploadTrain extends AsyncTask<Mat, Void, Void> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Mat... mats) {
            caledar = Calendar.getInstance();

            Color[] colors = color_of_surfaces(mats[0]);

            train.base_color = colors[1];
            train.test_color = colors[0];
            train.label = label;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            javaCameraView.disableView();
            Intent intent = new Intent(TrainActivity.this, TrainResultActivity.class);
            intent.putExtra("train", train);
            startActivity(intent);
            TrainActivity.this.finish();
        }
    }

    @Override
    public void onCameraViewStopped() {
        mRGBA.release();
        mRGBAT.release();
        circle_frame.release();
    }

    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == BaseLoaderCallback.SUCCESS)
                javaCameraView.enableView();
            else
                super.onManagerConnected(status);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.d("OpenCV-ERROR", "OpenCVLoader is initialized.");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        } else {
            Log.d("OpenCV-ERROR", "Cant initialize OpenCVLoader.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseLoaderCallback);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        javaCameraView.setMaxFrameSize(640, 480);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        if (!is_task_working) {
            Mat gray_frame = inputFrame.gray();

            Mat circles = new Mat();
            Imgproc.blur(gray_frame, gray_frame, new Size(7, 7), new Point(2, 2));
            Imgproc.HoughCircles(gray_frame, circles, Imgproc.CV_HOUGH_GRADIENT, 2, 100, 100, 90, 0, 1000);

            Point[] center = new Point[2];
            int[] radius = new int[2];
            if (circles.cols() == 2) {
                for (int x = 0; x < 2; ++x) {
                    double circleVec[] = circles.get(0, x);

                    if (circleVec == null) {
                        break;
                    }

                    center[x] = new Point((int) circleVec[0], (int) circleVec[1]);
                    radius[x] = (int) circleVec[2];
                    Imgproc.circle(gray_frame, center[x], radius[x], new Scalar(255, 255, 255), 2);
                }
                if(!is_task_working)
                {
                    TrainActivity.circles[0] = new Circle(center[0].x, center[0].y, radius[0]);
                    TrainActivity.circles[1] = new Circle(center[1].x, center[1].y, radius[1]);
                    circle_frame = new Mat(inputFrame.rgba(), new Range(0, inputFrame.rgba().rows()), new Range(0, inputFrame.rgba().cols()));
                }
            }
            if (!is_task_working) {
                mRGBA = inputFrame.rgba();
                mRGBAT = mRGBA.t();
                Core.flip(mRGBA.t(), mRGBAT, 1);
                Imgproc.resize(mRGBAT, mRGBAT, mRGBA.size());
            }

            circles.release();
            gray_frame.release();
        }

        return mRGBAT;
    }
}
