package org.firstinspires.ftc.teamcode.drivercontrol;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard;


public abstract class DriverControl<B extends ProgrammingBoard> {
    protected Telemetry telemetry;
    protected B board;
    protected Gamepad gamepad1;
    protected Gamepad gamepad2;
    protected double nominalVoltage;
    protected double currentVoltage;
    protected double applyDeadzone(double value, double deadzone) {
        if (Math.abs(value) > deadzone) {
            return value;
        }
        return 0.0;
    }
    protected double quadraticScale(double value) {
        return Math.signum(value) * value * value;
    }
    protected double multiMax(double...values) {
        double max = values[0];
        for (double value : values) {
            max = Math.max(max, value);
        }
        return max;
    }
    protected double multiAbsMax(double...values) {
        for (double value : values) {
            value = Math.abs(value);
        }
        return multiMax(values);
    }
    protected double voltageClamp(double value) {
        double compFactor = nominalVoltage / currentVoltage;
        return Math.min(1.0, Math.max(-1.0, compFactor * value));
    }
    protected double cubicScale(double value) {
        return value * value * value;
    }
    public abstract void init(Telemetry telemetry, B board, Gamepad gamepad1, Gamepad gamepad2);
    public abstract void loop();
}