package org.firstinspires.ftc.teamcode.Classes.RobotParts;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewDcMotor;
import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewServo;

public class Lift extends robotPart {
    public Lift(NewDcMotor[] motors, NewServo[] servos) {
        this.motors = motors;
        this.servos = servos;
    }

    public void extend() {
        for (NewDcMotor motor : motors) {
            motor.setMaxPower();
        }
    }
    public void contract() {
        for (NewDcMotor motor : motors) {
            motor.setMinPower();
        }
    }
}
