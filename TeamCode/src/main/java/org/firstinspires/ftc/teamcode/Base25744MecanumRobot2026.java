package org.firstinspires.ftc.teamcode;

// Modeled after MaristBaseRobot2026_Quad from 1 Oct 2026
// Class containing basic structure of FTC team 25744 robot
// with instances of all motor, servos, sensors, etc
// and generic functions to operate robot
// To use, create an instance in TeleOp or Autonomous routine
// Don't put any gamepad button mappings in this file

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Base25744MecanumRobot2026 {
    // Add all public variable, motors, servos, sensors, etc below
    private DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    DcMotor backLeftDrive;
    DcMotor backRightDrive;
    Servo rightIntakeServo;
    Servo leftIntakeServo;
    DcMotor intakeMotor;
    DcMotor shooterMotor;
    DcMotor kickerMotor;
    // This declares the IMU needed to get the current direction the robot is facing
    IMU imu;
    GoBildaPinpointDriver pinpoint;
    Limelight3A limelight;

    // Add any constants used to control the robot here


    // Constructor executed when instance is created
    public Base25744MecanumRobot2026 () {
        // leave blank as most configuration is completed in init() function
    }

    /* Initialize all Hardware components */
    public void init(HardwareMap hwMap) {
        //  Add all HardwareMap calls and complete initial configuration
        frontLeftDrive = hwMap.get(DcMotor.class, "front left");
        frontRightDrive = hwMap.get(DcMotor.class, "front right");
        backLeftDrive = hwMap.get(DcMotor.class, "back left");
        backRightDrive = hwMap.get(DcMotor.class, "back right");
        rightIntakeServo = hwMap. get(Servo.class,"right intake");
        leftIntakeServo = hwMap.get(Servo.class,"left intake");
        intakeMotor = hwMap.get(DcMotor.class,"intake motor");
        shooterMotor = hwMap.get(DcMotor.class, "shooter motor");
        kickerMotor = hwMap.get(DcMotor.class, "kicker motor");
        // Get a reference to the sensor
        pinpoint = hwMap.get(GoBildaPinpointDriver.class, "pinpoint");
        limelight = hwMap.get(Limelight3A.class, "limelight");

        // We set the left motors in reverse which is needed for drive trains where the left
        // motors are opposite to the right ones.
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        //backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        // This uses RUN_USING_ENCODER to be more accurate.   If you don't have the encoder
        // wires, you should remove these
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu = hwMap.get(IMU.class, "imu");
        // This needs to be changed to match the orientation on your robot
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

        RevHubOrientationOnRobot orientationOnRobot = new
                RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        // Configure the sensor
        configurePinpoint();
        // Set the location of the robot - this should be the place you are starting the robot from
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));

        //telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        //Starts polling for data.  If you neglect to call start(), getLatestResult() will return null.
        limelight.start();

    }

    // This routine drives the robot field relative
    public void driveFieldRelative(double forward, double right, double rotate) {
            // First, convert direction being asked to drive to polar coordinates
            double theta = Math.atan2(forward, right);
            double r = Math.hypot(right, forward);

            // Second, rotate angle by the angle the robot is pointing
            theta = AngleUnit.normalizeRadians(theta -
                    imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

            // Third, convert back to cartesian
            double newForward = r * Math.sin(theta);
            double newRight = r * Math.cos(theta);

            // Finally, call the drive method with robot relative forward and right amounts
            drive(newForward, newRight, rotate);
        }



    // This routine drives the robot relative to itself
    public void drive(double forward, double right, double rotate) {
        // This calculates the power needed for each wheel based on the amount of forward,
        // strafe right, and rotate
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        double maxPower = 1.0;
        double maxSpeed = 1.0;  // make this slower for outreaches

        // This is needed to make sure we don't pass > 1.0 to any wheel
        // It allows us to keep all of the motors in proportion to what they should
        // be and not get clipped
        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));

        // We multiply by maxSpeed so that it can be set lower for outreaches
        // When a young child is driving the robot, we may not want to allow full
        // speed.
        frontLeftDrive.setPower(maxSpeed * (frontLeftPower / maxPower));
        frontRightDrive.setPower(maxSpeed * (frontRightPower / maxPower));
        backLeftDrive.setPower(maxSpeed * (backLeftPower / maxPower));
        backRightDrive.setPower(maxSpeed * (backRightPower / maxPower));

        //telemetry.addData("front left power", frontLeftDrive.getPower());
        //telemetry.addData("front right power", frontRightDrive.getPower());
        //telemetry.addData("back left power", backLeftDrive.getPower());
        //telemetry.addData("back right power", backRightDrive.getPower());

    }

    public void runIntake(){
        intakeMotor.setPower(-1.0);
    }

    public void runIntakeBackwards(){
        intakeMotor.setPower(1.0);
    }

    public void stopIntake(){
        intakeMotor.setPower(0.0);
    }

    public void shootTheBall() {
        shooterMotor.setPower(0.38);
        kickerMotor.setPower(-0.5);
    }

    public void stopShooting() {
        shooterMotor.setPower(0.0);
        kickerMotor.setPower(0.0);
    }

    public void resetPosition() {
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
    }


    //  Add methods to operate all servos and get data from any sensors
    public void configurePinpoint(){
        /*
         *  Set the odometry pod positions relative to the point that you want the position to be measured from.
         *
         *  The X pod offset refers to how far sideways from the tracking point the X (forward) odometry pod is.
         *  Left of the center is a positive number, right of center is a negative number.
         *
         *  The Y pod offset refers to how far forwards from the tracking point the Y (strafe) odometry pod is.
         *  Forward of center is a positive number, backwards is a negative number.
         */
        pinpoint.setOffsets(85.0, -140.0, DistanceUnit.MM); //these are tuned for 3110-0002-0001 Product Insight #1

        /*
         * Set the kind of pods used by your robot. If you're using goBILDA odometry pods, select either
         * the goBILDA_SWINGARM_POD, or the goBILDA_4_BAR_POD.
         * If you're using another kind of odometry pod, uncomment setEncoderResolution and input the
         * number of ticks per unit of your odometry pod.  For example:
         *     pinpoint.setEncoderResolution(13.26291192, DistanceUnit.MM);
         */
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        /*
         * Set the direction that each of the two odometry pods count. The X (forward) pod should
         * increase when you move the robot forward. And the Y (strafe) pod should increase when
         * you move the robot to the left.
         */
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
        );

        /*
         * Before running the robot, recalibrate the IMU. This needs to happen when the robot is stationary
         * The IMU will automatically calibrate when first powered on, but recalibrating before running
         * the robot is a good idea to ensure that the calibration is "good".
         * resetPosAndIMU will reset the position to 0,0,0 and also recalibrate the IMU.
         * This is recommended before you run your autonomous, as a bad initial calibration can cause
         * an incorrect starting value for x, y, and heading.
         */
        pinpoint.resetPosAndIMU();
    }

}
