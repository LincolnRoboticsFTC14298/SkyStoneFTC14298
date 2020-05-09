package org.firstinspires.ftc.teamcode.Classes.RobotParts;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewServo;
import org.json.JSONException;
import org.json.JSONObject;

public class Flipper extends RobotPart {
    public double stepSize = 0.5;
    public NewServo servo = this.servos[0];

    public Flipper() {
        super.name = "Flipper";
    }

    @Override
    public void initJSON(JSONObject jsonObj) throws JSONException {
        stepSize = jsonObj.getDouble("stepSize");
        super.initJSON(jsonObj);
    }
    @Override
    public JSONObject getSettings() throws JSONException {
        JSONObject settings = super.getSettings();
        settings.put("stepSize", stepSize);
        return settings;
    }

    public void incrementUp() {
        servo.increment(stepSize);
    }
    public void incrementDown() {
        servo.increment(-stepSize);
    }

}
