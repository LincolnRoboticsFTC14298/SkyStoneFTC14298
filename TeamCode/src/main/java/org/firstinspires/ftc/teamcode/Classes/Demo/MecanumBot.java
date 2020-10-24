package org.firstinspires.ftc.teamcode.Classes.Demo;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Classes.Demo.Parts.Mecanum;
import org.firstinspires.ftc.teamcode.Classes.Robot;
import org.firstinspires.ftc.teamcode.Classes.Demo.Parts.Claw;
import org.firstinspires.ftc.teamcode.Classes.Demo.Parts.Flipper;
import org.firstinspires.ftc.teamcode.Classes.Demo.Parts.FoundationClaw;
import org.firstinspires.ftc.teamcode.Classes.Demo.Parts.Intake;
import org.firstinspires.ftc.teamcode.Classes.RobotPart;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MecanumBot {
    public JSONObject settings;
    public Mecanum drivetrain = new Mecanum();
    public Claw claw = new Claw();
    public Intake intake = new Intake();
    public FoundationClaw foundationClaw = new FoundationClaw();
    public Flipper flipper = new Flipper();

    public RobotPart[] robotParts = new RobotPart[]{claw, intake, foundationClaw, flipper};

    private Robot robot = new Robot(drivetrain, robotParts);

    public void init(HardwareMap hardwareMap) {
        String filename = "MecanumBot.json";
        File file = AppUtil.getInstance().getSettingsFile(filename);
        try {
            settings = new JSONObject(ReadWriteFile.readFile(file));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        robot.loadJSON(settings);
        robot.init(hardwareMap);
    }

}
