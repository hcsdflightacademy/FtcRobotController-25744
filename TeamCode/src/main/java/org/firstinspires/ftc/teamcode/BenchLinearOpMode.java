
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/*
 * This file contains a minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 */

@TeleOp(name="Bench 1 Linear OpMode", group="Linear OpMode")
//@Disabled
public class BenchLinearOpMode extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor benchMotor;
    private Servo servo2arm;
    private Servo servo6arm;
    private RevColorSensorV3 sensorV3;
    private RevTouchSensor button;
    private Servo rgbLight;


    @Override
    public void runOpMode() {
        benchMotor = hardwareMap.get(DcMotor.class, "motor");
        servo2arm = hardwareMap.get(Servo.class, "servo2arm");
        servo6arm = hardwareMap.get(Servo.class, "servo6arm");
        sensorV3 = hardwareMap.get(RevColorSensorV3.class, "sensorColorV3");
        button = hardwareMap.get(RevTouchSensor.class,"button");
        rgbLight = hardwareMap.get(Servo.class,"rgbLight");
        double tgtPower = 0;
        double jeff = 0;

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            tgtPower = gamepad1.left_stick_y;

            if(gamepad1.left_trigger_pressed) {
                servo2arm.setPosition(0.5);
            }else if(gamepad1.right_trigger_pressed) {
                servo2arm.setPosition(1);
            }
         // run until not pressed
            if (gamepad1.right_bumper ) {
                servo6arm.setPosition(1);
            }
            else if(gamepad1.left_bumper ) {
                servo6arm.setPosition(0);}
            else {
                servo6arm.setPosition(0.5);
            }

            sensorV3.getDistance(DistanceUnit.CM);

            if(button.isPressed() && tgtPower<0) {
                benchMotor.setPower(0);
            }
            else {
                benchMotor.setPower(tgtPower);
            }


            if(button.isPressed()) {
                rgbLight.setPosition(.288);
            }
            else if(gamepad1.right_stick_x>0) {
                rgbLight.setPosition(gamepad1.right_stick_x);
            }
            else {
                rgbLight.setPosition(0);
            }

            // Show the elapsed game time.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("benchMotor target power: ", tgtPower);
            telemetry.addData("benchMotor actual power: ", benchMotor.getPower());
            telemetry.addData("servo2arm position: ", servo2arm.getPosition());
            telemetry.addData("servo6arm speed: ", servo6arm.getPosition());
            telemetry.addData("sensorV3 object distance: ", sensorV3.getDistance (DistanceUnit.CM));
            telemetry.addData("button touched: ", button.getValue());
            telemetry.addData("rgb indicator color", rgbLight.getPosition());
            telemetry.update();
        }
    }
}
