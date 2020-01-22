package org.firstinspires.ftc.teamcode.Mecanum;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "MotorTest", group="Mecanum Op")
public class IntakeTest extends LinearOpMode {
    private DcMotor leftMotor, rightMotor;
    private Servo leftServo, rightServo;
    private double speedFactor = 0.05;
    private double motorSpeed = 0.0;
    private double servoPosition = 0.0;

    public void runOpMode() {
        leftMotor = hardwareMap.dcMotor.get("leftIntakeMotor");
        rightMotor = hardwareMap.dcMotor.get("rightIntakeMotor");

        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightServo.setDirection(Servo.Direction.REVERSE);

        leftServo = hardwareMap.servo.get("leftIntakeServo");
        rightServo = hardwareMap.servo.get("rightIntakeServo");

        leftMotor.setPower(0);
        rightMotor.setPower(0);

        leftServo.setPosition(0);
        rightServo.setPosition(0);

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                motorSpeed += speedFactor*gamepad1.left_stick_y;
                servoPosition += speedFactor*gamepad2.right_stick_x;

                leftMotor.setPower(motorSpeed);
                rightMotor.setPower(motorSpeed);

                leftServo.setPosition(servoPosition);
                rightServo.setPosition(servoPosition);
            }
        }


    }
}
