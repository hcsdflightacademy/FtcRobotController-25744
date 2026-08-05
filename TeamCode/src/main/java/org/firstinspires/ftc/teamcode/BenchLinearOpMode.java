
package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

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
    private IMU imu;
    private YawPitchRollAngles imuOrientation;
    private HuskyLens huskyLens;
    private Servo rgbLight;
    final double kRED_PWM = 0.28;
    final double kGREEN_PWM = 0.5;
    final double kBLUE_PWM = 0.611;
    final double kWHITE_PWM = 1.0;

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

        rgbLight = hardwareMap.get(Servo.class, "rgbLight");
        rgbLight.setPosition(kWHITE_PWM); // set color to white

        imu = hardwareMap.get(IMU.class, "imu");
        /* Define how the hub is mounted on the robot to get the correct Yaw, Pitch and Roll values.
         * Two input parameters are required to fully specify the Orientation.
         * The first parameter specifies the direction the printed logo on the Hub is pointing.
         * The second parameter specifies the direction the USB connector on the Hub is pointing.
         * All directions are relative to the robot, and left/right is as-viewed from behind the robot.
         */
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                                                                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
        // Now initialize the IMU with this mounting orientation
        // Note: if you choose two conflicting directions, this initialization will cause a code exception.
        imu.initialize(new IMU.Parameters(orientationOnRobot));

        huskyLens = hardwareMap.get(HuskyLens.class, "huskylens");
        /*
         * Basic check to see if the huskyLens is alive and communicating.  This is not
         * technically necessary here as the HuskyLens class does this in its
         * doInitialization() method which is called when the device is pulled out of
         * the hardware map.  However, sometimes it's unclear why a device reports as
         * failing on initialization.  In the case of this device, it's because the
         * call to knock() failed.
         */
        if (!huskyLens.knock()) {
            telemetry.addData(">>", "Problem communicating with " + huskyLens.getDeviceName());
        } else {
            telemetry.addData(">>", "Press start to continue");
        }
        huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Show the elapsed game time.
            telemetry.addData("Status", "Run Time " + runtime.toString());

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
            } else if (gamepad1.xWasPressed()){
                if (servo2Position == SERVO2POS.UNKNOWN || servo2Position == SERVO2POS.ONE){
                    servo2Position = SERVO2POS.TWO;
                    servo2arm.setPosition(kServo2PositionTWO);
                } else { // servo2Postition == SERVO2POS.TWO
                    servo2Position = SERVO2POS.ONE;
                    servo2arm.setPosition(kServo2PositionONE);
                }
            }

            if (gamepad1.left_trigger_pressed){
                //servo6arm.setPosition(0.0);
                servo6arm.setPosition(0.5 - gamepad1.left_trigger/2.0);
            } else if (gamepad1.right_trigger_pressed){
                //servo6arm.setPosition(1.0);
                servo6arm.setPosition(0.5 + gamepad1.right_trigger/2.0);
            } else {
                servo6arm.setPosition(0.5);
            }

            // determine if R, G, or B is strongest detected color by REVColorSensorV3
            // and set rgbLight to that color when digitalTouch is pressed

            //if (!digitalTouch.getState()){
            if (digitalTouch.isPressed()){
                telemetry.addData("Button ", "PRESSED");
                if (sensorV3.getNormalizedColors().red > sensorV3.getNormalizedColors().green &&
                    sensorV3.getNormalizedColors().red > sensorV3.getNormalizedColors().blue) {
                    rgbLight.setPosition(kRED_PWM);
                } else if (sensorV3.getNormalizedColors().green > sensorV3.getNormalizedColors().blue) {
                    rgbLight.setPosition(kGREEN_PWM);
                } else {
                    rgbLight.setPosition(kBLUE_PWM);
                }
            } else {
                telemetry.addData("Button ", "NOT PRESSED");
                rgbLight.setPosition(kWHITE_PWM); // set color to white
            }

            // Check to see if heading reset is requested
            if (gamepad1.back) {
                telemetry.addData("Yaw", "Resetting");
                imu.resetYaw();
            } else {
                telemetry.addData("Yaw", "Press BACK on Gamepad to reset IMU");
            }
            // Retrieve Rotational Angles and Velocities
            imuOrientation = imu.getRobotYawPitchRollAngles();

            /*
             * All algorithms, except for LINE_TRACKING, return a list of Blocks where a
             * Block represents the outline of a recognized object along with its ID number.
             * ID numbers allow you to identify what the device saw.  See the HuskyLens documentation
             * referenced in the header comment above for more information on IDs and how to
             * assign them to objects.
             *
             * Returns an empty array if no objects are seen.
             */
            HuskyLens.Block[] blocks = huskyLens.blocks();
            telemetry.addData("Block count", blocks.length);
            for (int i = 0; i < blocks.length; i++) {
                telemetry.addData("Block", blocks[i].toString());
                /*
                 * Here inside the FOR loop, you could save or evaluate specific info for the currently recognized Bounding Box:
                 * - blocks[i].width and blocks[i].height   (size of box, in pixels)
                 * - blocks[i].left and blocks[i].top       (edges of box)
                 * - blocks[i].x and blocks[i].y            (center location)
                 * - blocks[i].id                           (Color ID)
                 *
                 * These values have Java type int (integer).
                 */
            }

            telemetry.addData("imu YPR (deg)", "%.1f %.1f %.1f",
                    imuOrientation.getYaw(AngleUnit.DEGREES),
                    imuOrientation.getPitch(AngleUnit.DEGREES),
                    imuOrientation.getRoll(AngleUnit.DEGREES));
            telemetry.addData("motor target & actual pwr", "%.2f %.2f", tgtPower, benchMotor.getPower());
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
