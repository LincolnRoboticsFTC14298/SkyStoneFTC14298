package org.firstinspires.ftc.teamcode.Classes.RobotParts;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewDcMotor;
import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewServo;

public class Intake extends robotPart {
    public Intake(NewDcMotor[] motors, NewServo[] servos) {
        this.motors = motors;
        this.servos = servos;
    }

    public void extendArm() {
        for (NewServo servo : servos) {
            servo.open();
        }
    }
    public void contractArm() {
        for (NewServo servo : servos) {
            servo.close();
        }
    }

    public void intake() {
        for (NewDcMotor motor : motors) {
            motor.setMaxPower();
        }
    }
    public void extract() {
        for (NewDcMotor motor : motors) {
            // Reverse
            motor.setMinPower();
        }
    }
    public void stopMotors() {
        for (NewDcMotor motor : motors) {
            motor.motor.setPower(0);
        }
    }
}
