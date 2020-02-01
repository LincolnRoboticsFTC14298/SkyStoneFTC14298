package org.firstinspires.ftc.teamcode.Learning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

// Found and modified from
// https://github.com/uhs3939/SkyStone/blob/master/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/opencvSkystoneDetector.java
@Autonomous(name= "opencvSkystoneDetector", group="Learning")
// @Disabled
public class OpenCVSkystoneDetector extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    // 0 means skystone, 1 means yellow stone
    // -1 for debug, but we can keep it like this because if it works, it should change to either 0 or 255
    private static int valMid = -1;
    private static int valLeft = -1;
    private static int valRight = -1;

    private static float rectHeight = .6f/8f;
    private static float rectWidth = 1.5f/8f;

    private static float offsetX = 0f/8f; // Changing this moves the three rects and the three circles left or right, range : (-2, 2) not inclusive
    private static float offsetY = 0f/8f; // Changing this moves the three rects and circles up or down, range: (-4, 4) not inclusive

    // Moves all rectangles right or left by amount. units are in ratio to monitor
    private static float[] midPos = {4f/8f + offsetX, 4f/8f + offsetY}; // Index: 0 = col, 1 = row, so midPos[0] is the middle column
    private static float[] leftPos = {2f/8f + offsetX, 4f/8f + offsetY};
    private static float[] rightPos = {6f/8f + offsetX, 4f/8f + offsetY};

    private final int rows = 640;
    private final int cols = 480;

    // Constants for encoder calculations, from DriveByEncoder
    // See https://github.com/OpenFTC/EasyOpenCV
    // And https://github.com/OpenFTC/EasyOpenCV/blob/master/examples/src/main/java/org/openftc/easyopencv/examples/InternalCameraExample.java
    // For basic EasyOpenCV
    private OpenCvCamera phoneCam;

    @Override
    public void runOpMode() throws InterruptedException {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice(); // Open camera
        phoneCam.setPipeline(new StageSwitchingPipeline()); // Different stages
        phoneCam.startStreaming(rows, cols, OpenCvCameraRotation.UPRIGHT); // Display on Robot Controller
        //width, height
        //width = height in this case, because camera is in portrait mode.

        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            telemetry.addData("Values", valLeft + "   " + valMid + "   " + valRight);
            telemetry.addData("Height", rows);
            telemetry.addData("Width", cols);

            telemetry.update();
            sleep(100);
            // Call movement functions

        }
    }
    

    // Detection pipeline
    static class StageSwitchingPipeline extends OpenCvPipeline
    {
        // Variable name is short for yCbCr channel to Mat object
        Mat yCbCrChan2Mat = new Mat();

        Mat thresholdMat = new Mat();
        Mat all = new Mat();
        List<MatOfPoint> contoursList = new ArrayList<>();

        enum Stage
        {
            detection, // Includes outlines
        }

        private Stage stageToRenderToViewport = Stage.detection;
        private Stage[] stages = Stage.values();

        // When you tap on the screen, the screen changes from its detection methods
        @Override
        public void onViewportTapped()
        {
            /*
             * Note that this method is invoked from the UI thread
             * so whatever we do here, we must do quickly.
             */

            int currentStageNum = stageToRenderToViewport.ordinal();

            int nextStageNum = currentStageNum + 1;

            if(nextStageNum >= stages.length)
            {
                nextStageNum = 0;
            }

            stageToRenderToViewport = stages[nextStageNum];
        }

        @Override
        public Mat processFrame(Mat input)
        {
            contoursList.clear();

            // Color diff cb.
            // Lower cb = more blue = skystone = white
            // Higher cb = less blue = yellow stone = grey

            // Learn about YCrCb here: https://medium.com/breaktheloop/what-is-ycbcr-964fde85eeb3
            // And here: https://www.learnopencv.com/tag/ycrcb/

            // Convert the color of the image from RGB to YCrCb
            Imgproc.cvtColor(input, yCbCrChan2Mat, Imgproc.COLOR_RGB2YCrCb);

            // Stores the third channel (Cb) matrix into yCbCrChan2Mat
            Core.extractChannel(yCbCrChan2Mat, yCbCrChan2Mat, 2);

            // Stores the threshold matrix from Cb matrix values
            // Again, 0 shows the Skystone and 255 shows the blocks
            Imgproc.threshold(yCbCrChan2Mat, thresholdMat, 102, 255, Imgproc.THRESH_BINARY_INV);

            // Outline / Contour
            Imgproc.findContours(thresholdMat, contoursList, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
            yCbCrChan2Mat.copyTo(all); // Copies Mat object to the "all" variable
            //Imgproc.drawContours(all, contoursList, -1, new Scalar(255, 0, 0), 3, 8);//draws blue contours


            // Get values of the pixels inside the circle from the frame
            double[] pixMid = thresholdMat.get((int)(input.rows()* midPos[1]), (int)(input.cols()* midPos[0]));
            valMid = (int) pixMid[0];

            double[] pixLeft = thresholdMat.get((int)(input.rows()* leftPos[1]), (int)(input.cols()* leftPos[0]));
            valLeft = (int) pixLeft[0];

            double[] pixRight = thresholdMat.get((int)(input.rows()* rightPos[1]), (int)(input.cols()* rightPos[0]));
            valRight = (int) pixRight[0];

            // Create three points
            Point pointMid = new Point((int)(input.cols()* midPos[0]), (int)(input.rows()* midPos[1]));
            Point pointLeft = new Point((int)(input.cols()* leftPos[0]), (int)(input.rows()* leftPos[1]));
            Point pointRight = new Point((int)(input.cols()* rightPos[0]), (int)(input.rows()* rightPos[1]));

            // Draw circles on those points
            Imgproc.circle(all, pointMid,5, new Scalar( 255, 0, 0 ),1 );//draws circle
            Imgproc.circle(all, pointLeft,5, new Scalar( 255, 0, 0 ),1 );//draws circle
            Imgproc.circle(all, pointRight,5, new Scalar( 255, 0, 0 ),1 );//draws circle

            // Draw 3 rectangles
            Imgproc.rectangle(// Left position rectangle
                    all,
                    new Point(
                            input.cols()*(leftPos[0] - rectWidth / 2),
                            input.rows()*(leftPos[1] - rectHeight / 2)),
                    new Point(
                            input.cols()*(leftPos[0] + rectWidth/2),
                            input.rows()*(leftPos[1] + rectHeight/2)),
                    new Scalar(0, 255, 0), 3);

            Imgproc.rectangle(// Mid position rectangle
                    all,
                    new Point(
                            input.cols()*(midPos[0]-rectWidth/2),
                            input.rows()*(midPos[1]-rectHeight/2)),
                    new Point(
                            input.cols()*(midPos[0]+rectWidth/2),
                            input.rows()*(midPos[1]+rectHeight/2)),
                    new Scalar(0, 255, 0), 3);
            Imgproc.rectangle(// Right position rectangle
                    all,
                    new Point(
                            input.cols()*(rightPos[0]-rectWidth/2),
                            input.rows()*(rightPos[1]-rectHeight/2)),
                    new Point(
                            input.cols()*(rightPos[0]+rectWidth/2),
                            input.rows()*(rightPos[1]+rectHeight/2)),
                    new Scalar(0, 255, 0), 3);

            // Our viewport in this case should be the Robot Controller's screen
            switch (stageToRenderToViewport)
            {
                /*
                case THRESHOLD:
                {
                    return thresholdMat;
                }

                case detection:
                {
                    return all;
                }

                case RAW_IMAGE:
                {
                    return input;
                }*/

                default:
                {
                    return input;
                }
            }
        }

    }
}