package org.firstinspires.ftc.teamcode.Mecanum;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "MotorTest", group="Mecanum Op")
public class IntakeTest extends LinearOpMode {
    private DcMotor leftMotor, rightMotor;
    private Servo leftIntakeServo, rightIntakeServo;
    private double speedFactor = 1;
    private double motorSpeed = 0.0;
    private double leftIntakeServoPos = 0.5; // Closed at 0.5, opens at 0.13
    private double rightIntakeServoPos = 0.8; // CLosed at 0.8, opens at 1.0

    public void runOpMode() {
        // Set defaults
        leftMotor = hardwareMap.dcMotor.get("leftIntakeMotor");
        rightMotor = hardwareMap.dcMotor.get("rightIntakeMotor");

        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        //rightServo.setDirection(Servo.Direction.REVERSE);

        leftIntakeServo = hardwareMap.servo.get("leftIntakeServo");
        rightIntakeServo = hardwareMap.servo.get("rightIntakeServo");

        leftMotor.setPower(0);
        rightMotor.setPower(0);

        leftIntakeServo.setPosition(leftIntakeServoPos);
        rightIntakeServo.setPosition(rightIntakeServoPos);

        waitForStart();

        while (!isStopRequested()) {
            // The speed factor

            if (gamepad1.b) {
                motorSpeed = 1; // Intake assuming right motor is reversed
            }
            else if (gamepad1.x) {
                motorSpeed = -1; // Eject assumign right motor is reversed
            }
            else {
                motorSpeed = 0;
            }

            if (gamepad1.y) {
                leftIntakeServoPos = Range.clip(leftIntakeServoPos - 0.01, 0.13, 0.5);
                rightIntakeServoPos = Range.clip(rightIntakeServoPos + 0.01, 0.8, 1);
            }

            if (gamepad1.a) {
                leftIntakeServoPos = Range.clip(leftIntakeServoPos + 0.01, 0.13, 0.5);
                rightIntakeServoPos = Range.clip(rightIntakeServoPos - 0.01, 0.8, 1);
            }
            leftIntakeServo.setPosition(leftIntakeServoPos);
            rightIntakeServo.setPosition(rightIntakeServoPos);

            // Update power and position
            leftMotor.setPower(motorSpeed);
            rightMotor.setPower(motorSpeed);

            // Print Telemetry
            telemetry.addData("Left servo position: ", leftIntakeServo.getPosition());
            telemetry.addData("Right servo position: ", rightIntakeServo.getPosition());
            telemetry.addData("Motor speed: ", motorSpeed);
            telemetry.update();
        }


    }
}
