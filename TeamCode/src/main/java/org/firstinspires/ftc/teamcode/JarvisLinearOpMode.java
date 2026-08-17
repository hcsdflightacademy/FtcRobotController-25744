
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


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

@TeleOp(name="Jarvis Drive Linear OpMode", group="Linear OpMode")
//@Disabled
public class BenchLinearOpMode extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontmotorLeft;
    private DcMotor frontmotorRight;
    private DcMotor rearmotorLeft;
    private DcMotor rearmotorRight;

    @Override
    public void runOpMode() {
        frontmotorLeft = hardwareMap.get(DcMotor.class, "front left");
        frontmotorRight = hardwareMap.get(DcMotor.class, "front right");
        rearmotorLeft = hardwareMap.get(DcMotor.class, "back left");
        rearmotorRight = hardwareMap.get(DcMotor.class, "back right");



        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            frontmotorLeft.setPower(gamepad1.left_stick_y);
            rearmotorLeft.setPower(gamepad1.left_stick_y);
            frontmotorRight.setPower(gamepad1.right_stick_y);
            rearmotorRight.setPower(gamepad1.right_stick_y);

            // Show the elapsed game time.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            //telemetry.addData("target benchMotor power: ", tgtPower);
            //telemetry.addData("benchMotor power: ", benchMotor.getPower());
            telemetry.update();
        }
    }
}
