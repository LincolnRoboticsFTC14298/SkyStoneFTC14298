package org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos;

import com.qualcomm.robotcore.hardware.Servo;

public class NewServo {

    public Servo servo;

    private String name;
    private double defaultPos;
    private double openedPos;
    private double closedPos;

    public NewServo(String _name, double _defaultPos, double _openedPos, double _closedPos) {
        name = _name;
        defaultPos = _defaultPos;
        openedPos = _openedPos;
        closedPos = _closedPos;
    }

    public String getName() {
        return name;
    }
    public double getDefaultPosition() {
        return defaultPos;
    }
    public double getOpenedPosition() {
        return openedPos;
    }
    public double getClosedPosition() {
        return closedPos;
    }

    public void setDefaultPosition() {
        servo.setPosition(defaultPos);
    }
    public void open() {
        servo.setPosition(openedPos);
    }
    public void close() {
        servo.setPosition(closedPos);
    }

}
