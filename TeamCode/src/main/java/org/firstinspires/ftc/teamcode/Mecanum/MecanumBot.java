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
    public DcMotor  leftFront   = null;
    public DcMotor  rightFront  = null;
    public DcMotor  leftBack    = null;
    public DcMotor  rightBack   = null;

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

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        this.setWheelPower(0);

        // Set all motors to RUN_USING_ENCODERS if encoders are installed.
        this.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}


