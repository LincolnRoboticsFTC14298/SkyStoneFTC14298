package org.firstinspires.ftc.teamcode.Classes;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Classes.DriveTrain.DriveTrain;
import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewDcMotor;
import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewServo;
import org.firstinspires.ftc.teamcode.Classes.RobotParts.RobotPart;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* MODULAR ROBOT CODE */
public class Robot {

    private HardwareMap hardwareMap;
    private RobotPart[] robotParts;
    private DriveTrain drivetrain;

    private List<NewDcMotor> motors = new ArrayList<>();
    private List<NewServo> servos = new ArrayList<>();

    public Robot(DriveTrain drivetrain, RobotPart[] robotParts) {
        this.drivetrain = drivetrain;
        this.robotParts = robotParts;
    }

    public void init(HardwareMap hardwareMap) {
        for (RobotPart robotPart : robotParts) {
            addAll(motors, robotPart.getNewDcMotors());
            addAll(servos, robotPart.getNewServos());
        }
        addAll(motors, drivetrain.getNewDcMotors());
        
        this.hardwareMap = hardwareMap;

        // Initialize motors and servos
        for (NewDcMotor motor : motors) {
            motor = (NewDcMotor) hardwareMap.dcMotor.get(motor.getName());
            motor.setToDefaultPower();
        }
        for (NewServo servo : servos) {
            servo = (NewServo) hardwareMap.servo.get(servo.getName());
            servo.setToDefaultPosition();
        }
    }

    // For telemetry, get current powers and positions
    public NewDcMotor[] getNewDcMotors() {
        NewDcMotor[] arr = (NewDcMotor[]) motors.toArray();
        return arr;
    }
    public NewServo[] getNewServos() {
        NewServo[] arr = (NewServo[]) servos.toArray();
        return arr;
    }

    /* METHODS FOR ADDING ROBOT robotParts */

    // Note: the robotParts code must be custom made.

    public DriveTrain getDriveTrain() { return this.drivetrain; }

