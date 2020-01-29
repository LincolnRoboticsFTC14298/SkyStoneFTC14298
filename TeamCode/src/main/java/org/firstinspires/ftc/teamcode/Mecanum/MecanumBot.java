package org.firstinspires.ftc.teamcode.Mecanum;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 */
public class MecanumBot {
    /* Public OpMode members. */
    // Wheel Motors
    public DcMotor  leftFront   = null;
    public DcMotor  rightFront  = null;
    public DcMotor  leftBack    = null;
    public DcMotor  rightBack   = null;

    public DcMotor rightIntakeMotor = null;
    public DcMotor leftIntakeMotor = null;

    public Servo rightIntakeServo = null;
    public Servo leftIntakeServo = null;

    private double rightIntakeServoPos = 0.8;
    private double leftIntakeServoPos = 0.5;

    public Servo rightFoundationServo = null;
    public Servo leftFoundationServo = null;

    private  DcMotor[] wheelMotors = {leftFront, rightFront, leftBack, rightBack};

    /* local OpMode members. */
    private HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public MecanumBot(){

    }

    /* Sets the power of motors on the wheels to a single power */
    public void setWheelPower(double power) {
        for (DcMotor motor : wheelMotors) {
            motor.setPower(power);
        }
    }

    public void setMode(DcMotor.RunMode mode) {
        for (DcMotor motor : wheelMotors) {
            motor.setMode(mode);
        }
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

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        rightIntakeMotor.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        // this.setWheelPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        rightIntakeMotor.setPower(0);
        leftIntakeMotor.setPower(0);


        // Set all motors to RUN_USING_ENCODERS if encoders are installed.
        // this.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set the Servo to its starting positions
        leftIntakeServo.setPosition(leftIntakeServoPos);
        rightIntakeServo.setPosition(rightIntakeServoPos);
    }
}


