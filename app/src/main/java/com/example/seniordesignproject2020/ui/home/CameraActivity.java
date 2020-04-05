package com.example.seniordesignproject2020.ui.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.seniordesignproject2020.core.Color;
import com.example.seniordesignproject2020.core.Scan;
import com.example.seniordesignproject2020.core.database.DataBase;
import com.example.seniordesignproject2020.core.scan_types.RedColorScan;
import com.example.seniordesignproject2020.core.scan_types.ScanType;
import com.example.seniordesignproject2020.ui.gallery.GalleryFragment;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import static java.security.AccessController.getContext;
import static java.sql.Types.NULL;

public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

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

    static
    {
        if(OpenCVLoader.initDebug()){
            Log.d("OpenCV-ERROR","OpenCVLoader is initialized.");
        }
        else
        {
            Log.d("OpenCV-ERROR","Cant initialize OpenCVLoader.");
        }
    }

    private String get_image(Mat frame)
    {
        if(!FILE_PATH.exists()){
            if(!FILE_PATH.mkdir()) {
                Log.d("de", "could not create file");
            }
        }
        File image_path = new File(FILE_PATH, "image_" + count + ".jpg");
        boolean a = Imgcodecs.imwrite(image_path.getAbsolutePath(), frame);
        Uri image_uri = Uri.fromFile(image_path);

        return image_uri.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        FILE_PATH = new File(this.getFilesDir(), "Scans");

        button_scan = findViewById(R.id.button_scan);
        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.setVisibility(View.VISIBLE);
                button_scan.setVisibility(View.GONE);
                new AsyncImageProcess().execute(mRGBAT);
                javaCameraView.disableView();

            }
        });

        circles_ = new Mat();

        javaCameraView = (JavaCameraView) findViewById(R.id.cameraView);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);

        javaCameraView.disableView();
        javaCameraView.enableView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (javaCameraView != null)
            javaCameraView.disableView();
    }

    private class AsyncImageProcess extends AsyncTask<Mat, Void, Void>{

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Mat... mats) {
            caledar = Calendar.getInstance();

            Color surface_color = new Color(255, 255, 255);
            Color test_color = new Color(255, 0, 0);

            scan_type = new RedColorScan(surface_color, test_color);
            scan = new Scan(NULL, System.currentTimeMillis() / 1000, scan_type,
                    get_image(mats[0]), "0.32 Glucose");
            DataBase.getInstance(CameraActivity.this).add_red_scan(scan_type, scan);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // todo  Intent openResultIntent = Intent(CameraActivity.this, f)
            setResult(10);
            CameraActivity.this.finish();
        }
    }

    static private class HoughCircleTransformTask
            extends AsyncTask<Mat, Void, Mat> {

        @Override
        protected Mat doInBackground(Mat... mats) {
            Mat circles = new Mat();

            Imgproc.HoughCircles(mats[0], circles, Imgproc.CV_HOUGH_GRADIENT, 2, 100, 100, 90, 0, 1000);

            // returns number of circular objects found
            // then display it from onPostExecute()
            return circles;
        }

        @Override
        protected void onPostExecute(Mat circles){

            if (circles.cols() > 0) {
                circles_ = circles;
            }

        }
    }

    @Override
    public void onCameraViewStopped() {
        mRGBA.release();
    }

    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if(status == BaseLoaderCallback.SUCCESS)
                javaCameraView.enableView();
            else
                super.onManagerConnected(status);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()){
            Log.d("AAAAAAAAAA","AAAAAAAAAAAAAAAAA");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
        else
        {
            Log.d("BBBBBBBBBBBBBBBBBBBBB","BBBBBBBBBBBBBBBBBBBB");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseLoaderCallback);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    int count = 0;
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        //Imgproc.GaussianBlur(mRGBA, mRGBAT, new Size(100, 100), 1.0);
        input = inputFrame.gray();
        //Imgproc.blur(input, input, new Size(7, 7), new Point(2, 2));

        //task = new HoughCircleTransformTask();
        //task.execute(input);
        count++;
        /**
        if (circles_.cols() > 0) {
            for (int x=0; x < Math.min(circles_.cols(), 2); x++ ) {
                double circleVec[] = circles_.get(0, x);

                if (circleVec == null) {
                    break;
                }

                Point center = new Point((int) circleVec[0], (int) circleVec[1]);
                int radius = (int) circleVec[2];

                //Imgproc.circle(input, center, 3, new Scalar(50, 205, 50), 5);
                Imgproc.circle(input, center, radius, new Scalar(0, 255, 0), 3);
            }
        }


        /**
         if(detected_circles.size() != 0)
         {
         for(Circle index_circle : detected_circles)
         Imgproc.circle(input, index_circle.center_point, index_circle.radius, index_circle.color, 3);
         }
         **/


        //Log.i("SSSSSSSSSSSSSSSSSSSSSS", String.valueOf("size: " + circles.cols()) + ", " + String.valueOf(circles.rows()));

        //Point rect_sPoint = new Point((frame_width * 3) / 8, (frame_height * 1) / 5);
        //Point rect_ePoint = new Point((frame_width * 5) / 8, (frame_height * 4) / 5);
        //Imgproc.rectangle(input, rect_sPoint, rect_ePoint, new Scalar(50, 205, 50), 3);


        //Line 1
        /**
        Point line_sPoint = new Point((frame_width * 3) / 8, (frame_height * 2) / 5);
        Point line_ePoint = new Point((frame_width * 5) / 8, (frame_height * 2) / 5);
        Imgproc.line(input, line_sPoint, line_ePoint, new Scalar(50, 205, 50), 3);
        //Line 2
        /**
         line_sPoint = new Point((frame_width * 3) / 8, (frame_height * 3) / 5);
         line_ePoint = new Point((frame_width * 5) / 8, (frame_height * 3) / 5);
         Imgproc.line(input, line_sPoint, line_ePoint, new Scalar(50, 205, 50), 3);
         **/

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


        input.release();

        mRGBA = inputFrame.rgba();
        mRGBAT = mRGBA.t();
        Core.flip(mRGBA.t(), mRGBAT, 1);
        Imgproc.resize(mRGBAT, mRGBAT, mRGBA.size());
        return mRGBAT;
    }
}
