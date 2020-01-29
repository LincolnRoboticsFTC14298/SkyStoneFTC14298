package org.firstinspires.ftc.teamcode.Mecanum;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Mecanum.MecanumBot;

@TeleOp(name = "MecanumTeleOp", group="Mecanum Op")
public class MecanumTeleOp extends LinearOpMode {
    private MecanumBot robot = new MecanumBot();

    private double leftIntakeServoPos = 0.5;
    private double rightIntakeServoPos = 0.8;
    private double intakeServoSpeed = 0.01;

    @Override
    public void runOpMode() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        waitForStart();

        while (!isStopRequested()) {
            double motorSpeed;
            double clawPos = 1;

            double speed = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double angle = Math.atan2(-1 * gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double turn = gamepad1.right_stick_x;

            final double lfPower = speed * Math.cos(angle) + turn;
            final double rfPower = speed * Math.sin(angle) - turn;
            final double lbPower = speed * Math.sin(angle) + turn;
            final double rbPower = speed * Math.cos(angle) - turn;

            robot.leftFront.setPower(lfPower);
            robot.rightFront.setPower(rfPower);
            robot.leftBack.setPower(lbPower);
            robot.rightBack.setPower(rbPower);

            // Grab foundation
            if (gamepad1.x) {
                robot.leftFoundationServo.setPosition(0.8);
                robot.rightFoundationServo.setPosition(0.8);

            }

            // Let go of foundation
            if (gamepad1.b) {
                robot.leftFoundationServo.setPosition(0);
                robot.rightFoundationServo.setPosition(0);
            }

            // Make intake go out
            if (gamepad1.y) {
                leftIntakeServoPos = Range.clip(leftIntakeServoPos - intakeServoSpeed, 0.13, 0.5);
                rightIntakeServoPos = Range.clip(rightIntakeServoPos + intakeServoSpeed, 0.8, 1);
            }

            // Make intake go in
            if (gamepad1.a) {
                leftIntakeServoPos = Range.clip(leftIntakeServoPos + intakeServoSpeed, 0.13, 0.5);
                rightIntakeServoPos = Range.clip(rightIntakeServoPos - intakeServoSpeed, 0.8, 1);
            }

            // Move intake motors to spin
            if (gamepad1.right_bumper) {
                motorSpeed = 1; // Intake assuming right motor is reversed
            }
            else if (gamepad1.left_bumper) {
                motorSpeed = -1; // Eject assuming right motor is reversed
            }
            else {
                motorSpeed = 0;
            }
            if (gamepad1.left_trigger > 0.1) {
                clawPos = Range.clip(clawPos - 0.01, .65, 1);
            }
            else if (gamepad1.right_trigger > 0.1) {
                clawPos = Range.clip(clawPos + 0.01, .65, 1);
            }

            // Update power and position
            robot.leftIntakeServo.setPosition(leftIntakeServoPos);
            robot.rightIntakeServo.setPosition(rightIntakeServoPos);

            robot.claw.setPosition(clawPos);

            robot.leftIntakeMotor.setPower(motorSpeed);
            robot.rightIntakeMotor.setPower(motorSpeed);
        }
    }
}


