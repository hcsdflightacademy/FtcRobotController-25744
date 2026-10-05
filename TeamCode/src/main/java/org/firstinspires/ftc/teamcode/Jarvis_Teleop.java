package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

import java.util.List;

@TeleOp(name="Jarvis: Teleop", group="Iterative OpMode")
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
        telemetry.addLine("Press A to reset Yaw");
        telemetry.addLine("Hold left bumper to drive in robot relative");
        //telemetry.addLine("The left joystick sets the robot direction");
        //telemetry.addLine("Moving the right joystick left and right turns the robot");
        //telemetry.addLine("Push your robot around to see it track");
        telemetry.addLine("Press B to reset the position");

        // If you press the A button, then you reset the Yaw to be zero from the way
        // the robot is currently pointing
        if (gamepad1.a) {
            jarvis.imu.resetYaw();
        }
        // If you press the left bumper, you get a drive from the point of view of the robot
        // (much like driving an RC vehicle)
        if (gamepad1.left_bumper) {
            jarvis.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        } else {
            jarvis.driveFieldRelative(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        }
        if (gamepad1.right_bumper) {
            jarvis.rightIntakeServo.setPosition(1.0);
            jarvis.leftIntakeServo.setPosition(-1.0);
        }else {
            jarvis.rightIntakeServo.setPosition(0.5);
            jarvis.leftIntakeServo.setPosition(0.5);
        }

        if(gamepad1.b){
            // You could use readings from April Tags here to give a new known position to the pinpoint
            jarvis.pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
        }

        if(gamepad1.dpad_up){
            jarvis.intakeMotor.setPower(1.0);
        }else if (gamepad1.dpad_down){
            jarvis.intakeMotor.setPower(-1.0);
        }else{
            jarvis.intakeMotor.setPower(0.0);
        }

        if(gamepad1.left_trigger_pressed){
            jarvis.shooterMotor.setPower(-.4);
            jarvis.kickerMotor.setPower(-.25);
        }else {
            jarvis.shooterMotor.setPower(0.0);
            jarvis.kickerMotor.setPower(0.0);
        }




        jarvis.pinpoint.update();
        Pose2D pose2D = jarvis.pinpoint.getPosition();

        telemetry.addData("X coordinate (IN)", pose2D.getX(DistanceUnit.INCH));
        telemetry.addData("Y coordinate (IN)", pose2D.getY(DistanceUnit.INCH));
        telemetry.addData("Heading angle (DEGREES)", pose2D.getHeading(AngleUnit.DEGREES));

        LLStatus status = jarvis.limelight.getStatus();
        telemetry.addData("Name", "%s",
                status.getName());
        telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
                status.getTemp(), status.getCpu(),(int)status.getFps());
        telemetry.addData("Pipeline", "Index: %d, Type: %s",
                status.getPipelineIndex(), status.getPipelineType());

        LLResult result = jarvis.limelight.getLatestResult();
        if (result.isValid()) {
            // Access general information
            double captureLatency = result.getCaptureLatency();
            double targetingLatency = result.getTargetingLatency();
            double parseLatency = result.getParseLatency();
            telemetry.addData("LL Latency", captureLatency + targetingLatency);
            telemetry.addData("Parse Latency", parseLatency);
            telemetry.addData("tx", result.getTx());
            telemetry.addData("txnc", result.getTxNC());
            telemetry.addData("ty", result.getTy());
            telemetry.addData("tync", result.getTyNC());

            // Access fiducial results
            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
            for (LLResultTypes.FiducialResult fr : fiducialResults) {
                telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f", fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());
            }
        } else {
            telemetry.addData("Limelight", "No data available");
        }
        telemetry.update();
        //  Add any telemetry with instructions specific to this OpMode


        // Add all gamepad button mappings with calls to robot methods


        telemetry.update();
    }


}
