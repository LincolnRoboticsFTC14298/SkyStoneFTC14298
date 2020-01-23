package org.firstinspires.ftc.teamcode.Learning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "MecDriveSample")
public class MecanumDrive extends LinearOpMode {

    private DcMotor leftFront, rightFront, leftBack, rightBack;

    @Override
    public void runOpMode() {
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        // Samples generally reverse the right side
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);

        waitForStart();

        while (!isStopRequested()) {

            double speed = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double angle = Math.atan2(-1 * gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double turn = gamepad1.right_stick_x;

            final double lfPower = speed * Math.cos(angle) + turn;
            final double rfPower = speed * Math.sin(angle) - turn;
            final double lbPower = speed * Math.sin(angle) + turn;
            final double rbPower = speed * Math.cos(angle) - turn;

            leftFront.setPower(lfPower);
            rightFront.setPower(rfPower);
            leftBack.setPower(lbPower);
            rightBack.setPower(rbPower);

            // Prints the value of each motor's power on the Driver Station
            telemetry.addData("leftFront Power", leftFront.getPower());
            telemetry.addData("rightFront Power", rightFront.getPower());
            telemetry.addData("leftBack Power", leftBack.getPower());
            telemetry.addData("rightBack Power", rightBack.getPower());
            telemetry.update();
        }
    }
}


