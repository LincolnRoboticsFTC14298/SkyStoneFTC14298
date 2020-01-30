package org.firstinspires.ftc.teamcode.Mecanum;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class MecanumEnconderBot {
    /* Declare OpMode members. */
    private MecanumBot robot   = new MecanumBot();   // Use a Mecanum Bot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    private static final double     COUNTS_PER_MOTOR_REV    = 383.6 ;    // eg: 5202 Series Yellow Jacket Planetary Gear Motor (13.7:1 Ratio, 435 RPM)
    private static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    private static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    private static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    private static final double     DRIVE_SPEED             = 0.3;
    private static final double     TURN_SPEED              = 0.5;

    private void init(HardwareMap hwMap) {
        robot.init(hwMap);

        robot.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot. rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot. rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void encoderDrive(double speed,
                              double lfInches,
                              double rfInches,
                              double lbInches,
                              double rbInches,
                              double timeoutS, boolean opModeActive, telemetry) {
        // Ensure that the opmode is still active
        if (opModeActive) {

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
            while (opModeActive &&
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
