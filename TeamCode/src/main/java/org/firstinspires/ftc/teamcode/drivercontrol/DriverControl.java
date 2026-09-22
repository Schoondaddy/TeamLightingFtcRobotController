package org.firstinspires.ftc.teamcode.drivercontrol;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard;


/**
 * Driver Control is an abstract class for the classes that handle driver input logic
 * @param <B> The implementation of interface ProgrammingBoard which is used by subclasses of DriverControl
 */
public abstract class DriverControl<B extends ProgrammingBoard> {
    /** FTC telemetry interface used to send data to the Driver Station */
    protected Telemetry telemetry;
    /** Robot hardware used by a DriverControl subclass */
    protected B board;
    /** First driver gamepad */
    protected Gamepad gamepad1;
    /** Second driver gamepad */
    protected Gamepad gamepad2;
    /** The nominal, expected voltage */


    /**
     * Compares a value, typically a stick input, to a deadzone to filter unintended inputs/
     *
     * @param value The input value to test
     * @param deadzone The threshold below which the input is considered noise.
     * @return {@code 0.0} if the value is below the deadzone; otherwise the original value.
     */
    protected double applyDeadzone(double value, double deadzone) {
        if (Math.abs(value) > deadzone) {
            return value;
        }
        return 0.0;
    }

    /**
     *
     * @param value
     * @return
     */
    protected double quadraticScale(double value) {
        return Math.signum(value) * value * value;
    }

    /**
     *
     * @param values
     * @return
     */
    protected double multiMax(double...values) {
        double max = 0.0;
        for (double value : values) {
            max = Math.max(max, value);
        }
        return max;
    }

    /**
     *
     * @param values
     * @return
     */
    protected double multiAbsMax(double...values) {
        double[] absValues = values.clone();
        for (int i = 0; i < absValues.length; i++) {
            absValues[i] = Math.abs(absValues[i]);
        }
        return multiMax(absValues);
    }



    /**
     *
     * @param value
     * @return
     */
    protected double cubicScale(double value) {
        return value * value * value;
    }

    /**
     *
     * @param telemetry
     * @param board
     * @param gamepad1
     * @param gamepad2
     */
    public abstract void init(Telemetry telemetry, B board, Gamepad gamepad1, Gamepad gamepad2);

    /**
     *
     */
    public abstract void loop();
}