package org.firstinspires.ftc.teamcode.states.intakepassthrough;

/** Intake state for intake and passthrough*/
public class IntakeReverse extends IntakePassthroughState {
    public IntakeReverse() {
        super(-0.75, 0.0, 0.4, 0.10);
    }

    @Override
    public void onStateStart() {telemetry.addData("Intake: ", "Forward");}

    @Override
    public void onStateEnd() {}
}
