package org.firstinspires.ftc.teamcode.Mecanum;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo Test", group="Mecanum Op")
public class ServoTest extends LinearOpMode {
    private Servo servo;

    public void runOpMode() {
        servo = hardwareMap.servo.get("leftFoundationServo");
        servo.setPosition(0);

        int i = 1;
        waitForStart();
        if(opModeIsActive()){
            while (opModeIsActive()) {
                sleep(3000);
                servo.setPosition(i);
                i = 1 - i;

                telemetry.addData("Servo Position: ", servo.getPosition());
                telemetry.update();
            }
        }
    }
}
