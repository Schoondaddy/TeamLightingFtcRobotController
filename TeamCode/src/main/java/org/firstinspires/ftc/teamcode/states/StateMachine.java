package org.firstinspires.ftc.teamcode.states;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard;

public class StateMachine {
    private final Telemetry telemetry;
    private final ProgrammingBoard board;
    private final Gamepad gamepad1;
    private final Gamepad gamepad2;
    private RobotState currentState;

    public StateMachine (Telemetry telemetry, ProgrammingBoard board, Gamepad gamepad1, Gamepad gamepad2) {
        this.telemetry = telemetry;
        this.board = board;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }
    public void init(RobotState initState) {
        currentState = initState;
        currentState.initializeState(telemetry, board, gamepad1, gamepad2);
        currentState.onStateStart();
    }
    public void update() {
        RobotState nextState = currentState.getNextState();

        if (nextState != currentState) {
            currentState.onStateEnd();

            currentState = nextState;
            currentState.initializeState(telemetry, board, gamepad1, gamepad2);

            currentState.onStateStart();
        }

        currentState.onStateLoop();
    }
}
