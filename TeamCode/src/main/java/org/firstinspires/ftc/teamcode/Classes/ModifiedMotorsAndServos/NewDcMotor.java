package org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.json.JSONException;
import org.json.JSONObject;

public class NewDcMotor implements DcMotorSimple {
    private DcMotor motor;

    private String name = "null";
    private double defaultPower = 0;
    private double maxPower = 1;
    private double minPower = 0;
    private boolean isReversed = false;

    public void init(String _name, double _defaultPower, double _maxPower, double _minPower, boolean _isReversed) {
        name = _name;
        defaultPower = _defaultPower;
        maxPower = _maxPower;
        minPower = _minPower;
        isReversed = _isReversed;
        checkIfReversed(); // Reverses motor if needed
    }
    public void initJSON(JSONObject settings) throws JSONException {
        String _name = settings.getString("name");
        double _defaultPower = settings.getDouble("defaultPower");
        double _maxPower = settings.getDouble("maxPower");
        double _minPower = settings.getDouble("minPower");
        boolean _isReversed = settings.getBoolean("isReversed");

        init(_name, _defaultPower, _maxPower, _minPower, _isReversed);
    }

    public JSONObject getSettings() throws JSONException {
        JSONObject settings = new JSONObject();

        settings.put("name", name);
        settings.put("defaultPower", defaultPower);
        settings.put("maxPower", maxPower);
        settings.put("minPower", minPower);
        settings.put("isReversed", isReversed);

        return settings;
    }

    public void map(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, name);
    }

    public void setName(String _name) {
        name = _name;
    }
    public void setDefaultPower(double _defaultPower) {
        defaultPower = _defaultPower;
    }
    public void setMaxPower(double _maxPower) {
        maxPower = _maxPower;
    }
    public void setMinPower(double _minPower) {
        minPower = _minPower;
    }

    public String getName() {
        return name;
    }
    public double getDefaultPower() {
        return defaultPower;
    }
    public double getMaxPower() {
        return maxPower;
    }
    public double getMinPower() {
        return minPower;
    }

    public void setPowerDefault() {
        motor.setPower(defaultPower);
    }
    public void setPowerMax() {
        motor.setPower(maxPower);
    }
    public void setPowerMin() {
        motor.setPower(minPower);
    }

    public void checkIfReversed() {
        if (isReversed) {
            motor.setDirection(Direction.REVERSE);
        }
    }

    // Normal motor stuff
    public MotorConfigurationType getMotorType() {
        return motor.getMotorType();
    }

    public void setMotorType(MotorConfigurationType motorType) {
        motor.setMotorType(motorType);
    }

    public DcMotorController getController() {
        return motor.getController();
    }

    public int getPortNumber() {
        return motor.getPortNumber();
    }

    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        motor.setZeroPowerBehavior(zeroPowerBehavior);
    }

    public ZeroPowerBehavior getZeroPowerBehavior() {
        return motor.getZeroPowerBehavior();
    }

    public void setPowerFloat() {
        motor.setPowerFloat();
    }

    public boolean getPowerFloat() {
        return motor.getPowerFloat();
    }

    public void setTargetPosition(int position) {
        motor.setTargetPosition(position);
    }

    public int getTargetPosition() {
        return motor.getTargetPosition();
    }

    public boolean isBusy() {
        return motor.isBusy();
    }

    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }

    public void setMode(RunMode mode) {
        motor.setMode(mode);
    }

    public RunMode getMode() {
        return motor.getMode();
    }

    public void setDirection(Direction direction) {
        motor.setDirection(direction);
    }

    public Direction getDirection() {
        return motor.getDirection();
    }

    public void setPower(double power) {
        motor.setPower(power);
    }

    public double getPower() {
        return motor.getPower();
    }

    public Manufacturer getManufacturer() {
        return motor.getManufacturer();
    }

    public String getDeviceName() {
        return motor.getDeviceName();
    }

    public String getConnectionInfo() {
        return motor.getConnectionInfo();
    }

    public int getVersion() {
        return motor.getVersion();
    }

    public void resetDeviceConfigurationForOpMode() {
        motor.resetDeviceConfigurationForOpMode();
    }

    public void close() {
        motor.close();
    }
}
