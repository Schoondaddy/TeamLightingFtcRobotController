package org.firstinspires.ftc.teamcode.states.intakepassthrough;

import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.ExampleAutonomous;
import org.firstinspires.ftc.teamcode.states.RobotState;
import org.firstinspires.ftc.teamcode.states.StateCategory;

/** Intake state for intake and passthrough*/
public class IntakeReverse extends IntakePassthroughState {
    public IntakeReverse() {
        super(-0.5, 0.0, 0.4, 0.10);
    }

    @Override
    public void onStateStart() {telemetry.addData("Intake: ", "Forward");}

    @Override
    public void onStateEnd() {}
}
