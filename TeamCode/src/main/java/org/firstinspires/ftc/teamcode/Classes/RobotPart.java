package org.firstinspires.ftc.teamcode.Classes;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewDcMotor;
import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewServo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class RobotPart {
    public String name = "null"; // Used for json file
    public NewDcMotor[] motors;
    public NewServo[] servos;

    public void init(NewDcMotor[] motors, NewServo[] servos) {
        this.motors = motors;
        this.servos = servos;
    }
    public void initJSON(JSONObject settings) throws JSONException {
        List<NewDcMotor> _motors = new ArrayList<NewDcMotor>();
        List<NewServo> _servos = new ArrayList<NewServo>();

        JSONArray jsonMotors = settings.getJSONArray("Motors");
        JSONArray jsonServos = settings.getJSONArray("Servos");

        for (int i = 0; i < jsonMotors.length(); i++) {
            JSONObject motorSettings = jsonMotors.getJSONObject(i);

            NewDcMotor motor = new NewDcMotor();
            motor.initJSON(motorSettings);
            _motors.add(motor);
        }

        for (int i = 0; i < jsonServos.length(); i++) {
            JSONObject servoSettings = jsonServos.getJSONObject(i);

            NewServo servo = new NewServo();
            servo.initJSON(servoSettings);
            _servos.add(servo);
        }

        init((NewDcMotor[]) _motors.toArray(), (NewServo[]) _servos.toArray());
    }

    public JSONObject getSettings() throws JSONException {
        JSONObject settings = new JSONObject();

        JSONArray jsonMotors = new JSONArray();
        JSONArray jsonServos = new JSONArray();

        for (NewDcMotor motor : motors) {
            jsonMotors.put(motor.getSettings());
        }

        for (NewServo servo : servos) {
            jsonServos.put(servo.getSettings());
        }

        settings.put("Motors", jsonMotors);
        settings.put("Servos", jsonServos);

        return settings;
    }

    public void setName(String _name) {
        name = _name;
    }

    public String getName() {
        return name;
    }
    public NewDcMotor[] getNewDcMotors() {
        return motors;
    };
    public NewServo[] getNewServos(){
        return servos;
    }
}
