package org.firstinspires.ftc.teamcode.Classes;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewDcMotor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Drivetrain {
    public NewDcMotor[] motors;
    public void init(NewDcMotor[] motors){
        this.motors = motors;
    }
    public void initJSON(JSONObject settings) throws JSONException {
        List<NewDcMotor> _motors = new ArrayList<NewDcMotor>();

        JSONArray jsonMotors;

        jsonMotors = settings.getJSONArray("Motors");

        for (int i = 0; i < jsonMotors.length(); i++) {
            JSONObject motorSettings = jsonMotors.getJSONObject(i);

            NewDcMotor motor = new NewDcMotor();
            motor.initJSON(motorSettings);
            _motors.add(motor);
        }

        init((NewDcMotor[]) _motors.toArray());
    }

    public JSONObject getSettings() throws JSONException {
        JSONObject settings = new JSONObject();
        JSONArray jsonMotors = new JSONArray();

        for (NewDcMotor motor : motors) {
            jsonMotors.put(motor.getSettings());
        }

        settings.put("Motors", jsonMotors);

        return settings;
    }

    public NewDcMotor[] getNewDcMotors(){
        return motors;
    }

    public interface TeleOp {
        void move(double leftx, double lefty, double rightx, double righty);
    }

    public interface Autonomous {
        void move(double speed, double distance, double timeout, boolean opModeActive);
        void rotate(double speed, double angle, double timeout, boolean opModeActive);
    }

    public void setMode(NewDcMotor[] motors, DcMotor.RunMode mode) {
        for (NewDcMotor motor : motors) {
            motor.setMode(mode);
        }
    }

    public void setPower(NewDcMotor[] motors, double power) {
        for (NewDcMotor motor : motors) {
            motor.setPower(power);
        }
    }
}


