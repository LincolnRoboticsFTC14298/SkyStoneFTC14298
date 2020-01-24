package org.firstinspires.ftc.teamcode.Mecanum;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo Test", group="Mecanum Op")
public class ServoTest extends LinearOpMode {
    private Servo servo;

    public void runOpMode() {
        servo = hardwareMap.servo.get("LeftServo");
        servo.setPosition(0);

        waitForStart();
        if(opModeIsActive()){
            sleep(3000);
            servo.setPosition(1);
        }
    }
}
