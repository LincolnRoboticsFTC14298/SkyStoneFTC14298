package org.firstinspires.ftc.teamcode.Classes.RobotParts;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewServo;

public class FoundationClaw extends RobotPart {
    public FoundationClaw() {
        super.name = "FoundationClaw";
    }

    public void close() {
        for (NewServo servo : servos) {
            servo.close();
        }
    }
    public void open() {
        for (NewServo servo : servos) {
            servo.open();
        }
    }
}
