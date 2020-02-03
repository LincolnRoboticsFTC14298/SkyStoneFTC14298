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

        public void build(HardwareMap ahwMap) {
            // Initialize motors and servos
            hwMap = ahwMap;
            for (NewDcMotor motor : motors) {
                motor.motor = hwMap.dcMotor.get(motor.getName());
                motor.checkIfReversed(); // Reverses motor if needed
                motor.setDefaultPower();
            }
            for (NewServo servo : servos) {
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
            this.servos.addAll(robotPart.getNewServos());
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

    private abstract class DriveTrain {
        public abstract class TeleOp {
            abstract void move();
        }
        public abstract class Encoder {
            void move(double speed, double dist, double timeout, boolean opIsActive) {

            };
            void rotate(double speed, double angle, double timeout, boolean opIsActive) {

            };
            abstract void strafe(double speed, double dist, double timeout, boolean opIsActive);
        }
    }

    public getDriveTrain() { return this.driveTrain; }

    private class Mecanum extends DriveTrain {
        public class TeleOp extends DriveTrain.TeleOp {
            public void move() {

            }
        }
        public class Encoder extends DriveTrain.Encoder {
            public void strafe(double speed, double dist, double timeout, boolean opIsActive) {}
        }
    }

    private class Classic implements DriveTrain {
        public class TeleOp extends DriveTrain.TeleOp {
            public void move() {

            }
        }
        public class Encoder extends DriveTrain.Encoder {
            public void strafe(double speed, double dist, double timeout, boolean opIsActive) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
