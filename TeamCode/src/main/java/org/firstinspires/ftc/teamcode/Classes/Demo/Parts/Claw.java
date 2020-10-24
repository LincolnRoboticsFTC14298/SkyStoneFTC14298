package org.firstinspires.ftc.teamcode.Classes.Demo.Parts;

import org.firstinspires.ftc.teamcode.Classes.RobotPart;

public class Claw extends RobotPart {
    public Claw() {
        super.name = "Claw";
    }

    public void open() {
        this.servos[0].setPositionMax();
    }
    public void close() {
        this.servos[0].setPositionMin();
    }

}
