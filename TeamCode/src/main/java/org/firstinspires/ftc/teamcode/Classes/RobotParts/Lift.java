package org.firstinspires.ftc.teamcode.Classes.RobotParts;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewDcMotor;

public class Lift extends RobotPart {

    public Lift() {
        super.name = "Lift";
    }

    public void extend() {
        for (NewDcMotor motor : motors) {
            motor.setToMaxPower();
        }
    }
    public void contract() {
        for (NewDcMotor motor : motors) {
            motor.setToMinPower();
        }
    }
}
