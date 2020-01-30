package org.firstinspires.ftc.teamcode.Mecanum;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

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

@Autonomous(name="Mecanum Red Foundation Auto", group="Mecanum Op")
// @Disabled
public class MecRedFoundation extends LinearOpMode {

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

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

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
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        encoderDrive(DRIVE_SPEED,  -33,  -33, -33, -33, 6.0);  // S1: Backwards 33 inches, 6 second timeout
        // Close servos
        robot.leftFoundationServo.setPosition(0.8);
        robot.rightFoundationServo.setPosition(0.8);
        sleep(2000);
        encoderDrive(DRIVE_SPEED, 30, 30, 30, 30, 6.0);
        robot.leftFoundationServo.setPosition(0.0);
        robot.rightFoundationServo.setPosition(0.0);
        sleep(1000);
        encoderDrive(1, -50, 45, 45, -45, 6.0);
        telemetry.addData("Path", "Complete");
        telemetry.update();
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
            robot. rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            // robot.setWheelPower(Math.abs(speed));
            robot.leftFront.setPower(Math.abs(speed));
            robot.rightFront.setPower(Math.abs(speed));
            robot.leftBack.setPower(Math.abs(speed));
            robot. rightBack.setPower(Math.abs(speed));


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

            sleep(250);   // optional pause after each move
        }
    }
}
