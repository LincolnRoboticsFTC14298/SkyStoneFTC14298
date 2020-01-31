package org.firstinspires.ftc.teamcode.Mecanum;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Learning.OpenCVSkystoneDetector;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="MecRedQuarry", group="Mecanum Op")
// @Disabled
public class MecRedQuarry extends LinearOpMode {

    /* Declare OpMode members. */
    private MecanumBot robot   = new MecanumBot();   // Use a Mecanum Bot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    private static final double     COUNTS_PER_MOTOR_REV    = 383.6 ;    // eg: 5202 Series Yellow Jacket Planetary Gear Motor (13.7:1 Ratio, 435 RPM)
    private static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    private static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    private static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    private static final double     DRIVE_SPEED             = 0.3;
    private static final double     TURN_SPEED              = 0.5;

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
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice(); // Open camera
        phoneCam.setPipeline(new MecRedQuarry.StageSwitchingPipeline()); // Different stages
        phoneCam.startStreaming(rows, cols, OpenCvCameraRotation.UPRIGHT); // Display on Robot Controller
        //width, height
        //width = height in this case, because camera is in portrait mode.

        runtime.reset();

        telemetry.addData("Values", valLeft + "   " + valMid + "   " + valRight);
        telemetry.addData("Height", rows);
        telemetry.addData("Width", cols);

