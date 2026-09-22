package org.firstinspires.ftc.teamcode.drivercontrol;

public class ControlUtils {
    /**
     * Compares a value, typically a stick input, to a deadzone to filter unintended inputs
     *
     * @param value The input value to test
     * @param deadzone The threshold below which the input is considered noise.
     * @return {@code 0.0} if the value is less than or equal to the deadzone; otherwise the original value.
     */
    public static double applyDeadzone(double value, double deadzone) {
        if (Math.abs(value) > deadzone) {
            return value;
        }
        return 0.0;
    }

    /** Scales an input quadratically.
     *
     * @param value the input value, typically in the range {@code [-1.0, 1.0]}
     * @return the quadratically scaled value
     */
    public static double quadraticScale(double value) {
        return Math.signum(value) * value * value;
    }

    /**
     * Scales an input cubically.
     * @param value the input value, typically in the range {@code [-1.0, 1.0]}
     * @return the cubically scaled value
     */
    public static double cubicScale(double value) {
        return value * value * value;
    }
    /**Finds the max value of an array of doubles
     *
     * @param values an array of doubles with arbitrary length
     * @return {@code 0.0} if fed an empty array; the max value of the array otherwise
     */
    public static double multiMax(double...values) {
        if (values.length == 0) {
            return 0.0;
        }
        double max = values[0];
        for (double value : values) {
            max = Math.max(max, value);
        }
        return max;
    }

    /**Finds the max absolute value of an array of doubles
     *
     * @param values an array of doubles with arbitrary length
     * @return {@code 0.0} if fed an empty array; the max absolute value of the array otherwise
     */
    public static double multiAbsMax(double...values) {
        double[] absValues = values.clone();
        for (int i = 0; i < absValues.length; i++) {
            absValues[i] = Math.abs(absValues[i]);
        }
        return multiMax(absValues);
    }
}
