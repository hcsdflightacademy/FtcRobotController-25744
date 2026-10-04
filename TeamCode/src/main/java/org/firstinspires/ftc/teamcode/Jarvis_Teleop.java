package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Jarvis: Teleop using Base25744Mecanum", group="Iterative OpMode")
public class Jarvis_Teleop extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();
    Base25744MecanumRobot2026 jarvis = new Base25744MecanumRobot2026();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        // Any code to configure/initialize jarvis should be in init(hardwareMap) method of Base25744MecanumRobot2026
        jarvis.init(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }


    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        //  Add any telemetry with instructions specific to this OpMode


        // Add all gamepad button mappings with calls to robot methods


        telemetry.update();
    }


}
