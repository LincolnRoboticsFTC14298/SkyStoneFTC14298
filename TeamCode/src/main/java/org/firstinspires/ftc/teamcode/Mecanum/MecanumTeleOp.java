package org.firstinspires.ftc.teamcode.Mecanum;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Mecanum.MecanumBot;

@TeleOp(name = "MecanumTeleOp", group="Mecanum Op")
public class MecanumTeleOp extends LinearOpMode {
    private MecanumBot robot = new MecanumBot();

    @Override
    public void runOpMode() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        waitForStart();

        while (!isStopRequested()) {

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

            telemetry.addData("leftFront Power", robot.leftFront.getPower());
            telemetry.addData("rightFront Power", robot.rightFront.getPower());
            telemetry.addData("leftBack Power", robot.leftBack.getPower());
            telemetry.addData("rightBack Power", robot.rightBack.getPower());
            telemetry.update();
        }
    }
}


