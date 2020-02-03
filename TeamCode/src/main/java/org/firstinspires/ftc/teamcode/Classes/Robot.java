package org.firstinspires.ftc.teamcode.Classes;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewDcMotor;
import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewServo;


import java.util.ArrayList;
import java.util.List;

/* MODULAR ROBOT CODE */
public class Robot {

    public class Builder {
        private Class[] robotParts = null;

        public Robot build(HardwareMap ahwMap) {
            // Initialize motors and servos
            hwMap = ahwMap;
            for (NewDcMotor motor : Motors) {
                motor.motor = hwMap.dcMotor.get(motor.getName());
                motor.checkIfReversed(); // Reverses motor if needed
                motor.setDefaultPower();
            }
            for (NewServo servo : Servos) {
                servo.servo = hwMap.servo.get(servo.getName());
                servo.setDefaultPosition();
            }
        }

    }

    private HardwareMap hwMap;
    private Class[] robotParts;

    private List<NewDcMotor> motors;
    private List<NewServo> servos;

    public Robot(HardwareMap hwMap, Class[] robotParts) {
        this.hwMap = hwMap;
        this.robotParts = robotParts;

        this.motors = new ArrayList<NewDcMotor>();
        this.servos = new ArrayList<NewServo>();
        
        for (Class robotPart : robotParts) {
            this.motors.addAll(robotPart.getNewDcMotors());
        }

    }

    // For telemetry, get current powers and positions
    public double[] getMotorPowers() {
        int size = motors.size();
        double[] powers = new double[size];
        for(int i = 0; i < size; i++) {
            powers[i] = motors.get(i).motor.getPower();
        }
        return powers;
    }
    public double[] getServoPositions() {
        int size = servos.size();
        double[] positions = new double[size];
        for(int i = 0; i < size; i++) {
            positions[i] = servos.get(i).servo.getPosition();
        }
        return positions;
    }

    /* METHODS FOR ADDING ROBOT robotParts */

    // Note: the robotParts code must be costume made.

    private interface DriveTrain {
        interface TeleOp {
            void move();
        }
        interface Mecanum {
            void move(double speed, double dist, double timeout, boolean opIsActive);
            void strafe(double speed, double dist, double timeout, boolean opIsActive);
            void rotate(double speed, double angle, double timeout, boolean opIsActive);
        }
    }

    public getDriveTrain() { return this.driveTrain; }

    private class Mecanum implements DriveTrain {
        public TeleOp implements TeleOp {

        }
    }

    private class Classic implements DriveTrain {

        public void strafe() {
            throw new UnsupportedOperationException();
        }
    }
}