        telemetry.update();
        // Call movement functions

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        robot.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot. rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot. rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Encoder Reset",  "Starting at lf: %7d, rf: %7d, lb: %7d, rb: %7d",
                robot.leftFront.getCurrentPosition(),
                robot.rightFront.getCurrentPosition(),
                robot.leftBack.getCurrentPosition(),
                robot.rightBack.getCurrentPosition()
        );
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        // Assume that the phone lines up with the middle block to detect the blocks
        waitForStart();
        /*
        // For a single block auto
        if (valLeft == 0) { // If the left block is the skystone
            encoderDrive(DRIVE_SPEED, -3, -3, -3, -3, 2.0); // Line the claw up with the blocks instead of phone
            encoderDrive(DRIVE_SPEED, -8, -8, -8, -8, 2.0); // Line the claw up with the leftmost block
            encoderDrive(DRIVE_SPEED, -31, 31, 31, -31, 5.0);  // Strafe left toward a block
            robot.claw.setPosition(0.65); // Bring down claw servo on block
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 10, -10, -10, 10, 5.0); // Strafe right to make space to move through middle
            encoderDrive(DRIVE_SPEED, -52, -52, -52, -52, 5.0); // Move backwards 52 inches.
            robot.claw.setPosition(1.0); // Open Claw
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 22, 22, 22, 22, 5.0); // Park
            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
        else if (valMid == 0) { // If the middle block has the skystone
            // Step through each leg of the path,
            // Note: Reverse movement is obtained by setting a negative distance (not speed)
            encoderDrive(DRIVE_SPEED, -3, -3, -3, -3, 2.0); // Line the claw up with the block
            encoderDrive(DRIVE_SPEED, -31, 31, 31, -31, 5.0);  // Strafe left toward a block
            robot.claw.setPosition(0.65); // Bring down claw servo on block
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 10, -10, -10, 10, 5.0); // Strafe right to make space to move through middle
            encoderDrive(DRIVE_SPEED, -60, -60, -60, -60, 5.0); // Move backwards 5 ft.
            robot.claw.setPosition(1.0); // Open Claw
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 22, 22, 22, 22, 5.0); // Park
            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
        else if (valRight == 0) {
            encoderDrive(DRIVE_SPEED, -3, -3, -3, -3, 2.0); // Line the claw up with the block
            encoderDrive(DRIVE_SPEED, 8, 8, 8, 8, 2.0); // Line the claw up with the leftmost block
            encoderDrive(DRIVE_SPEED, -31, 31, 31, -31, 5.0);  // Strafe left toward a block
            robot.claw.setPosition(0.65); // Bring down claw servo on block
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 10, -10, -10, 10, 5.0); // Strafe right to make space to move through middle
            encoderDrive(DRIVE_SPEED, -62, -62, -62, -62, 5.0); // Move backwards 62 inches.
            robot.claw.setPosition(1.0); // Open Claw
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 22, 22, 22, 22, 5.0); // Park
            telemetry.addData("Path", "Complete");
            telemetry.update();
        }*/
        if (valLeft == 0) { // If the left block is the skystone
            encoderDrive(DRIVE_SPEED, -3, -3, -3, -3, 2.0); // Line the claw up with the blocks instead of phone
            encoderDrive(DRIVE_SPEED, -8, -8, -8, -8, 2.0); // Line the claw up with the leftmost block
            encoderDrive(DRIVE_SPEED, -31, 31, 31, -31, 5.0);  // Strafe left toward a block
            robot.claw.setPosition(0.65); // Bring down claw servo on block
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 10, -10, -10, 10, 5.0); // Strafe right to make space to move through middle
            encoderDrive(DRIVE_SPEED, -52, -52, -52, -52, 5.0); // Move backwards 52 inches.
            robot.claw.setPosition(1.0); // Open Claw
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 76, 76, 76, 76, 8.0); // Go to the other stone
            encoderDrive(DRIVE_SPEED, -10, 10, 10, -10, 5.0); // Strafe left to prepare to take the block
            robot.claw.setPosition(0.65); // Bring down claw servo on block
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 10, -10, -10, 10, 5.0); // Strafe right to make space to move through middle
            encoderDrive(DRIVE_SPEED, -76, -76, -76, -76, 8.0); // Move backwards 76 in.
            robot.claw.setPosition(1.0); // Open Claw
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 22, 22, 22, 22, 5.0); // Park
            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
        else if (valMid == 0) { // If the middle block has the skystone
            // Step through each leg of the path,
            // Note: Reverse movement is obtained by setting a negative distance (not speed)
            encoderDrive(DRIVE_SPEED, -3, -3, -3, -3, 2.0); // Line the claw up with the block
            encoderDrive(DRIVE_SPEED, -31, 31, 31, -31, 5.0);  // Strafe left toward a block
            robot.claw.setPosition(0.65); // Bring down claw servo on block
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 10, -10, -10, 10, 5.0); // Strafe right to make space to move through middle
            encoderDrive(DRIVE_SPEED, -60, -60, -60, -60, 8.0); // Move backwards 5 ft.
            robot.claw.setPosition(1.0); // Open Claw
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 84, 84, 84, 84, 9.0); // Go to the other stone
            encoderDrive(DRIVE_SPEED, -10, 10, 10, -10, 5.0); // Strafe left to prepare to take the block
            robot.claw.setPosition(0.65); // Bring down claw servo on block
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 10, -10, -10, 10, 5.0); // Strafe right to make space to move through middle
            encoderDrive(DRIVE_SPEED, -84, -84, -84, -84, 9.0); // Move backwards 84 in.
            robot.claw.setPosition(1.0); // Open Claw
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 22, 22, 22, 22, 5.0); // Park
            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
        else if (valRight == 0) { // If the right block is the skystone, score with it and a regular stone
            encoderDrive(DRIVE_SPEED, -3, -3, -3, -3, 2.0); // Line the claw up with the block
            encoderDrive(DRIVE_SPEED, 8, 8, 8, 8, 2.0); // Line the claw up with the leftmost block
            encoderDrive(DRIVE_SPEED, -31, 31, 31, -31, 5.0);  // Strafe left toward a block
            robot.claw.setPosition(0.65); // Bring down claw servo on block
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 10, -10, -10, 10, 5.0); // Strafe right to make space to move through middle
            encoderDrive(DRIVE_SPEED, -62, -62, -62, -62, 5.0); // Move backwards 62 inches.
            robot.claw.setPosition(1.0); // Open Claw
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 46, 46, 46, 46, 9.0); // Go to the other stone
            encoderDrive(DRIVE_SPEED, -10, 10, 10, -10, 5.0); // Strafe left to prepare to take the block
            robot.claw.setPosition(0.65); // Bring down claw servo on block
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 10, -10, -10, 10, 5.0); // Strafe right to make space to move through middle
            encoderDrive(DRIVE_SPEED, -46, -46, -46, -46, 9.0); // Move backwards 46 in.
            robot.claw.setPosition(1.0); // Open Claw
            sleep(1000);
            encoderDrive(DRIVE_SPEED, 22, 22, 22, 22, 5.0); // Park
            telemetry.addData("Path", "Complete");
            telemetry.update();
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
            THRESHOLD, // Black & White
            RAW_IMAGE, // Displays raw view
            yCbCr
        }

        private MecRedQuarry.StageSwitchingPipeline.Stage stageToRenderToViewport = MecRedQuarry.StageSwitchingPipeline.Stage.detection;
        private MecRedQuarry.StageSwitchingPipeline.Stage[] stages = MecRedQuarry.StageSwitchingPipeline.Stage.values();

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
                }

                default:
                {
                    return input;
                }
            }
        }

    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    private void encoderDrive(double speed,
                             double lfInches,
                             double rfInches,
                             double lbInches,
                             double rbInches,
                             double timeoutS) {
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            int newLfTarget = robot.leftFront.getCurrentPosition() + (int)(lfInches * COUNTS_PER_INCH);
            int newRfTarget = robot.rightFront.getCurrentPosition() + (int)(rfInches * COUNTS_PER_INCH);
            int newLbTarget = robot.leftBack.getCurrentPosition() + (int)(lbInches * COUNTS_PER_INCH);
            int newRbTarget = robot.rightBack.getCurrentPosition() + (int)(rbInches * COUNTS_PER_INCH);

            robot.leftFront.setTargetPosition(newLfTarget);
            robot.rightFront.setTargetPosition(newRfTarget);
            robot.leftBack.setTargetPosition(newLbTarget);
            robot.rightBack.setTargetPosition(newRbTarget);

            // Turn On RUN_TO_POSITION
            // robot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            // robot.setWheelPower(Math.abs(speed));
            robot.leftFront.setPower(Math.abs(speed));
            robot.rightFront.setPower(Math.abs(speed));
            robot.leftBack.setPower(Math.abs(speed));
            robot.rightBack.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftFront.isBusy() && robot.rightFront.isBusy() && robot.leftBack.isBusy() && robot.rightBack.isBusy())
            ) {

                // Display it for the driver.
                telemetry.addData("Target",  "Running to lf: %7d, rf: %7d, lb: %7d, rb: %7d",
                        newLfTarget,
                        newRfTarget,
                        newLbTarget,
                        newRbTarget
                );
                telemetry.addData("Currently",  "Running at lf: %7d, rf: %7d, lb: %7d, rb: %7d",
                        robot.leftFront.getCurrentPosition(),
                        robot.rightFront.getCurrentPosition(),
                        robot.leftBack.getCurrentPosition(),
                        robot.rightBack.getCurrentPosition()
                );
                telemetry.update();
            }

            // Stop all motion
            // robot.setWheelPower(0);
            robot.leftFront.setPower(0);
            robot.rightFront.setPower(0);
            robot.leftBack.setPower(0);
            robot.rightBack.setPower(0);

            // Turn off RUN_TO_POSITION
            // robot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
