package org.firstinspires.ftc.teamcode.Classes.RobotParts;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewDcMotor;
import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewServo;

public abstract class robotPart {
    public NewDcMotor[] motors;
    public NewServo[] servos;

    public NewDcMotor[] getNewDcMotors() {
        return motors;
    };
    public NewServo[] getNewServos(){
        return servos;
    }
}
