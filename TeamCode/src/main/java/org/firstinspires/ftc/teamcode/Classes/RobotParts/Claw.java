package org.firstinspires.ftc.teamcode.Classes.RobotParts;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewServo;

public class Claw extends robotPart{

    public Claw(NewServo[] servo) {
        this.servos = servo;
    }

    public void open() {
        this.servos[0].open();
    }
    public void close() {
        this.servos[0].close();
    }

}
