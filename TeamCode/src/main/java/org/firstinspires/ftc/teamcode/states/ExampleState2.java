package org.firstinspires.ftc.teamcode.states;

public class ExampleState2 extends RobotState {
    public ExampleState2() {}

    @Override
    public void onStateStart() {
    }

    @Override
    public void onStateLoop() {
        if (stateTimer.seconds() > 10) {
            transitionState(new ExampleState());
        }
    }

    @Override
    public void onStateEnd() {
    }
}
