package org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos;

import com.qualcomm.robotcore.hardware.DcMotor;

public class NewDcMotor {

    public DcMotor motor;

    private String name;
    private double defaultPower;
    private double maxPower;
    private double minPower;
    private boolean isReversed;

    public NewDcMotor(String _name, double _defaultPower, double _maxPower, double _minPower, boolean _isReversed) {
        name = _name;
        defaultPower = _defaultPower;
        maxPower = _maxPower;
        minPower = _minPower;
        isReversed = _isReversed;
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

    public void setDefaultPower() {
        motor.setPower(defaultPower);
    }
    public void setMaxPower() {
        motor.setPower(maxPower);
    }
    public void setMinPower() {
        motor.setPower(minPower);
    }

    public void checkIfReversed() {
        if (this.isReversed) {
            this.motor.setDirection(DcMotor.Direction.REVERSE);
        }
    }
}
