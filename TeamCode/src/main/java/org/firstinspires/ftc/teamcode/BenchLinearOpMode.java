
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/*
 * This file contains a minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 */

@TeleOp(name="Bench 2 Linear OpMode", group="Linear OpMode")
//@Disabled
public class BenchLinearOpMode extends LinearOpMode {

    // Declare OpMode members.
    private final ElapsedTime runtime = new ElapsedTime();
    private DcMotor benchMotor;
    private Servo servo2arm;
    private Servo servo4arm;
    private DigitalChannel button1;
    private Rev2mDistanceSensor distanceSensor;
    private double distance;

    private DigitalChannel magnetism;
    @Override
    public void runOpMode() {
        benchMotor = hardwareMap.get(DcMotor.class, "motor 1");
        double tgtPower = 0;
        servo2arm = hardwareMap.get(Servo.class, "2arms");
        servo4arm = hardwareMap.get(Servo.class, "4arms");
        button1 = hardwareMap.get(DigitalChannel.class, "button");
        distanceSensor = hardwareMap.get(Rev2mDistanceSensor.class, "distance");
        magnetism = hardwareMap.get(DigitalChannel.class, "magnetism");
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            tgtPower = gamepad1.left_stick_y;
            //benchMotor.setPower(tgtPower);
            if (gamepad1.a) {
                servo2arm.setPosition(0.0);
            } else if (gamepad1.b) {
                servo2arm.setPosition(0.5);
            }
            if (gamepad1.right_trigger_pressed) {
                servo4arm.setPosition(0.0);
            } else if (gamepad1.left_trigger_pressed) {
                servo4arm.setPosition(0.5);
            }
//            if (button1.getState()) {
//                telemetry.addData("button1", "not pressed");
//                benchMotor.setPower(tgtPower);
//            } else if (tgtPower > 0.0) { //Move backwards
//                telemetry.addData("tgtPower", "more than 0");
//                benchMotor.setPower(tgtPower);
//            } else if (tgtPower < 0.0) { //Stop
//                telemetry.addData("tgtPower", "less than 0");
//                benchMotor.setPower(0);
//            }

            if (magnetism.getState()){
                telemetry.addData("magnetism", "magnet not found");
                benchMotor.setPower(tgtPower);
            } else if(tgtPower < 0.0) {
                telemetry.addData("magnetism", "magnet found");
                benchMotor.setPower(0);
            }else if (tgtPower > 0.0){
                benchMotor.setPower(tgtPower);
            }
            distance = distanceSensor.getDistance(DistanceUnit.CM);

            // Show the elapsed game time.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("benchMotor target power: ", tgtPower);
            telemetry.addData("benchMotor actual power: ", benchMotor.getPower());
            telemetry.addData("Servo2arm position", servo2arm.getPosition());
            telemetry.addData("Servo4arm position", servo4arm.getPosition());
            telemetry.addData("Distance (cm):", distance);
            telemetry.update();

        }
    }
}
