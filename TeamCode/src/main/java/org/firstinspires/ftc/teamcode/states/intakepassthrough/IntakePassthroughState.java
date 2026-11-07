package org.firstinspires.ftc.teamcode.states.intakepassthrough;

import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.ExampleAutonomous;
import org.firstinspires.ftc.teamcode.opmodes.teleop.LM0TeleOp;
import org.firstinspires.ftc.teamcode.states.RobotState;
import org.firstinspires.ftc.teamcode.states.StateCategory;

/** Intake state for intake */
public abstract class IntakePassthroughState extends RobotState<LM0Board, LM0TeleOp> {
    private final double INTAKE_POWER;
    private final double PASSTHROUGH_POWER;
    private final double POWER_STEP_FACTOR;
    private final double POWER_SNAP_THRESHOLD;
    public IntakePassthroughState(double INTAKE_POWER, double PASSTHROUGH_POWER, double POWER_STEP_FACTOR, double POWER_SNAP_THRESHOLD) {
        super(StateCategory.INTAKE_PASSTHROUGH);
        this.INTAKE_POWER = INTAKE_POWER;
        this.PASSTHROUGH_POWER = PASSTHROUGH_POWER;
        this.POWER_STEP_FACTOR = POWER_STEP_FACTOR;
        this.POWER_SNAP_THRESHOLD = POWER_SNAP_THRESHOLD;
    }

    @Override
    public void onStateLoop() {
        double currentIntakePower = board.getIntakeMotorPower();
        double currentPassthroughPower = board.getPassthroughMotorPower();

        if (currentIntakePower != INTAKE_POWER) {
            if (Math.abs(currentIntakePower - INTAKE_POWER) < POWER_SNAP_THRESHOLD) {
                currentIntakePower = INTAKE_POWER;
            }
            else {
                currentIntakePower = lerpPower(currentIntakePower, INTAKE_POWER, POWER_STEP_FACTOR);
            }
        }

        if (currentPassthroughPower != PASSTHROUGH_POWER) {
            if (Math.abs(currentPassthroughPower - PASSTHROUGH_POWER) < POWER_SNAP_THRESHOLD) {
                currentPassthroughPower = PASSTHROUGH_POWER;
            }
            else {
                currentPassthroughPower = lerpPower(currentPassthroughPower, PASSTHROUGH_POWER, POWER_STEP_FACTOR);
            }
        }

        board.cacheIntakeMotorPower(currentIntakePower);
        board.cachePassthroughMotorPower(currentPassthroughPower);
    }

    /** Power lerp smoothing */
    private double lerpPower(double current, double target, double stepFactor) {
        return current + (target - current) * stepFactor;
    }
}
