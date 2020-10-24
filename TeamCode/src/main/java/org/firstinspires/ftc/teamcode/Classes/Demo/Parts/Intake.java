package org.firstinspires.ftc.teamcode.Classes.Demo.Parts;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewDcMotor;
import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewServo;
import org.firstinspires.ftc.teamcode.Classes.RobotPart;
import org.json.JSONException;
import org.json.JSONObject;

public class Intake extends RobotPart {
    public double stepSize = 0.5;

    public Intake() {
        super.name = "Intake";
    }

    @Override
    public void initJSON(JSONObject jsonObj) throws JSONException {
        super.initJSON(jsonObj);
        try {
            stepSize = jsonObj.getDouble("stepSize");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public JSONObject getSettings() throws JSONException {
        JSONObject settings = super.getSettings();
        settings.put("stepSize", stepSize);
        return settings;
    }

    public void extendArm() {
        for (NewServo servo : servos) {
            servo.setPositionMax();
        }
    }
    public void contractArm() {
        for (NewServo servo : servos) {
            servo.setPositionMin();
        }
    }

    public void incrementUp() {
        for (NewServo servo : servos) {
            servo.increment(stepSize);
        }
    }
    public void incrementDown() {
        for (NewServo servo : servos) {
            servo.increment(-stepSize);
        }
    }

    public void intake() {
        for (NewDcMotor motor : motors) {
            motor.setPowerMax();
        }
    }
    public void eject() {
        for (NewDcMotor motor : motors) {
            // Reverse
            motor.setPowerMin();
        }
    }
    public void stopMotors() {
        for (NewDcMotor motor : motors) {
            motor.setPower(0);
        }
    }
}
