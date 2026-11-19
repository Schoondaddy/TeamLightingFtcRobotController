package org.firstinspires.ftc.teamcode.states.intakepassthrough;

/** Intake state for intake and passthrough*/
public class IntakePassthroughForward extends IntakePassthroughState {
    public IntakePassthroughForward() {
        super(0.75, 0.75, 0.4, 0.10);
    }

    @Override
    public void onStateStart() {telemetry.addData("Intake passthrough: ", "Forward");}

    @Override
    public void onStateEnd() {}
}
