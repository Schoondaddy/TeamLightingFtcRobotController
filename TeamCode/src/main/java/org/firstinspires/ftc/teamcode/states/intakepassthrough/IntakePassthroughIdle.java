package org.firstinspires.ftc.teamcode.states.intakepassthrough;

/** Idle state for intake and passthrough*/
public class IntakePassthroughIdle extends IntakePassthroughState {
    public IntakePassthroughIdle() {
        super(0.0, 0.0, 0.4, 0.10);
    }

    @Override
    public void onStateStart() {telemetry.addData("Intake passthrough: ", "Idle");}

    @Override
    public void onStateEnd() {}
}
