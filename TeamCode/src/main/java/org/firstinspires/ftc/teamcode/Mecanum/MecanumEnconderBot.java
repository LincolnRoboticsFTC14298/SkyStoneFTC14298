package org.firstinspires.ftc.teamcode.Mecanum;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class MecanumEnconderBot extends MecanumBot {
    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    private static final double     COUNTS_PER_MOTOR_REV    = 383.6 ;    // eg: 5202 Series Yellow Jacket Planetary Gear Motor (13.7:1 Ratio, 435 RPM)
    private static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    private static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    private static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    private static final double     DRIVE_SPEED             = 0.3;
    private static final double     TURN_SPEED              = 0.5;

    private double leftIntakeServoPos = 0.5; // Closed at 0.5, opens at 0.13
    private double rightIntakeServoPos = 0.8; // Closed at 0.8, opens at 1.0
    private double clawPos = 1;

    @Override
    public void init(HardwareMap hwMap) {
        // Define and Initialize Motors
        leftFront = hwMap.get(DcMotor.class, "leftFront");
        rightFront = hwMap.get(DcMotor.class, "rightFront");
        leftBack = hwMap.get(DcMotor.class, "leftBack");
        rightBack = hwMap.get(DcMotor.class, "rightBack");

        rightIntakeMotor = hwMap.get(DcMotor.class, "rightIntakeMotor");
        leftIntakeMotor = hwMap.get(DcMotor.class, "leftIntakeMotor");

        leftFoundationServo = hwMap.get(Servo.class, "leftFoundationServo");
        rightFoundationServo = hwMap.get(Servo.class, "rightFoundationServo");

        leftIntakeServo = hwMap.get(Servo.class, "leftIntakeServo");
        rightIntakeServo = hwMap.get(Servo.class, "rightIntakeServo");

        claw = hwMap.get(Servo.class, "claw");

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        rightIntakeMotor.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        // this.setWheelPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        rightIntakeMotor.setPower(0);
        leftIntakeMotor.setPower(0);


        // Set all motors to RUN_USING_ENCODERS if encoders are installed.
        // this.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set the Servo to its starting positions
        leftIntakeServo.setPosition(leftIntakeServoPos);
        rightIntakeServo.setPosition(rightIntakeServoPos);
        claw.setPosition(clawPos);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
            // robot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            // robot.setWheelPower(Math.abs(speed));
            leftFront.setPower(Math.abs(speed));
            rightFront.setPower(Math.abs(speed));
            leftBack.setPower(Math.abs(speed));
            rightBack.setPower(Math.abs(speed));


            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            /*
            while (opModeActive &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftFront.isBusy() && robot.rightFront.isBusy() && robot.leftBack.isBusy() && robot.rightBack.isBusy())
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
                telemetry.update();
            }
             */

            // Stop all motion
            // robot.setWheelPower(0);
            leftFront.setPower(0);
            rightFront.setPower(0);
            leftBack.setPower(0);
            rightBack.setPower(0);

            // Turn off RUN_TO_POSITION
            // robot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
