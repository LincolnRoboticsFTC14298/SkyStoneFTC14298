package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

@TeleOp(name = "Tetrix_Tank_2")
public class Tetrix_Tank_2 extends LinearOpMode {

    private DcMotor right_drive;
    HardwarePushbot robot   = new HardwarePushbot();
    private DcMotor left_drive;

    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {

        robot.init(hardwareMap);

        // Reverse one of the drive motors.
        // You will have to determine which motor to reverse for your robot.
        // In this example, the right motor was reversed so that positive
        // applied power makes it move the robot in the forward direction.
        right_drive.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            while (opModeIsActive()) {
                // Put loop blocks here.


                servo.setPosition(position);
                // The Y axis of a joystick ranges from -1 in its topmost position
                // to +1 in its bottommost position. We negate this value so that
                // the topmost position corresponds to maximum forward power.
                left_drive.setPower(gamepad1.left_stick_y);
                right_drive.setPower(gamepad1.right_stick_y);
                telemetry.addData("Left Pow", left_drive.getPower());
                telemetry.addData("Right Pow", right_drive.getPower());
                telemetry.update();
            }
        }
    }
}