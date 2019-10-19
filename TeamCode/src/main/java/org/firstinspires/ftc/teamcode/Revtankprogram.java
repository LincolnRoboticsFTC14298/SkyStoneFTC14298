
    package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

    @TeleOp(name = "FairgroundsOP (Blocks to Java)", group = "")
    public class Revtankprogram extends LinearOpMode {

        private DcMotor right_drive;
        private DcMotor left_drive;
        private DcMotor arm_movement;
        private Servo left_hand;
        private Servo right_hand;

        /**
         * This function is executed when this Op Mode is selected from the Driver Station.
         */
        @Override
        public void runOpMode() {
            double ServoPosition;
            double ServoSpeed;
            double ServoPositon2;
            double ServoSpeed2;



            right_drive = hardwareMap.dcMotor.get("right_drive");
            left_drive = hardwareMap.dcMotor.get("left_drive");
            arm_movement = hardwareMap.dcMotor.get("arm_movement");
            left_hand = hardwareMap.servo.get("left_hand");
            right_hand = hardwareMap.servo.get("right_hand");

            // TO-DO: Explain each section of code specifically
            // GAME PAD 1
            //     Left Stick: Move up or down
            //      Right Stick: Move right or left
            // GAME PAD 2
            //      Left Bumper: Open Claw
            //      Right Bumper: Close Claw
            //      Dpad UP: Lower Arm to front
            //      Dpad DOWN: Lift Arm to back
            //      Right Stick UP: Increase Lift
            //      Right Stick DOWN: Lower Lift
            // Reverse one of the drive motors.
            // You will have to determine which motor to reverse for your robot.
            // In this example, the right motor was reversed so that positive
            // applied power makes it move the robot in the forward direction.
            right_drive.setDirection(DcMotorSimple.Direction.REVERSE);
            // Set the servos to a default starting position.
            ServoPosition = 0.5;
            ServoPositon2 = 0.5;
            ServoSpeed = 0.1;
            ServoSpeed2 = 0.1;
            // The Y axis of a joystick ranges from -1 in its topmost position
            // to +1 in its bottommost position. We negate this value so that
            // the topmost position corresponds to maximum forward power.
            left_drive.setPower(0);
            right_drive.setPower(0);
            waitForStart();
            // Put run blocks here.
            while (opModeIsActive()) {
                // Put loop blocks here.
                // Use left stick to drive and right stick to turn
                // The Y axis of a joystick ranges from -1 in its topmost position
                // to +1 in its bottommost position. We negate this value so that
                // the topmost position corresponds to maximum forward power.
                left_drive.setPower(-gamepad1.left_stick_y + gamepad1.right_stick_x);
                right_drive.setPower(-gamepad1.left_stick_y - gamepad1.right_stick_x);
                // Use buttons to close and open the claw
                // Left Bumper opens
                if (gamepad1.left_bumper) {
                    ServoPosition += ServoSpeed;
                    ServoPositon2 += -ServoSpeed2;
                }
                // Right Bumper closes
                if (gamepad1.right_bumper) {
                    ServoPosition += -ServoSpeed;
                    ServoPositon2 += ServoSpeed2;
                }
                // Move the arm using motor of the Dpad of gamepad2
                if (gamepad1.dpad_up) {
                    arm_movement.setPower(-1);
                } else {
                    arm_movement.setPower(0);
                }
                if (gamepad1.dpad_down) {
                    arm_movement.setPower(0.3);
                } else {
                    arm_movement.setPower(0);
                }
                // Keep Servo position in valid range
                ServoPosition = Math.min(Math.max(ServoPosition, 0), 1);
                ServoPositon2 = Math.min(Math.max(ServoPositon2, 0), 1);
                left_hand.setPosition(ServoPosition);
                right_hand.setPosition(ServoPositon2);
                // Prints data it gathers to Telemetry
                telemetry.addData("right_Pow", right_drive.getPower());
                telemetry.addData("left_Pow", left_drive.getPower());
                telemetry.addData("arm_movement Pow", arm_movement.getPower());
                telemetry.addData("Servo", ServoPosition);
                telemetry.addData("Servo2", ServoPositon2);
                telemetry.update();
            }
        }
    }


