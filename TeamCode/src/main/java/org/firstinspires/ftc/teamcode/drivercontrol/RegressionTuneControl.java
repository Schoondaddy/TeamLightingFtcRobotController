package org.firstinspires.ftc.teamcode.drivercontrol;

import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.applyDeadzone;
import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.multiAbsMax;
import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.quadraticScale;

import android.text.BoringLayout;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.mechanisms.LM2Board;
import org.firstinspires.ftc.teamcode.opmodes.teleop.RegressionTunerTeleOp;
import org.firstinspires.ftc.teamcode.utils.CSVWebServer;
import org.firstinspires.ftc.teamcode.utils.ShotData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class RegressionTuneControl extends DriverControl<LM2Board, RegressionTunerTeleOp> {
    public File CSV;
    private CSVWebServer server;
    private Pose targetPose;
    private ElapsedTime matchTimer;
    public List<ShotData> shots = new ArrayList<>();
    public int dataPoints = 0;
    private boolean driveStop;
    private boolean intakeStop;

    private boolean hoodStop;
    private double flywheelVel;
    private double hoodAngle;
    public double distance;
    private double targetX;
    private double targetY;
    private Follower follower;

    private ElapsedTime uploadTimer;
    private boolean serverActive;
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

    public void init(Telemetry telemetry, LM2Board board, RegressionTunerTeleOp opMode, Gamepad gamepad1, Gamepad gamepad2) {

        this.board = board;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.telemetry = telemetry;

        this.driveStop = false;
        this.intakeStop = false;
        this.hoodStop = false;

        flywheelVel = 2500.0;
        hoodAngle = 0.5;
        uploadTimer = new ElapsedTime();
    }

    @Override
    public void start() {
        board.setFlywheelMotor1Vel(flywheelVel);
        board.setFlywheelMotor2Vel(flywheelVel);
        board.setAimServoPosition(hoodAngle);
        board.deployBlocker();
        matchTimer = new ElapsedTime();
        errorWindow.clear();
        errorSum = 0.0;
        follower = opMode.follower;
        targetPose = opMode.targetPose;
        targetX = targetPose.x();
        targetY = targetPose.y();
    }

    @Override
    public void loop() {
        if (serverActive) {
            return;
        }
        distance = follower.pose().distance(targetPose);


        double actualFlywheelVel = (board.getFlywheelMotor1Vel() + board.getFlywheelMotor2Vel()) / 2;
        double now = matchTimer.seconds();
        double currentError = Math.abs(flywheelVel - actualFlywheelVel);

        errorWindow.addLast(new ErrorSample(now, currentError));
        errorSum += currentError;

        while (!errorWindow.isEmpty() && (now - errorWindow.peekFirst().time > WINDOW_SECONDS)) {
            errorSum -= errorWindow.removeFirst().error;
        }

        double aveError = errorWindow.isEmpty() ? 0.0 : errorSum / errorWindow.size();

        if (gamepad1.bWasPressed()) {
            intakeStop = !intakeStop;
        }

        //uploads code, after 5 seconds of press
        if (gamepad1.startWasPressed()) {
            uploadTimer.reset();
        }
        if (gamepad1.start) {
            if (uploadTimer.seconds() > 5.0) {
                writeCSV();
                openServer();
                serverActive = true;
            }
        }

        if (gamepad1.leftTriggerWasPressed()) {
            Pose currentPose = follower.pose();
            driveStop = !driveStop;
            if (driveStop) {
                double angleToTarget = Math.atan2(targetY - currentPose.y(), targetX - currentPose.x());
                Pose aimPose = new Pose(currentPose.x(), currentPose.y(), angleToTarget);
                follower.hold(aimPose);
            } else {
                follower.stop();
            }
        }

        if (gamepad1.yWasPressed()) {
            hoodStop = !hoodStop;
        }

        if (!driveStop) {
            handleDrivetrain();
        } else {
            board.setDriveKillSwitch(true);
        }
        if (!intakeStop) {
            board.cacheIntakeMotorPower(0.75);
            board.cachePassthroughMotorPower(0.75);
        } else {
            board.cacheIntakeMotorPower(0.0);
            board.cachePassthroughMotorPower(0.0);
        }

        flywheelVel = handleFWheel();
        if (!hoodStop) {
            hoodAngle = handleHood();
        }

        if (gamepad1.rightTriggerWasPressed()) {
            board.retractBlocker();
        } else if (gamepad1.rightTriggerWasReleased()) {
            board.deployBlocker();
        }
        boolean xPressed = gamepad1.xWasPressed();
        if (driveStop && xPressed) {
            //logs the TARGET flywheel speed, not the actual speed
            shots.add(new ShotData(hoodAngle, flywheelVel, distance));
            dataPoints++;
        }

        telemetry.addData("Hood Angle", hoodAngle);
        telemetry.addData("Distance to target", distance);
        telemetry.addData("Target FW speed", flywheelVel);
        telemetry.addData("Actual FW speed: ", board.getFlywheelMotor1Vel());
        telemetry.addData("Past 5 second FW Inaccuracy", aveError);
        telemetry.addData("Data points", dataPoints);
        telemetry.addData("Voltage", board.getCachedVoltage());
        //telemetry.addData("PID coeffs: ", board.flywheelMotor1.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).toString());
    }
    public void stop() {
        if (server != null) {
            server.stop();
        } else {
            writeCSV(); //so the file will live on the hub even if the human forgot to download the csv in the opmode
        }
    }
    private void handleDrivetrain() {
        board.setDriveKillSwitch(false);
        double axial = -0.5 * quadraticScale(applyDeadzone(gamepad1.left_stick_y, 0.05));
        double lateral = 0.5 * quadraticScale(applyDeadzone(gamepad1.left_stick_x, 0.05));
        double yaw = 0.5 * quadraticScale(applyDeadzone(gamepad1.right_stick_x, 0.05));

        double flPower = axial + lateral + yaw;
        double frPower = axial - lateral - yaw;
        double blPower = axial - lateral + yaw;
        double brPower = axial + lateral - yaw;


        double max = multiAbsMax(flPower, frPower, blPower, brPower);

        if (max > 1.0) {
            flPower /= max;
            frPower /= max;
            blPower /= max;
            brPower /= max;
        }


        board.cacheFlMotorPower(flPower);
        board.cacheFrMotorPower(frPower);
        board.cacheBlMotorPower(blPower);
        board.cacheBrMotorPower(brPower);


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

    public double handleHood() {
        double increment = 0.0025;
        if (gamepad1.dpad_left) {
            ;
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

    public void writeCSV() {
        CSV = AppUtil.getInstance().getSettingsFile("shots.csv");

        try (BufferedWriter w = new BufferedWriter(new FileWriter(CSV))) {
            w.write(ShotData.HEADER);
            w.newLine();
            for (ShotData s : shots) {
                w.write(s.toCsvRow());
                w.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openServer() {
        server = new CSVWebServer(8085, CSV);
        try {
            server.start();
        } catch (IOException e) {
            telemetry.addData("Server Error", e.getMessage());
        }
    }
}