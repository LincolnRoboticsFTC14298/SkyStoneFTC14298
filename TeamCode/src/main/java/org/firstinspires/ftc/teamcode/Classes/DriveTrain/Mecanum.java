package org.firstinspires.ftc.teamcode.Classes.DriveTrain;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewDcMotor;

public class Mecanum extends DriveTrain {
    public TeleOp teleop;
    public Autonomous autonomous;
    public Mecanum() {
        teleop = new TeleOp();
        autonomous = new Autonomous();
        autonomous.init(383.6 , 2.0, 4.0, Math.sqrt(13*13 + 15*15)/2.0);
    }
    public class TeleOp implements DriveTrain.TeleOp {
        public void move(double leftx, double lefty, double rightx, double righty) {
            double speed = Math.hypot(leftx, lefty);
            double angle = Math.atan2(-1 * lefty, leftx);
            double turn = rightx;
            double theta = angle - Math.PI / 4;

            motors[0].setPower(speed * Math.cos(theta) + turn);
            motors[1].setPower(speed * Math.sin(theta) - turn);
            motors[2].setPower(speed * Math.sin(theta) + turn);
            motors[3].setPower(speed * Math.cos(theta) - turn);
        }
    }
    public class Autonomous implements DriveTrain.Autonomous {
        private ElapsedTime runtime = new ElapsedTime();
        private double COUNTS_PER_MOTOR_REV;    // eg: 5202 Series Yellow Jacket Planetary Gear Motor (13.7:1 Ratio, 435 RPM)
        private double DRIVE_GEAR_REDUCTION;     // This is < 1.0 if geared UP
        private double WHEEL_DIAMETER_INCHES;     // For figuring circumference
        private double COUNTS_PER_INCH; // (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
        private double RADIUS;


        public void init(double COUNTS_PER_MOTOR_REV, double DRIVE_GEAR_REDUCTION, double WHEEL_DIAMETER_INCHES, double RADIUS) {
            this.COUNTS_PER_MOTOR_REV = COUNTS_PER_MOTOR_REV;
            this.DRIVE_GEAR_REDUCTION = DRIVE_GEAR_REDUCTION;
            this.WHEEL_DIAMETER_INCHES = WHEEL_DIAMETER_INCHES;
            this.COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);
            this.RADIUS = RADIUS;
        }

        public void move(double speed, double distance, double timeout, boolean opModeActive) {
            double[] distances = new double[motors.length];
            for (double dist : distances) {
                dist = distance;
            }
            encoderDrive(speed, distances, timeout, opModeActive);
        }
        public void rotate(double speed, double angle, double timeout, boolean opModeActive) {
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
        }
        public void strafe(double speed, double distance, double timeout, boolean opModeActive) {
            double[] distances = {distance, -distance, -distance, distance};
            encoderDrive(speed, distances, timeout, opModeActive);
        }

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
                int target = motors[i].getCurrentPosition() + (int) (distances[i] * COUNTS_PER_INCH);
                motors[i].setTargetPosition(target);
                targetdistances[i] = target;
            }
            return targetdistances;
        }
        private boolean motorsAreBusy(NewDcMotor[] motors) {
                /* Note: We use (isBusy() || isBusy()) in the loop test, which means that when EITHER motor hits
                its target position, the motion will stop.  This is "safer" in the event that the robot will
                always end the motion as soon as possible.
                However, if you require that BOTH motors have finished their moves before the robot continues
                onto the next step, use (isBusy() && isBusy()) in the loop test. */
            boolean motorsAreBusy = false;
            for (NewDcMotor motor : motors) {
                motorsAreBusy = motorsAreBusy || motor.isBusy();
            }
            return motorsAreBusy;
        }

    }
}
