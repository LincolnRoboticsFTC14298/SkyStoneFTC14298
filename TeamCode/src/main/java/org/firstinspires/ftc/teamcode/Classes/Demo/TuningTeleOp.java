package org.firstinspires.ftc.teamcode.Classes.Demo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewDcMotor;
import org.firstinspires.ftc.teamcode.Classes.ModifiedMotorsAndServos.NewServo;
import org.firstinspires.ftc.teamcode.Classes.RobotPart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;

import static java.lang.System.in;

@TeleOp(name = "TuningMecanumBot", group="Tuning")
public class TuningTeleOp extends LinearOpMode {
    private MecanumBot robot = new MecanumBot();
    JSONObject settings;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);

        waitForStart();

        while (!isStopRequested()) {
            settings = new JSONObject();
            //Cycle through parts for tuning
            //Change value with d-pad
            //Display original and current value
            //Save to json file

            for(RobotPart part : robot.robotParts) {
                NewDcMotor[] motors = part.motors;
                NewServo[] servos = part.servos;

                JSONObject partSettings = null;
                JSONArray allMotorSettings = null;
                JSONArray allServoSettings = null;

                try {
                    partSettings = settings.getJSONObject(part.name);
                    allMotorSettings = partSettings.getJSONArray("Motor");
                    allServoSettings = partSettings.getJSONArray("Servos");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int s = 0;
                while (s < servos.length) {
                    boolean done = false;
                    JSONObject servoSettings = null;

                    try {
                        // THEY MAY NOT BE THE SAME SERVOSSSSSSS/MOTORSSSSSSSSSSSSSS
                        servoSettings = allServoSettings.getJSONObject(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    int i = 0;
                    while(i < servoSettings.names().length()) {
                        String key = null;
                        Object oldValue = null;
                        Object newValue = null;
                        try {
                            key = servoSettings.names().getString(i);
                            if (servoSettings.get(key) instanceof JSONObject && key == "name") {
                                oldValue = servoSettings.get(key);
                                newValue = oldValue;
                                if (oldValue instanceof Double) {
                                    if (gamepad1.dpad_up) {
                                        newValue = Range.clip(((Double) newValue) + 0.05, 0, 1);
                                    } else if (gamepad1.dpad_down) {
                                        newValue = Range.clip(((Double) newValue) - 0.05, 0, 1);
                                    }
                                    servos[s].setPosition((Double) newValue);
                                } else if (oldValue instanceof Boolean) {
                                    if (gamepad1.dpad_up) {
                                        newValue = true;
                                    }
                                    if (gamepad1.dpad_down) {
                                        newValue = false;
                                    }
                                }
                                // Telemetry
                                telemetry.addLine()
                                        .addData("Part: ", part.getName())
                                        .addData("Servo: ", servos[s].getName());
                                telemetry.addLine()
                                        .addData("Original " + key + ": ", oldValue)
                                        .addData("New " + key + ": ", newValue);
                                telemetry.update();
                            } else {
                                i += 1;
                            }

                            if (gamepad1.x && (i < servoSettings.names().length() - 1)) {
                                i += 1;
                                servoSettings.put(key, newValue);
                            } else if (gamepad1.y && i > 1) {
                                i -= 1;
                                servoSettings.put(key, newValue);
                            }
                            if (gamepad1.a && s < servos.length - 1) {
                                s += 1;
                                servoSettings.put(key, newValue);
                                break;
                            } else if (gamepad1.b && s >= 1) {
                                s -= 1;
                                servoSettings.put(key, newValue);
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (gamepad1.left_bumper && gamepad1.right_bumper) {
                            done = true;
                            break;
                        }
                    }
                    if (done == true) {
                        break;
                    }
                }
            }
        }
        String filename = "NewMecanumBot.json";
        File file = AppUtil.getInstance().getSettingsFile(filename);
        ReadWriteFile.writeFile(file, String.valueOf(settings));
        telemetry.log().add("Settings saved to '%s'", filename);
    }
}
