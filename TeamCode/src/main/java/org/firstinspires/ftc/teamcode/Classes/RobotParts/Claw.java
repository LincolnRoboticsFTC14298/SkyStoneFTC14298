package org.firstinspires.ftc.teamcode.Classes.RobotParts;

public class Claw extends RobotPart {
    public Claw() {
        super.name = "Claw";
    }

    public void open() {
        this.servos[0].open();
    }
    public void close() {
        this.servos[0].close();
    }

}
