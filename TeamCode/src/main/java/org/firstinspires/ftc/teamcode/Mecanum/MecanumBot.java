package org.firstinspires.ftc.teamcode.Mecanum;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a goBILDA mecanum strafing bot.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "leftDrive"
 * Motor channel:  Right drive motor:        "rightDrive"
 */
public class MecanumBot {
    /* Public OpMode members. */
    // Wheel Motors
    public DcMotor  leftFront;
    public DcMotor  rightFront;
    public DcMotor  leftBack;
    public DcMotor  rightBack;

    public DcMotor rightIntakeMotor;
    public DcMotor leftIntakeMotor;

    public Servo rightIntakeServo;
    public Servo leftIntakeServo;

    public double leftIntakeServoPos = 0.5; // Closed at 0.5, opens at 0.13
    public double rightIntakeServoPos = 0.8; // Closed at 0.8, opens at 1.0
    public double clawPos = 1; // Up at 1, down at 0.65

    public Servo rightFoundationServo;
    public Servo leftFoundationServo;

    public Servo claw;

    public Servo flipper;

    private DcMotor[] wheelMotors = new DcMotor[4];

    /* local OpMode members. */
    private HardwareMap hwMap;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public MecanumBot(){

    }

    /* Sets the power of motors on the wheels to a single power */
    public void setPower(double power) {
        leftFront.setPower(power);
        rightFront.setPower(power);
        leftBack.setPower(power);
        rightBack.setPower(power);
    }

    public void setMode(DcMotor.RunMode mode) {
        leftFront.setMode(mode);
        rightFront.setMode(mode);
        leftBack.setMode(mode);
        rightBack.setMode(mode);
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftFront = hwMap.get(DcMotor.class, "leftFront");
        rightFront = hwMap.get(DcMotor.class, "rightFront");
        leftBack = hwMap.get(DcMotor.class, "leftBack");
        rightBack = hwMap.get(DcMotor.class, "rightBack");

        rightIntakeMotor = hwMap.get(DcMotor.class, "rightIntakeMotor");
        leftIntakeMotor = hwMap.get(DcMotor.class, "leftIntakeMotor");

        leftFoundationServo = hwMap.get(Servo.class, "leftFoundationServo");
        rightFoundationServo = hwMap.get(Servo.class, "rightFoundationServo");

        leftIntakeServo = hwMap.get(Servo.class, "leftIntakeServo");
        rightIntakeServo = hwMap.get(Servo.class, "rightIntakeServo");

        claw = hwMap.get(Servo.class, "claw");
        flipper = hwMap.get(Servo.class, "flipper");

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        rightIntakeMotor.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        setPower(0);
        /*
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        */
        rightIntakeMotor.setPower(0);
        leftIntakeMotor.setPower(0);


        // Set all motors to RUN_USING_ENCODERS if encoders are installed.
        // this.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        /*
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        */

        // Set the Servo to its starting positions
        leftIntakeServo.setPosition(leftIntakeServoPos);
        rightIntakeServo.setPosition(rightIntakeServoPos);
        claw.setPosition(clawPos);
        flipper.setPosition(0);
    }
}


