package org.firstinspires.ftc.teamcode.states.intake;

import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.ExampleAutonomous;
import org.firstinspires.ftc.teamcode.states.RobotState;
import org.firstinspires.ftc.teamcode.states.StateCategory;

/** Eject state for intake and passthrough*/
public class EjectState extends RobotState<LM0Board, ExampleAutonomous> {
    private final double INTAKE_POWER;
    private final double PASSTHROUGH_POWER;
    private final double POWER_STEP_SIZE;

    public EjectState() {
        super(StateCategory.INTAKE_PASSTHROUGH);
        INTAKE_POWER = -0.5;
        PASSTHROUGH_POWER = 0.0;
        POWER_STEP_SIZE = 0.05;
    }

    @Override
    public void onStateStart() {
        telemetry.addData("Passthrough", "Ejecting");
    }

    @Override
    public void onStateLoop() {
        double currentIntakePower = board.getIntakeMotorPower();
        double currentPassthroughPower = board.getPassthroughMotorPower();

        if (currentIntakePower > INTAKE_POWER) {
            currentIntakePower -= POWER_STEP_SIZE;
            if (currentIntakePower < INTAKE_POWER) {
                currentIntakePower = INTAKE_POWER;
            }
        }
        if (currentPassthroughPower > PASSTHROUGH_POWER) {
            currentPassthroughPower -= POWER_STEP_SIZE;
            if (currentPassthroughPower < PASSTHROUGH_POWER) {
                currentPassthroughPower = PASSTHROUGH_POWER;
            }
        }

        board.cacheIntakeMotorPower(currentIntakePower);
        board.cachePassthroughMotorPower(currentPassthroughPower);
    }

    @Override
    public void onStateEnd() {}
}
