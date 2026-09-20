package org.firstinspires.ftc.teamcode.states;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard;


public abstract class RobotState {
    protected RobotState nextState = this;
    protected Gamepad gamepad1;
    protected Gamepad gamepad2;
    protected ProgrammingBoard board;
    protected Telemetry telemetry;
    public final ElapsedTime stateTimer = new ElapsedTime();

    public void initializeState(Telemetry telemetry, ProgrammingBoard board, Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.board = board;
        this.telemetry = telemetry;
    }
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
