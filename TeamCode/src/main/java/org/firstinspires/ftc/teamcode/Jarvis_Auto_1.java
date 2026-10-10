package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@Autonomous(name="Jarvis: Auto 1", group="Linear OpMode")
public class Jarvis_Auto_1 extends LinearOpMode {
    Base25744MecanumRobot2026 jarvis = new Base25744MecanumRobot2026();
    private ElapsedTime runtime = new ElapsedTime();
    final double SPEED_GAIN = 0.015;
    final double STRAFE_GAIN = 0.015;
    final double TURN_GAIN = 0.05;

    // Max Speeds
    final double MAX_AUTO_SPEED = 0.5;
    final double MAX_AUTO_STRAFE = 0.5;
    final double MAX_AUTO_TURN = 0.5;

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
        telemetry.addData("Status", "Run Time: " + runtime.toString());
            //  Add any telemetry with instructions specific to this OpMode


            // Add all robot commands to execute during autonomous routine
            // Remember you can't use gamepads during Auto...
            telemetry.addData("current Pose", jarvis.pinpoint.getPosition());

            Pose2D targetPos1 = new Pose2D(DistanceUnit.INCH,76 , 0, AngleUnit.DEGREES, 0);
            Pose2D targetPos2 = new Pose2D(DistanceUnit.INCH, 76, 20, AngleUnit.DEGREES, 0);
            Pose2D targetPos3 = new Pose2D(DistanceUnit.INCH, 76, 20, AngleUnit.DEGREES, 180);
            goToPose(targetPos1, 5, 20);
            telemetry.addData("Position 1 Reached","Hopefully");
            telemetry.update();

            delay(0.5);
            goToPose(targetPos2, 5, 20);
            telemetry.addData("Position 2 Reached","Hopefully");
            telemetry.update();

            delay(.5);
            goToPose(targetPos3, 5, 20);
            telemetry.addData("Position 3 Reached","Hopefully");
            telemetry.update();

    }

    public void delay(double t) { // Imitates the Arduino delay function
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < t)) {
            //telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            //telemetry.update();
        }
    }
    public void goToPose(Pose2D targetPos, double  positionThreshold, double headingThreshold) {

        // Current Data
        Pose2D currentPos = jarvis.pinpoint.getPosition();
        double xPos = currentPos.getX(DistanceUnit.INCH);
        double yPos = currentPos.getY(DistanceUnit.INCH);
        double thetaPos = currentPos.getHeading(AngleUnit.DEGREES);

        // Target Data
        double xTarget = targetPos.getX(DistanceUnit.INCH);
        double yTarget = targetPos.getY(DistanceUnit.INCH);
        double thetaTarget = targetPos.getHeading(AngleUnit.DEGREES);

        double drive = 0;
        double turn = 0;
        double strafe = 0;

        // TODO: Figure out Exit
        // For Now Using Threshold of distance
        while(opModeIsActive() &&
                ((getPoseDistance(currentPos, targetPos) > positionThreshold) ||
                (Math.abs(targetPos.getHeading(AngleUnit.DEGREES) - currentPos.getHeading(AngleUnit.DEGREES)) > headingThreshold))){
           jarvis. pinpoint.update();

            currentPos = jarvis.pinpoint.getPosition();
            xPos = currentPos.getX(DistanceUnit.INCH);
            yPos = currentPos.getY(DistanceUnit.INCH);
            thetaPos = currentPos.getHeading(AngleUnit.DEGREES);

            // Compute Differences
            double xError = xTarget - xPos;
            double yError = yTarget - yPos;
            double yawError = thetaTarget - thetaPos;

            drive = Range.clip(xError*SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
            turn = Range.clip(yawError*TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN);
            strafe = Range.clip(yError*STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);

            jarvis.driveFieldRelative(drive, -strafe, -turn);

            telemetry.addData("current Pose", currentPos);
            telemetry.addData("Target Pose", targetPos);
            telemetry.addData("Pose Distance", getPoseDistance(currentPos, targetPos));
            telemetry.addData("X position", currentPos.getX(DistanceUnit.INCH));
            telemetry.addData("X target position", targetPos.getX(DistanceUnit.INCH));
            telemetry.addData("y postion", currentPos.getY(DistanceUnit.INCH));
            telemetry.addData("y  target postion", targetPos.getY(DistanceUnit.INCH));
            telemetry.addData("Turn", turn);
            telemetry.addData("Yaw Error", yawError);
            telemetry.update();
        }

        jarvis.driveFieldRelative( 0, 0, 0);

    }

    private double getPoseDistance(Pose2D pos1, Pose2D pos2) {
        double x1 = pos1.getX(DistanceUnit.INCH);
        double y1 = pos1.getY(DistanceUnit.INCH);
        //double theta1 = pos1.getHeading(AngleUnit.DEGREES);

        double x2 = pos2.getX(DistanceUnit.INCH);
        double y2 = pos2.getY(DistanceUnit.INCH);
        //double theta2 = pos2.getHeading(AngleUnit.DEGREES);

        double diff = Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2); //+ Math.pow((theta1 - theta2), 2);
        double distance = Math.sqrt(diff);

        return distance;

    }
}
