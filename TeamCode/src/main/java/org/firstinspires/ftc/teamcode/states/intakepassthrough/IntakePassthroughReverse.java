package org.firstinspires.ftc.teamcode.states.intakepassthrough;

/** Eject state for intake and passthrough*/
public class IntakePassthroughReverse extends IntakePassthroughState {
    public IntakePassthroughReverse() {
        super(-0.75, -0.75, 0.4, 0.10);
    }

    @Override
    public void onStateStart() {telemetry.addData("Intake passthrough: ", "Reverse");}

    @Override
    public void onStateEnd() {}
}
