package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

public abstract class RobotState {
    private RobotState nextState = this;
    protected final ElapsedTime stateTimer = new ElapsedTime();
    public RobotState() {}

    public abstract void onStateStart();
    public abstract void onStateEnd();
    public abstract void onStateLoop();

    protected void transitionState(RobotState newState) {
        this.nextState = newState;
    }

    public RobotState getNextState() {
        return nextState;
    }
}
