package org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Servo.Direction;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.Range;

import org.json.JSONException;
import org.json.JSONObject;

public class NewServo implements HardwareDevice {
    private Servo servo;

    private String name = "null";
    private double defaultPos = 0;
    private double maxPos = 1;
    private double minPos = 0;
    private boolean isReversed;

    public void init(String _name, double _defaultPos, double _maxPos, double _minPos, boolean _isReversed) {
        name = _name;
        defaultPos = _defaultPos;
        maxPos = _maxPos;
        minPos = _minPos;
        isReversed = _isReversed;
        checkIfReversed();
    }
    public void initJSON(JSONObject settings) throws JSONException {
        String _name = settings.getString("name");
        double _defaultPos = settings.getDouble("defaultPower");
        double _maxPos = settings.getDouble("maxPos");
        double _minPos = settings.getDouble("minPos");
        Boolean _isReversed = settings.getBoolean("isReversed");

        init(_name, _defaultPos, _maxPos, _minPos, _isReversed);
    }

    public JSONObject getSettings() throws JSONException {
        JSONObject settings = new JSONObject();

        settings.put("name", name);
        settings.put("defaultPos", defaultPos);
        settings.put("maxPos", maxPos);
        settings.put("minPos", minPos);
        settings.put("isReversed", isReversed);

        return settings;
    }

    public void map(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, name);
    }

    public void setName(String _name) {
        name = _name;
    }
    public void setDefaultPosition(double _defaultPos) {
        defaultPos = _defaultPos;
    }
    public void setMaxPosition(double _maxPos) {
        maxPos = _maxPos;
    }
    public void setMinPosition(double _minPos) {
        minPos = _minPos;
    }

    public String getName() {
        return name;
    }
    public double getDefaultPosition() {
        return defaultPos;
    }
    public double getMaxPosition() {
        return maxPos;
    }
    public double getMinPosition() {
        return minPos;
    }

    public void setPositionDefault() {
        servo.setPosition(defaultPos);
    }
    public void setPositionMax() {
        servo.setPosition(maxPos);
    }
    public void setPositionMin() {
        servo.setPosition(minPos);
    }
    public void increment(double increment){
        double pos = Range.clip(getPosition() + increment, maxPos, minPos);
        setPosition(pos);
    }

    public void checkIfReversed() {
        if (isReversed) {
            servo.setDirection(Direction.REVERSE);
        }
    }

    // Default Servo stuff
    public ServoController getController() {
        return servo.getController();
    }

    public int getPortNumber() {
        return servo.getPortNumber();
    }

    public void setDirection(Direction direction) {
        servo.setDirection(direction);
    }

    public Direction getDirection() {
        return servo.getDirection();
    }

    public void setPosition(double position) {
        servo.setPosition(position);
    }

    public double getPosition() {
        return servo.getPosition();
    }

    public void scaleRange(double min, double max) {
        servo.scaleRange(min, max);
    }

    public Manufacturer getManufacturer() {
        return servo.getManufacturer();
    }

    public String getDeviceName() {
        return servo.getDeviceName();
    }

    public String getConnectionInfo() {
        return servo.getConnectionInfo();
    }

    public int getVersion() {
        return servo.getVersion();
    }

    public void resetDeviceConfigurationForOpMode() {
        servo.resetDeviceConfigurationForOpMode();
    }

    public void close() { servo.close(); }

}
