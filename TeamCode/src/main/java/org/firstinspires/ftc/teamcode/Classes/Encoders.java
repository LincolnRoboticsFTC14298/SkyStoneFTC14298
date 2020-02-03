package org.firstinspires.ftc.teamcode.Classes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Encoders {
    private ElapsedTime runtime = new ElapsedTime();

    private static final double COUNTS_PER_MOTOR_REV;    // eg: 5202 Series Yellow Jacket Planetary Gear Motor (13.7:1 Ratio, 435 RPM)
    private static final double DRIVE_GEAR_REDUCTION;     // This is < 1.0 if geared UP
    private static final double WHEEL_DIAMETER_INCHES;     // For figuring circumference
    private static final double COUNTS_PER_INCH;
    private static final double RADIUS = Math.sqrt(13*13 + 15*15)/2; // 13 in by 15 in

    Encoders(double countsPerMotorRev, double driveGearReduction, double wheelDiameterInches) {
        COUNTS_PER_MOTOR_REV = countsPerMotorRev;
        DRIVE_GEAR_REDUCTION = driveGearReduction;
        WHEEL_DIAMETER_INCHES = wheelDiameterInches;
        COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                (WHEEL_DIAMETER_INCHES * 3.1415);
    }

    public void straight(double speed, double dist, double timeout, boolean opModeActive) {
        encoderDrive(speed, dist, dist, dist, dist, timeout, opModeActive);
    }

    public void strafe(double speed, double dist, double timeout, boolean opModeActive) {
        // If dist is neg, it strafes left, if pos, right
        dist *= 1/.935;
        // Adjust for friction. Found that the strafe was off by a tenth
        encoderDrive(speed, dist, -dist, -dist, dist, timeout, opModeActive);
    }

    public void rotate(double speed, double angle, double timeout, boolean opModeActive) {
        // Negative angle is clockwise
        // Distance is simply the circumfrance traveled
        double circum = 2*Math.PI*RADIUS*angle/360; // Full circumfrance times ratio of angle
        encoderDrive(speed, -circum, circum, -circum, circum, timeout, opModeActive);
    }

    public void encoderDrive(double speed,
                             double lfInches,
                             double rfInches,
                             double lbInches,
                             double rbInches,
                             double timeoutS, boolean opModeActive) {
        // Ensure that the opmode is still active
        if (opModeActive) {

            // Determine new target position, and pass to motor controller
            int newLfTarget = leftFront.getCurrentPosition() + (int) (lfInches * COUNTS_PER_INCH);
            int newRfTarget = rightFront.getCurrentPosition() + (int) (rfInches * COUNTS_PER_INCH);
            int newLbTarget = leftBack.getCurrentPosition() + (int) (lbInches * COUNTS_PER_INCH);
            int newRbTarget = rightBack.getCurrentPosition() + (int) (rbInches * COUNTS_PER_INCH);

            leftFront.setTargetPosition(newLfTarget);
            rightFront.setTargetPosition(newRfTarget);
            leftBack.setTargetPosition(newLbTarget);
            rightBack.setTargetPosition(newRbTarget);

            // Turn On RUN_TO_POSITION
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            runtime.reset();
            setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.

            while (opModeActive &&
                    (runtime.seconds() < timeoutS) &&
                    (leftFront.isBusy() && rightFront.isBusy() && leftBack.isBusy() && rightBack.isBusy())
            ) {
                // Display it for the driver.
                telemetry.addData("Target", "Running to lf: %7d, rf: %7d, lb: %7d, rb: %7d",
                        newLfTarget,
                        newRfTarget,
                        newLbTarget,
                        newRbTarget
                );
                telemetry.addData("Currently", "Running at lf: %7d, rf: %7d, lb: %7d, rb: %7d",
                        robot.leftFront.getCurrentPosition(),
                        robot.rightFront.getCurrentPosition(),
                        robot.leftBack.getCurrentPosition(),
                        robot.rightBack.getCurrentPosition()
                );
                Telemetry.update();
            }


            // Stop all motion
            setPower(0);
            /*
            leftFront.setPower(0);
            rightFront.setPower(0);
            leftBack.setPower(0);
            rightBack.setPower(0);
            */

            // Turn off RUN_TO_POSITION
            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            /*
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            */

            //  sleep(250);   // optional pause after each move
        }
    }
}
