package org.firstinspires.ftc.teamcode.Mecanum;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


// Robot starts with claws facing the skyblocks and intake facing away from bridge
@Autonomous(name="Mecanum Encoder To Line Blue", group="Mecanum Op")
//@Disabled
public class ToLineBlue extends LinearOpMode {
    private MecanumEncoderBot robot   = new MecanumEncoderBot();   // Use a Mecanum Encoder Bot's hardware
    private static final double     DRIVE_SPEED             = 0.3;
    private static final double     TURN_SPEED              = 0.5;
    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        robot.strafe(DRIVE_SPEED, 25, 5, opModeIsActive());
        robot.straight(DRIVE_SPEED, -35, 5, opModeIsActive());

    }

}
