package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Jarvis: Auto 1", group="Linear OpMode")
public class Jarvis_Auto_1 extends LinearOpMode {
    Base25744MecanumRobot2026 jarvis = new Base25744MecanumRobot2026();
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {
        // Any code to configure/initialize jarvis should be in init(hardwareMap) method of Base25744MecanumRobot2026
        jarvis.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            //  Add any telemetry with instructions specific to this OpMode


            // Add all robot commands to execute during autonomous routine
            // Remember you can't use gamepads during Auto...



            telemetry.update();
        }
    }
}
