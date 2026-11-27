package org.firstinspires.ftc.teamcode.drivercontrol;

import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.applyDeadzone;
import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.multiAbsMax;
import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.quadraticScale;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.LM2Board;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class RegressionTuneControl extends DriverControl<LM2Board, OpMode>{
    private ElapsedTime matchTimer;
    ArrayList<Double> xList = new ArrayList<>();
    ArrayList<Double> yList = new ArrayList<>();
    private boolean driveStop;
    private boolean intakeStop;
    private boolean FWStop;
    private boolean hoodStop;
    private double flywheelVel;
    private double hoodAngle;
    private ElapsedTime uploadTimer;
    private static class ErrorSample {
        final double time;
        final double error;
        ErrorSample(double time, double error) {
            this.time = time;
            this.error = error;
        }
    }
    private final Deque<ErrorSample> errorWindow = new ArrayDeque<>();
    private double errorSum = 0.0;
    private static final double WINDOW_SECONDS = 5.0;

    public void init(Telemetry telemetry, LM2Board board, OpMode opMode, Gamepad gamepad1, Gamepad gamepad2) {
        this.board = board;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.telemetry = telemetry;

        this.driveStop = false;
        this.intakeStop = false;
        this.FWStop = false;
        this.hoodStop = false;

        flywheelVel = 2500.0;
        hoodAngle = 0.5;
    }

    @Override
    public void start () {
        board.setFlywheelMotor1Vel(flywheelVel);
        board.setFlywheelMotor2Vel(flywheelVel);
        board.setAimServoPosition(hoodAngle);
        board.deployBlocker();
        matchTimer = new ElapsedTime();
        errorWindow.clear();
        errorSum = 0.0;
    }

    @Override
    public void loop() {

        double actualFlywheelVel = ( board.getFlywheelMotor1Vel() + board.getFlywheelMotor2Vel() )/ 2;
        double now = matchTimer.seconds();
        double currentError = Math.abs(flywheelVel - actualFlywheelVel);

        errorWindow.addLast(new ErrorSample(now, currentError));
        errorSum += currentError;

        while (!errorWindow.isEmpty() && (now - errorWindow.peekFirst().time > WINDOW_SECONDS)) {
            errorSum -= errorWindow.removeFirst().error;
        }

        double aveError = errorWindow.isEmpty() ? 0.0 : errorSum / errorWindow.size();
        if (gamepad1.aWasPressed()) { driveStop = !driveStop; }

        if (gamepad1.bWasPressed()) { intakeStop = !intakeStop; }

        //uploads code, after 5 seconds of press
        if (gamepad1.startWasPressed()) {uploadTimer.reset();}
        if(gamepad1.start) {
            if (uploadTimer.seconds() > 5.0) {
                //create the csv here
            }
        }

        if (gamepad1.yWasPressed()) { hoodStop = !hoodStop; }

        if (!driveStop) {handleDrivetrain();}
        if (!intakeStop) {
            board.cacheIntakeMotorPower(0.75);
            board.cachePassthroughMotorPower(0.75);
        } else {
            board.cacheIntakeMotorPower(0.0);
            board.cachePassthroughMotorPower(0.0);
        }

        if (!FWStop) {flywheelVel = handleFWheel();}
        if (!hoodStop) {hoodAngle = handleHood();}

        if (gamepad1.rightTriggerWasPressed()) {
            board.retractBlocker();
        } else if (gamepad1.rightTriggerWasReleased()) {
            board.deployBlocker();
        }

        telemetry.addData("Hood Angle", hoodAngle);
        telemetry.addData("Target FW speed", flywheelVel);
        telemetry.addData("Actual FW speed: ", board.getFlywheelMotor1Vel());
        telemetry.addData("Past 5 second FW Inaccuracy", aveError);
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
