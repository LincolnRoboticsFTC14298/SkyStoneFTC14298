package org.firstinspires.ftc.teamcode.Mecanum;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


// Robot starts with claws facing the skyblocks and intake facing away from bridge
@Autonomous(name="Mec Drive Straight Back", group="Mecanum Op")
//@Disabled
public class driveBackwardsAuto extends LinearOpMode {
    private MecanumEncoderBot robot   = new MecanumEncoderBot();   // Use a Mecanum Encoder Bot's hardware
    private static final double     DRIVE_SPEED             = 0.3;
    private static final double     TURN_SPEED              = 0.5;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        robot.straight(DRIVE_SPEED, -26, 5, opModeIsActive());
    }

}
