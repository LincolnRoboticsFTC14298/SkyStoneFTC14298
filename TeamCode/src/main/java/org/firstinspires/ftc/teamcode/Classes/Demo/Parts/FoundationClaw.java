package org.firstinspires.ftc.teamcode.Classes.Demo.Parts;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewServo;
import org.firstinspires.ftc.teamcode.Classes.RobotPart;

public class FoundationClaw extends RobotPart {
    public FoundationClaw() {
        super.name = "FoundationClaw";
    }

    public void open() {
        for (NewServo servo : servos) {
            servo.setPositionMax();
        }
    }
    public void close() {
        for (NewServo servo : servos) {
            servo.setPositionMin();
        }
    }
}
