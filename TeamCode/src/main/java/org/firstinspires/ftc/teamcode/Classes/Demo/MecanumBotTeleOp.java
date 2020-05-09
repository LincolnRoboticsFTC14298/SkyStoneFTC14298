package org.firstinspires.ftc.teamcode.Classes.Demo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "MecanumBotTeleOp", group="Mecanum Op")
public class MecanumBotTeleOp extends LinearOpMode {
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

            robot.drivetrain.teleop.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.right_stick_y);

            // Grab foundation
            if (gamepad1.x) {
                robot.foundationClaw.close();
            }

            // Let go of foundation
            if (gamepad1.b) {
                robot.foundationClaw.open();
            }

            // Make intake go out
            if (gamepad1.y) {
                robot.intake.incrementUp();
            }

            // Make intake go in
            if (gamepad1.a) {
                robot.intake.incrementDown();
            }

            // Move intake motors to spin
            if (gamepad1.right_bumper) {
                robot.intake.intake();
            }
            else if (gamepad1.left_bumper) {
                robot.intake.eject();
            }
            else if (gamepad1.left_bumper && gamepad1.right_bumper){
                robot.intake.stopMotors();
            }

            if (gamepad1.dpad_up) {
                robot.flipper.incrementUp();
            }
            if (gamepad1.dpad_down) {
                robot.flipper.incrementDown();
            }
        }
    }
}
