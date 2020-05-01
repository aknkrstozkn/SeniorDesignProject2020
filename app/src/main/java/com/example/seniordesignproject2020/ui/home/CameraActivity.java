package com.example.seniordesignproject2020.ui.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.CoreComponentFactory;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.example.seniordesignproject2020.R;
import com.example.seniordesignproject2020.core.Circle;
import com.example.seniordesignproject2020.core.Color;
import com.example.seniordesignproject2020.core.Scan;
import com.example.seniordesignproject2020.core.database.DataBase;
import com.example.seniordesignproject2020.core.scan_types.RedColorScan;
import com.example.seniordesignproject2020.core.scan_types.ScanType;
import com.example.seniordesignproject2020.ui.gallery.GalleryFragment;
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
import java.util.List;

import static java.security.AccessController.getContext;
import static java.sql.Types.NULL;

public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    public File FILE_PATH;

    private Scan scan;
    private ScanType scan_type;

    private Calendar caledar;

    JavaCameraView javaCameraView;
    View loading;
    Mat mRGBA, mRGBAT;
    public static Mat circles_;
    public static Mat input;
    HoughCircleTransformTask task;

    Button button_scan;

    float frame_width, frame_height;

    public static Circle[] circles;

    static {
        if (OpenCVLoader.initDebug()) {
            Log.d("OpenCV-ERROR", "OpenCVLoader is initialized.");
        } else {
            Log.d("OpenCV-ERROR", "Cant initialize OpenCVLoader.");
        }
    }

    private String get_image(Mat frame) {
        if (!FILE_PATH.exists()) {
            if (!FILE_PATH.mkdir()) {
                Log.d("de", "could not create file");
            }
        }
        File image_path = new File(FILE_PATH, "image_" + System.currentTimeMillis() / 1000 + ".jpg");
        Log.d("FILE-PATH", FILE_PATH.toString());
        boolean a = Imgcodecs.imwrite(image_path.getAbsolutePath(), frame);
        Uri image_uri = Uri.fromFile(image_path);

        return image_uri.toString();
    }


    public Mat circle_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        circles = new Circle[2];
        circle_frame = new Mat();
        is_task_working = false;

        javaCameraView = (JavaCameraView) findViewById(R.id.cameraView);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        FILE_PATH = new File(this.getFilesDir(), "Scans");

        button_scan = findViewById(R.id.button_scan);
        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_task_working = true;
                loading.setVisibility(View.VISIBLE);
                button_scan.setVisibility(View.GONE);
                new AsyncImageProcess().execute(mRGBAT, circle_frame);

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

    public final static int EXPERIMENT_SURFACE_ID = 1;
    public final static int TEST_SURFACE_ID = 2;

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


            /*
            Point rect_sPoint = new Point(rect_start_x, rect_start_y);
            Point rect_ePoint = new Point(rect_end_x, rect_end_y);
            Imgproc.rectangle(mRGBAT, rect_sPoint, rect_ePoint, new Scalar(255, 0, 0), 3);
            */

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

    Color[] colors;

    private class AsyncImageProcess extends AsyncTask<Mat, Void, Void> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Mat... mats) {
            caledar = Calendar.getInstance();

            Color[] colors = color_of_surfaces(mats[1]);

            Imgproc.cvtColor(mats[0], mats[0], Imgproc.COLOR_BGRA2RGB, 4);

            Color surface_color = colors[0];
            Color test_color = colors[1];

            String result_1 = surface_color.red + "," + surface_color.green + "," + surface_color.blue;
            String result_2 = test_color.red + "," + test_color.green + "," + test_color.blue;

            scan_type = new RedColorScan(surface_color, test_color);
            scan = new Scan(NULL, System.currentTimeMillis() / 1000, scan_type,
                    get_image(mats[0]), result_1 + "--" + result_2);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            javaCameraView.disableView();
            Intent intent = new Intent(CameraActivity.this, ResultActivity.class);
            intent.putExtra("scan", scan);
            startActivity(intent);
            CameraActivity.this.finish();
        }
    }

    static private class HoughCircleTransformTask
            extends AsyncTask<Mat, Void, Mat> {

        @Override
        protected Mat doInBackground(Mat... mats) {
            Mat circles = new Mat();

            Imgproc.blur(mats[0], mats[0], new Size(7, 7), new Point(2, 2));
            Imgproc.HoughCircles(mats[0], circles, Imgproc.CV_HOUGH_GRADIENT, 2, 100, 100, 90, 0, 1000);

            // returns number of circular objects found
            // then display it from onPostExecute()
            return circles;
        }

        @Override
        protected void onPostExecute(Mat circles) {

            if (circles.cols() > 0) {
                circles_ = circles;
            }
            is_task_working = false;

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
    }

    Mat save_img;
    static boolean is_task_working = false;


    Mat transpoze_mat(Mat frame) {
        Mat return_frame = frame.t();
        Core.flip(frame.t(), return_frame, -1);
        Core.flip(return_frame.t(), return_frame, -1);
        Imgproc.resize(return_frame, return_frame, frame.size());
        return return_frame;
    }

    CameraBridgeViewBase.CvCameraViewFrame frame;
    int count = 0;

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        //Imgproc.GaussianBlur(mRGBA, mRGBAT, new Size(100, 100), 1.0);
        if (!is_task_working) {
            Mat gray_frame = inputFrame.gray();

            Mat circles = new Mat();
            //Imgproc.blur(gray_frame, gray_frame, new Size(7, 7), new Point(2, 2));
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
                    CameraActivity.circles[0] = new Circle(center[0].x, center[0].y, radius[0]);
                    CameraActivity.circles[1] = new Circle(center[1].x, center[1].y, radius[1]);
                    //Core.flip(inputFrame.rgba().t(), inputFrame.rgba(), 1);
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

        /*
        Point x_y = new Point(200, 200);
        int r = 40;

        Imgproc.circle(input, x_y, r, new Scalar(255, 255, 255), 2);

        Imgproc.circle(input, new Point(200, 0), r, new Scalar(255, 0, 0), 2);






            //double[] colors = mRGBA.get(2, 3);

            /**
             if(!is_task_working)
             {
             task = new HoughCircleTransformTask();
             task.execute(input);
             }
             **/



            /*
            if(c[0] != null && r[0] != 0)
            {
                Imgproc.circle(mRGBA, c[0], r[0] + 5, new Scalar(255, 0, 0), 3);
            }
            if(c[1] != null && r[1] != 0)
            {
                Imgproc.circle(mRGBA, c[1], r[1] + 5, new Scalar(255, 0, 0), 3);
            }
            */



                //Log.i("SSSSSSSSSSSSSSSSSSSSSS", String.valueOf("size: " + circles.cols()) + ", " + String.valueOf(circles.rows()));
        /*
        Point rect_sPoint = new Point((frame_width * 3) / 8, (frame_height * 1) / 5);
        Point rect_ePoint = new Point((frame_width * 5) / 8, (frame_height * 4) / 5);
        Imgproc.rectangle(input, rect_sPoint, rect_ePoint, new Scalar(50, 205, 50), 3);
        */

                //Line 1
        /*
        Point line_sPoint = new Point((frame_width * 3) / 8, (frame_height * 2) / 5);
        Point line_ePoint = new Point((frame_width * 5) / 8, (frame_height * 2) / 5);
        Imgproc.line(input, line_sPoint, line_ePoint, new Scalar(50, 205, 50), 3);
        //Line 2

         line_sPoint = new Point((frame_width * 3) / 8, (frame_height * 3) / 5);
         line_ePoint = new Point((frame_width * 5) / 8, (frame_height * 3) / 5);
         Imgproc.line(input, line_sPoint, line_ePoint, new Scalar(50, 205, 50), 3);
        */

        /*
        if (circles.cols() > 0) {
            for (int x=0; x < Math.min(circles.cols(), 2); x++ ) {
                double circleVec[] = circles.get(0, x);

                if (circleVec == null) {
                    break;
                }

                Point center = new Point((int) circleVec[0], (int) circleVec[1]);
                int radius = (int) circleVec[2];

                //Imgproc.circle(input, center, 3, new Scalar(50, 205, 50), 5);
                Imgproc.circle(input, center, radius, new Scalar(0, 255, 0), 3);
            }
        }
        */

            }

            return mRGBAT;
        }
    }
