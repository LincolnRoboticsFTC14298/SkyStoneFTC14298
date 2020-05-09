package org.firstinspires.ftc.teamcode.Classes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Configure", group="Set up")
public class Config extends LinearOpMode {
    private Robot robot = new Robot();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        waitForStart();

        while (!isStopRequested()) {

        }
    }
}


