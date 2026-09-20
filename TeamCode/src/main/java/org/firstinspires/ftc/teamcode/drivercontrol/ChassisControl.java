package org.firstinspires.ftc.teamcode.drivercontrol;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;import org.firstinspires.ftc.teamcode.mechanisms.BaseChassisBoard;
import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard;


public class ChassisControl extends DriverControl<BaseChassisBoard> {
    @Override
    public void init(Telemetry telemetry, BaseChassisBoard board, Gamepad gamepad1, Gamepad gamepad2) {
        this.board = board;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.telemetry = telemetry;
    }
    @Override
    public void loop() {
        currentVoltage = board.getCachedVoltage();
        final double DEADZONE = .05;
        double axial = -quadraticScale(applyDeadzone(gamepad1.left_stick_y, DEADZONE));
        double lateral = quadraticScale(applyDeadzone(gamepad1.left_stick_x, DEADZONE));
        double yaw = quadraticScale(applyDeadzone(gamepad1.right_stick_x, DEADZONE));

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

        board.setFlMotorPower(flPower);
        board.setFrMotorPower(frPower);
        board.setBlMotorPower(blPower);
        board.setBrMotorPower(brPower);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Front left/Right", "%4.2f, %4.2f", flPower, frPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", blPower, brPower);
        telemetry.update();
    }
}
