package org.firstinspires.ftc.teamcode.states;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard;

public class StateMachine<B extends ProgrammingBoard> {
    private final Telemetry telemetry;
    private final B board;
    private final Gamepad gamepad1;
    private final Gamepad gamepad2;
    private RobotState<B> currentState;

    public StateMachine (Telemetry telemetry, B board, Gamepad gamepad1, Gamepad gamepad2) {
        this.telemetry = telemetry;
        this.board = board;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }
    public void init(RobotState<B> initState) {
        currentState = initState;
        currentState.initializeState(telemetry, board, gamepad1, gamepad2);
        currentState.onStateStart();
    }
    public void loop() {
        RobotState<B> nextState = currentState.getNextState();

        if (nextState != currentState) {
            currentState.onStateEnd();

            currentState = nextState;
            currentState.initializeState(telemetry, board, gamepad1, gamepad2);

            currentState.onStateStart();
        }

        currentState.onStateLoop();
    }
}
