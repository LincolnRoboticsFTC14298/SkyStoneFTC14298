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

    private Servo claw;
    private Servo flipper;

    public void runOpMode() {
        leftFoundationServo = hardwareMap.get(Servo.class, "leftFoundationServo");
        rightFoundationServo = hardwareMap.get(Servo.class, "rightFoundationServo");

        leftIntakeServo = hardwareMap.servo.get("leftIntakeServo");
        rightIntakeServo = hardwareMap.servo.get("rightIntakeServo");
        claw = hardwareMap.servo.get("claw");

        flipper = hardwareMap.servo.get("flipper");

        leftFoundationServo.setPosition(0);
        rightFoundationServo.setPosition(0);

        leftIntakeServo.setPosition(0.5);
        rightIntakeServo.setPosition(0.8);

        claw.setPosition(1); // Most upright claw position

        double foundationPosition = 0;
        double intakeLPos = 0.5; // 0.5 closed, 0.13 to open
        double intakeRPos = 0.8; // 0.8 closed, 1 to open
        double clawPos = 1; // 1 opened .65 closed
        double flipperPos = 0;

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

            if (gamepad1.left_trigger > 0.0) {
                clawPos = Range.clip(clawPos + 0.01,.65,1);
            }
            if (gamepad1.right_trigger > 0.0) {
                clawPos = Range.clip(clawPos - 0.01,.65,1);
            }

            if (gamepad1.dpad_up) {
                flipperPos = Range.clip(flipperPos + 0.01, 0, 1);
            }
            if (gamepad1.dpad_down) {
                flipperPos = Range.clip(flipperPos - 0.01, 0, 1);
            }
            leftFoundationServo.setPosition(foundationPosition);
            rightFoundationServo.setPosition(foundationPosition);

            leftIntakeServo.setPosition(intakeLPos);
            rightIntakeServo.setPosition(intakeRPos);

            claw.setPosition(clawPos);
            flipper.setPosition(flipperPos);

            // Print Telemetry
            telemetry.addData("leftFoundationServo position: ", leftFoundationServo.getPosition());
            telemetry.addData("rightFoundationServo position: ", rightFoundationServo.getPosition());
            telemetry.addData("leftIntakeServo position: ", leftIntakeServo.getPosition());
            telemetry.addData("rightIntakeServo position: ", rightIntakeServo.getPosition());
            telemetry.addData("claw position", claw.getPosition());
            telemetry.addData("Flipper position", flipper.getPosition());
            telemetry.update();
        }
    }
}
