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
    private double speedFactor = 0.01;
    private double motorSpeed = 0.0;
    private double servoPosition = 0.5;

    public void runOpMode() {
        // Set defaults
        leftMotor = hardwareMap.dcMotor.get("leftIntakeMotor");
        rightMotor = hardwareMap.dcMotor.get("rightIntakeMotor");

        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightServo.setDirection(Servo.Direction.REVERSE);

        leftServo = hardwareMap.servo.get("leftIntakeServo");
        rightServo = hardwareMap.servo.get("rightIntakeServo");

        leftMotor.setPower(0);
        rightMotor.setPower(0);

        leftServo.setPosition(servoPosition);
        rightServo.setPosition(servoPosition);

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                // Start ejecting if button is pressed
                if(gamepad1.a) {
                    // Reverses direction of motor
                    motorSpeed *= -1;
                }

                // The speed factor times the y sticks position is added to motor speed and servo position
                motorSpeed += speedFactor*gamepad1.left_stick_y;
                servoPosition += speedFactor*gamepad1.right_stick_y;

                // Update power and position
                leftMotor.setPower(motorSpeed);
                rightMotor.setPower(motorSpeed);

                leftServo.setPosition(servoPosition);
                rightServo.setPosition(servoPosition);

                // Print Telemetry
                telemetry.addData("Left servo position: ", leftServo.getPosition());
                telemetry.addData("Right servo position: ", rightServo.getPosition());
                telemetry.addData("Motor speed: ", motorSpeed);
            }
        }


    }
}
