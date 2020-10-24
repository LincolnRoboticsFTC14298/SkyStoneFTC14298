package org.firstinspires.ftc.teamcode.Classes.Demo.Parts;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewDcMotor;
import org.firstinspires.ftc.teamcode.Classes.RobotPart;

public class Lift extends RobotPart {

    public Lift() {
        super.name = "Lift";
    }

    public void extend() {
        for (NewDcMotor motor : motors) {
            motor.setPowerMax();
        }
    }
    public void contract() {
        for (NewDcMotor motor : motors) {
            motor.setPowerMin();
        }
    }
}
