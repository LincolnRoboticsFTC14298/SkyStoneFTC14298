package org.firstinspires.ftc.teamcode.Mecanum;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


// Robot starts with claws facing the skyblocks and intake facing away from bridge
@Autonomous(name="Mecanum Encoder Test", group="Mecanum Op")
//@Disabled
public class ToLine extends LinearOpMode {
    private MecanumEncoderBot robot   = new MecanumEncoderBot();   // Use a Mecanum Encoder Bot's hardware
    private static final double     DRIVE_SPEED             = 0.3;
    private static final double     TURN_SPEED              = 0.5;
    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        robot.strafe(DRIVE_SPEED, -1, 10, opModeIsActive());
        robot.straight(DRIVE_SPEED, 33, opModeIsActive());

    }

}
