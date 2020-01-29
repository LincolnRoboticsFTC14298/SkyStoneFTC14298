package org.firstinspires.ftc.teamcode.Mecanum;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Servo Test", group="Mecanum Op")
public class ServoTest extends LinearOpMode {
    private Servo leftFoundationServo;
    private Servo rightFoundationServo;

    private Servo leftIntakeServo;
    private Servo rightIntakeServo;

    public void runOpMode() {
        leftFoundationServo = hardwareMap.get(Servo.class, "leftFoundationServo");
        rightFoundationServo = hardwareMap.get(Servo.class, "rightFoundationServo");

        leftIntakeServo = hardwareMap.servo.get("leftIntakeServo");
        rightIntakeServo = hardwareMap.servo.get("rightIntakeServo");

        leftFoundationServo.setPosition(0);
        rightFoundationServo.setPosition(0);

        leftIntakeServo.setPosition(0.5);
        rightIntakeServo.setPosition(0.8);

        double foundationPosition = 0;
        double intakeLPos = 0.5; // 0.5 closed, 0.13 to open
        double intakeRPos = 0.8; // 0.8 closed, 1 to open

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.x) {
                foundationPosition = 0.8;
            }

            if (gamepad1.b) {
                foundationPosition = 0;
            }

            if (gamepad1.y) {
                intakeLPos = Range.clip(intakeLPos - 0.01, 0.13, 0.5);
                intakeRPos = Range.clip(intakeRPos + 0.01, 0.8, 1);
            }

            if (gamepad1.a) {
                intakeLPos = Range.clip(intakeLPos + 0.01, 0.13, 0.5);
                intakeRPos = Range.clip(intakeRPos - 0.01, 0.8, 1);
            }
            leftFoundationServo.setPosition(foundationPosition);
            rightFoundationServo.setPosition(foundationPosition);

            leftIntakeServo.setPosition(intakeLPos);
            rightIntakeServo.setPosition(intakeRPos);

            // Print Telemetry
            telemetry.addData("leftFoundationServo position: ", leftFoundationServo.getPosition());
            telemetry.addData("rightFoundationServo position: ", rightFoundationServo.getPosition());
            telemetry.addData("leftIntakeServo position: ", leftIntakeServo.getPosition());
            telemetry.addData("rightIntakeServo position: ", rightIntakeServo.getPosition());
            telemetry.update();
        }

    }
}
