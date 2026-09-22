package org.firstinspires.ftc.teamcode.drivercontrol;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;import org.firstinspires.ftc.teamcode.mechanisms.ChassisBoard;


public class ChassisControl extends DriverControl<ChassisBoard> {
    private boolean slowMode;
    private double slowModeMult;
    private double nominalVoltage;
    private double currentVoltage;
    @Override
    public void init(Telemetry telemetry, ChassisBoard board, Gamepad gamepad1, Gamepad gamepad2) {
        this.board = board;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.telemetry = telemetry;
        slowMode = false;
        slowModeMult = 0.33;
        nominalVoltage = 12.0;
    }
    @Override
    public void loop() {
        currentVoltage = board.getCachedVoltage();

        if (gamepad1.aWasPressed()) {
            slowMode = !slowMode;
        }

        handleDrivetrain();
    }
    private void handleDrivetrain() {
        currentVoltage = board.getCachedVoltage();
        double axial = -quadraticScale(applyDeadzone(gamepad1.left_stick_y, 0.05));
        double lateral = quadraticScale(applyDeadzone(gamepad1.left_stick_x, 0.05));
        double yaw = quadraticScale(applyDeadzone(gamepad1.right_stick_x, 0.05));

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

        flPower = voltageClamp(flPower);
        frPower = voltageClamp(frPower);
        blPower = voltageClamp(blPower);
        brPower = voltageClamp(brPower);



        if (slowMode) {
            flPower *= slowModeMult;
            frPower *= slowModeMult;
            blPower *= slowModeMult;
            brPower *= slowModeMult;
        }


        board.setFlMotorPower(flPower);
        board.setFrMotorPower(frPower);
        board.setBlMotorPower(blPower);
        board.setBrMotorPower(brPower);



        telemetry.addData("Front left/Right", "%4.2f, %4.2f", flPower, frPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", blPower, brPower);
        telemetry.addData("Voltage", board.getCachedVoltage());
    }

    private double voltageClamp(double value) {
        double compFactor = nominalVoltage / currentVoltage;
        return Math.min(1.0, Math.max(-1.0, compFactor * value));
    }
}
