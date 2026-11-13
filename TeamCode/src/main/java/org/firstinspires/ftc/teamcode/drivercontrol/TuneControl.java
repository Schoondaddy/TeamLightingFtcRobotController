package org.firstinspires.ftc.teamcode.drivercontrol;

import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.applyDeadzone;
import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.multiAbsMax;
import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.quadraticScale;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;

import java.util.ArrayList;

public class TuneControl extends DriverControl<LM0Board, OpMode>{
    ArrayList<Double> xList = new ArrayList<>();
    ArrayList<Double> yList = new ArrayList<>();
    private boolean driveStop;
    private boolean intakeStop;
    private boolean TPSStop;
    private boolean hoodStop;
    private double flywheelVel;
    private double hoodAngle;

    public void init(Telemetry telemetry, LM0Board board, OpMode opMode, Gamepad gamepad1, Gamepad gamepad2) {
        this.board = board;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.telemetry = telemetry;

        this.driveStop = false;
        this.intakeStop = false;
        this.TPSStop = true;
        this.hoodStop = true;

        flywheelVel = 980.0;
        hoodAngle = 0.66;
    }

    @Override
    public void start () {
        board.setFlywheelMotor1Vel(flywheelVel);
        board.setFlywheelMotor2Vel(flywheelVel);
        board.setAimServoPosition(hoodAngle);
    }

    @Override
    public void loop() {
        if (gamepad1.aWasPressed()) { driveStop = !driveStop; }

        if (gamepad1.bWasPressed()) { intakeStop = !intakeStop; }

        if (gamepad1.xWasPressed()) { TPSStop = !TPSStop; }

        if (gamepad1.yWasPressed()) { hoodStop = !hoodStop; }

        if (!driveStop) {handleDrivetrain();}
        if (!intakeStop) {
            board.cacheIntakeMotorPower(0.75);
            board.cachePassthroughMotorPower(0.75);
            board.setBlockerServoPower(1.0);
        } else {
            board.cacheIntakeMotorPower(0.0);
            board.cachePassthroughMotorPower(0.0);
            board.setBlockerServoPower(0.0);
        }

        if (!TPSStop) {flywheelVel = handleFWheel();}
        if (!hoodStop) {hoodAngle = handleHood();}



        if (gamepad1.startWasPressed()) {
            board.flipKillSwitch();
            gamepad1.rumbleBlips(3);
        }
        telemetry.addData("Hood Angle", hoodAngle);
        telemetry.addData("Target FW speed", flywheelVel);
        telemetry.addData("Actual FW speed: ", board.getFlywheelMotor1Vel());
        //telemetry.addData("PID coeffs: ", board.flywheelMotor1.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).toString());
    }
    private void handleDrivetrain() {
        double axial = -0.5 * quadraticScale(applyDeadzone(gamepad1.left_stick_y, 0.05));
        double lateral = 0.5 *quadraticScale(applyDeadzone(gamepad1.left_stick_x, 0.05));
        double yaw = 0.5 * quadraticScale(applyDeadzone(gamepad1.right_stick_x, 0.05));

        double flPower = axial + lateral + yaw;
        double frPower = axial - lateral - yaw;
        double blPower = axial - lateral + yaw;
        double brPower = axial + lateral - yaw;


        double max = multiAbsMax(flPower, frPower, blPower, brPower);

        if (max  > 1.0) {
            flPower /= max;
            frPower /= max;
            blPower /= max;
            brPower /= max;
        }


        board.cacheFlMotorPower(flPower);
        board.cacheFrMotorPower(frPower);
        board.cacheBlMotorPower(blPower);
        board.cacheBrMotorPower(brPower);



        telemetry.addData("Front left/Right", "%4.2f, %4.2f", flPower, frPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", blPower, brPower);
        telemetry.addData("Voltage", board.getCachedVoltage());
    }

    public double handleFWheel() {
        double increment = 2;
        if (gamepad1.dpad_up) {
            flywheelVel += increment;
            board.setFlywheelMotor1Vel(flywheelVel);
            board.setFlywheelMotor2Vel(flywheelVel);
        } else if (gamepad1.dpad_down) {
            flywheelVel -= increment;
            board.setFlywheelMotor1Vel(flywheelVel);
            board.setFlywheelMotor2Vel(flywheelVel);
        }
        return flywheelVel;
    }

    public  double handleHood() {
        double increment = 0.0025;
        if (gamepad1.dpad_left) {;
            hoodAngle -= increment;
            if (hoodAngle > 0.0) {
                board.setAimServoPosition(hoodAngle);
            } else {
                hoodAngle += increment;
            }
        } else if (gamepad1.dpad_right) {
            hoodAngle += increment;
            if (hoodAngle < 1.0) {
                board.setAimServoPosition(hoodAngle);
            } else {
                hoodAngle -= increment;
            }
        }
        return hoodAngle;
    }
}
