
package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/*
 * This file contains a minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="Bench Linear OpMode", group="Linear OpMode")
//@Disabled
public class BenchLinearOpMode extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor benchMotor;
    //private DigitalChannel digitalTouch;
    private RevTouchSensor digitalTouch;
    private RevColorSensorV3 sensorV3;
    private Servo servo2arm;
    enum SERVO2POS {
        ONE, TWO, UNKNOWN
    }
    private SERVO2POS servo2Position = SERVO2POS.UNKNOWN;
    private double kServo2PositionONE = 0.3;
    private double kServo2PositionTWO = 0.55;
    private Servo servo6arm;

    @Override
    public void runOpMode() {
        benchMotor = hardwareMap.get(DcMotor.class, "motor");
        double tgtPower;

        //digitalTouch = hardwareMap.get(DigitalChannel.class, "button");
        //digitalTouch.setMode(DigitalChannel.Mode.INPUT);
        digitalTouch = hardwareMap.get(RevTouchSensor.class, "button");

        sensorV3 = hardwareMap.get(RevColorSensorV3.class, "sensorColorV3");

        servo2arm = hardwareMap.get(Servo.class, "servo2arm");
        servo6arm = hardwareMap.get(Servo.class, "servo6arm");


        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            tgtPower = this.gamepad1.left_stick_y;
            benchMotor.setPower(tgtPower);

            if (gamepad1.a){
                servo2arm.setPosition(0.0);
                servo2Position = SERVO2POS.UNKNOWN;
            } else if (gamepad1.b){
                servo2arm.setPosition(0.5);
                servo2Position = SERVO2POS.UNKNOWN;
            } else if (gamepad1.y){
                servo2arm.setPosition(1.0);
                servo2Position = SERVO2POS.UNKNOWN;
            } else if (gamepad1.x){
                if (servo2Position == SERVO2POS.UNKNOWN || servo2Position == SERVO2POS.ONE){
                    servo2Position = SERVO2POS.TWO;
                    servo2arm.setPosition(kServo2PositionTWO);
                } else { // servo2Postition == SERVO2POS.TWO
                    servo2Position = SERVO2POS.ONE;
                    servo2arm.setPosition(kServo2PositionONE);
                }
            }

            if (gamepad1.left_bumper){
                servo6arm.setPosition(0.0);
            } else if (gamepad1.right_bumper){
                servo6arm.setPosition(1.0);
            } else {
                servo6arm.setPosition(0.5);
            }

            //if (!digitalTouch.getState()){
            if (digitalTouch.isPressed()){
                telemetry.addData("Button ", "PRESSED");
            } else {
                telemetry.addData("Button ", "NOT PRESSED");
            }

            // Show the elapsed game time.
            telemetry.addData("Status", "Run Time " + runtime.toString());
            telemetry.addData("benchMotor target power", tgtPower);
            telemetry.addData("benchMotor actual power", benchMotor.getPower());
            telemetry.addData("V3 distance (cm)",  sensorV3.getDistance(DistanceUnit.CM));
            telemetry.addData("V3 color (nRGB)", "%.4f %.4f %.4f",
                    sensorV3.getNormalizedColors().red,
                    sensorV3.getNormalizedColors().green,
                    sensorV3.getNormalizedColors().blue);
            telemetry.addData("servo2arm position", servo2arm.getPosition() + " " + servo2Position);
            telemetry.addData("servo6arm position", servo6arm.getPosition());
            telemetry.update();
        }
    }
}