    public void loadJSON(JSONObject settings) {
        // Get drivetrain motors settings and initialize
        try {
            drivetrain.initJSON(settings.getJSONObject("Drivetrain"));
            // Get part motors and servos and initialize
            for (RobotPart robotPart : robotParts) {
                robotPart.initJSON(settings.getJSONObject("Parts").getJSONObject(robotPart.name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

    }

    public JSONObject generateJSON() {
        try {
            JSONObject settings = new JSONObject();
            settings.put("Drivetrain", drivetrain.getSettings());

            JSONObject partSettings = new JSONObject();
            for (RobotPart robotPart : robotParts) {
                partSettings.put(robotPart.name, robotPart.getSettings());
            }
            settings.put("Parts", partSettings);
            return settings;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <p> void addAll(List<p> partList, p[] parts) {
        partList.addAll(Arrays.asList(parts));
    }

    /*
    private abstract class DriveTrain {
        public NewDcMotor[] motors;
        public DriveTrain(NewDcMotor[] motors){
            this.motors = motors;
        }

        public NewDcMotor[] getNewDcMotors(){
            return motors;
        }

        public abstract class TeleOp {
            abstract void move();
        }
        public abstract class Autonomous {
            private ElapsedTime runtime = new ElapsedTime();
            private double COUNTS_PER_MOTOR_REV;    // eg: 5202 Series Yellow Jacket Planetary Gear Motor (13.7:1 Ratio, 435 RPM)
            private double DRIVE_GEAR_REDUCTION;     // This is < 1.0 if geared UP
            private double WHEEL_DIAMETER_INCHES;     // For figuring circumference
            private double COUNTS_PER_INCH; // (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
            private double RADIUS;

            public Encoder(double COUNTS_PER_MOTOR_REV, double DRIVE_GEAR_REDUCTION, double WHEEL_DIAMETER_INCHES, double RADIUS) {
                this.COUNTS_PER_MOTOR_REV = COUNTS_PER_MOTOR_REV;
                this.DRIVE_GEAR_REDUCTION = DRIVE_GEAR_REDUCTION;
                this.WHEEL_DIAMETER_INCHES = WHEEL_DIAMETER_INCHES;
                this.COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);
                this.RADIUS = RADIUS;
            }

            void move(double speed, double distance, double timeout, boolean opModeActive) {
                double[] distances = new double[motors.length];
                for (double dist : distances) {
                    dist = distance;
                }
                encoderDrive(speed, distances, timeout, opModeActive);
            }
            void rotate(double speed, double angle, double timeout, boolean opModeActive) {
                double circum = -2*Math.PI*RADIUS*angle/360; // Full circumfrance times ratio of angle, by default left
                double[] distances;
                if (motors.length == 2) {
                    distances = new double[]{circum, -circum};

                }
                else if (motors.length == 4) {
                    distances = new double[]{circum, -circum, circum, -circum};
                }
                else {
                    distances = null;
                }
                encoderDrive(speed, distances, timeout, opModeActive);
            };

            public void encoderDrive(double speed, double[] distances, double timeout, boolean opModeActive) {
                int[] targetdistances;
                // Ensure that the opmode is still active
                if (opModeActive) {
                    // Determine new target position, and pass to motor controller
                    targetdistances = setTargetDistances(motors, distances);

                    // Turn On RUN_TO_POSITION
                    setMode(motors, DcMotor.RunMode.RUN_TO_POSITION);

                    // reset the timeout time and start motion.
                    runtime.reset();
                    setPower(motors, Math.abs(speed));

                    // Keep looping while we are still active, and there is time left, and both motors are running.
                    // Note: We use (isBusy() || isBusy()) in the loop test, which means that when EITHER motor hits
                    // its target position, the motion will stop.  This is "safer" in the event that the robot will
                    // always end the motion as soon as possible.
                    // However, if you require that BOTH motors have finished their moves before the robot continues
                    // onto the next step, use (isBusy() && isBusy()) in the loop test.

                    while (opModeActive && (runtime.seconds() < timeout) && (motorsAreBusy(motors))) {
                        /*
                        // Display it for the driver.
                        telemetry.addData("Target", "Running to lf: %7d, rf: %7d, lb: %7d, rb: %7d",
                                newLfTarget,
                                newRfTarget,
                                newLbTarget,
                                newRbTarget
                        );
                        telemetry.addData("Currently", "Running at lf: %7d, rf: %7d, lb: %7d, rb: %7d",
                                motors[0].getCurrentPosition(),
                                motors[1].getCurrentPosition(),
                                motors[2].getCurrentPosition(),
                                motors[3].getCurrentPosition()
                        );
                        telemetry.update();*/
    /*
                    }

                    // Stop all motion
                    setPower(motors, 0);

                    // Turn off RUN_TO_POSITION
                    setMode(motors, DcMotor.RunMode.RUN_USING_ENCODER);
                }
            }
            private int[] setTargetDistances(NewDcMotor[] motors, double[] distances) {
                int[] targetdistances = new int[4];
                for (int i = 0; i < 4; i++) {
                    int target = motors[i].motor.getCurrentPosition() + (int) (distances[i] * COUNTS_PER_INCH);
                    motors[i].motor.setTargetPosition(target);
                    targetdistances[i] = target;
                }
                return targetdistances;
            }
            private boolean motorsAreBusy(NewDcMotor[] motors) {
                Note: We use (isBusy() || isBusy()) in the loop test, which means that when EITHER motor hits
                its target position, the motion will stop.  This is "safer" in the event that the robot will
                always end the motion as soon as possible.
                However, if you require that BOTH motors have finished their moves before the robot continues
                onto the next step, use (isBusy() && isBusy()) in the loop test.
                boolean motorsAreBusy = false;
                for (NewDcMotor motor : motors) {
                    motorsAreBusy = motorsAreBusy || motor.motor.isBusy();
                }
                return motorsAreBusy;
            }

            abstract void strafe(double speed, double distance, double timeout, boolean opModeActive);

        }
    }

    private class Mecanum extends DriveTrain {
        public Mecanum(NewDcMotor[] motors){
            super(motors);
        }
        public class TeleOp extends DriveTrain.TeleOp {
            public void move(double speed, double angle, double turn) {
                double theta = angle - Math.PI / 4;

                motors[0].motor.setPower(speed * Math.cos(theta) + turn);
                motors[1].motor.setPower(speed * Math.sin(theta) - turn);
                motors[2].motor.setPower(speed * Math.sin(theta) + turn);
                motors[3].motor.setPower(speed * Math.cos(theta) - turn);
            }
        }
        public class Encoder extends DriveTrain.Autonomous {
            public Encoder(double COUNTS_PER_MOTOR_REV, double DRIVE_GEAR_REDUCTION, double WHEEL_DIAMETER_INCHES, double RADIUS) {
                super(COUNTS_PER_MOTOR_REV, DRIVE_GEAR_REDUCTION, WHEEL_DIAMETER_INCHES, RADIUS);
            }
            public void strafe(double speed, double distance, double timeout, boolean opModeActive) {
                double[] distances = {distance, -distance, -distance, distance};
                encoderDrive(speed, distances, timeout, opModeActive);
            }
        }
    }

    private class Classic extends DriveTrain {
        public Classic(NewDcMotor[] motors){
            super(motors);
        }
        public class TeleOp extends DriveTrain.TeleOp {
            public void move(double vel, double turn) {
                //vel = speed * 1 or -1, front and back respectivly
                for (NewDcMotor motor : motors) {
                    motor.motor.setPower(vel-turn);
                    turn *= -1;
                }

            }
        }
        public class Encoder extends DriveTrain.Autonomous {
            public Encoder(double COUNTS_PER_MOTOR_REV, double DRIVE_GEAR_REDUCTION, double WHEEL_DIAMETER_INCHES, double RADIUS) {
                super(COUNTS_PER_MOTOR_REV, DRIVE_GEAR_REDUCTION, WHEEL_DIAMETER_INCHES, RADIUS);
            }
            public void strafe(double speed, double distance, double timeout, boolean opModeActive) {
                throw new UnsupportedOperationException();
            }
        }
    }
    */

}
