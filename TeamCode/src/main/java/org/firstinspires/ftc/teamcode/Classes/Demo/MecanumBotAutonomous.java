package org.firstinspires.ftc.teamcode.Classes.Demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Mecanum Red Foundation Auto Original", group="Mecanum Op")
public class MecanumBotAutonomous extends LinearOpMode {
    MecanumBot robot = new MecanumBot();
    private static double DRIVE_SPEED = 0.3;
    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        robot.drivetrain.autonomous.strafe(DRIVE_SPEED, -4, 3, opModeIsActive());//encoderDrive(DRIVE_SPEED, -4, 4, 4, -4, 3.0);
        // Robot starts backwards because the foundation servos are at the back of the robot
        robot.drivetrain.autonomous.move(DRIVE_SPEED,  -33, 6.0, opModeIsActive());  // S1: Backwards 33 inches, 6 second timeout
        // Close servos
        robot.foundationClaw.close();
        sleep(2000);
        robot.drivetrain.autonomous.move(DRIVE_SPEED, 30,6.0, opModeIsActive());
        // Open servos
        robot.foundationClaw.open();
        sleep(1000);
        // Strafe "right" just outside the foundation
        robot.drivetrain.autonomous.strafe(DRIVE_SPEED, 34, 5.0, opModeIsActive());

        // Move backwards 18 inches to be right next to the foundation
        robot.drivetrain.autonomous.move(DRIVE_SPEED, -18, 5.0, opModeIsActive());

        // Strafe left to put foundation in
        robot.drivetrain.autonomous.strafe(1, -22, 5.0, opModeIsActive());

        // Strafe "right" toward the line
        robot.drivetrain.autonomous.strafe(1, 35, 6.0, opModeIsActive());
    }
}
