package org.firstinspires.ftc.teamcode.states.intake;

import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.states.RobotState;
import org.firstinspires.ftc.teamcode.states.StateCategory;

/** Idle state for intake and passthrough*/
public class IdleState extends RobotState<LM0Board> {
    private final double INTAKE_POWER;
    private final double PASSTHROUGH_POWER;
    private final double POWER_STEP_SIZE;
    public IdleState() {
        super(StateCategory.INTAKE_PASSTHROUGH);
        INTAKE_POWER = 0.0;
        PASSTHROUGH_POWER = 0.0;
        POWER_STEP_SIZE = 10.0;
    }

    @Override
    public void onStateStart() {System.out.println("Intake idle");}

    @Override
    public void onStateLoop() {
        double currentIntakePower = board.getIntakeMotorPower();
        double currentPassthroughPower = board.getPassthroughMotorPower();

        if (currentIntakePower != INTAKE_POWER) {
            currentIntakePower -= POWER_STEP_SIZE * Math.signum(currentIntakePower);
            if (Math.abs(INTAKE_POWER - currentIntakePower) < POWER_STEP_SIZE){
                currentIntakePower = INTAKE_POWER;
            }
        }
        if (currentPassthroughPower != PASSTHROUGH_POWER) {
            currentPassthroughPower -= POWER_STEP_SIZE * Math.signum(currentPassthroughPower);
            if (Math.abs(PASSTHROUGH_POWER - currentPassthroughPower) < POWER_STEP_SIZE){
                currentPassthroughPower = PASSTHROUGH_POWER;
            }
        }

        board.cacheIntakeMotorPower(currentIntakePower);
        board.cachePassthroughMotorPower(currentPassthroughPower);
    }

    @Override
    public void onStateEnd() {}
}
