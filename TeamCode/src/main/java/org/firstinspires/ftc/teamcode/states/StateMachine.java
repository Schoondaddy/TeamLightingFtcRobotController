package org.firstinspires.ftc.teamcode.states;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard;

public class StateMachine<B extends ProgrammingBoard> {
    /** FTC telemetry interface used to send data to the Driver Station*/
    private final Telemetry telemetry;
    /** Robot hardware used by a DriverControl subclass*/
    private final B board;
    /** First driver gamepad*/
    private final Gamepad gamepad1;
    /** Second driver gamepad*/
    private final Gamepad gamepad2;
    /** The current active state*/
    private RobotState<B> currentState;

    public StateMachine (Telemetry telemetry, B board, Gamepad gamepad1, Gamepad gamepad2) {
        this.telemetry = telemetry;
        this.board = board;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    /** Initializes the state machine, along with the initial state
     *
     * @param initState the state that the state machine will initiate
     */
    public void init(RobotState<B> initState) {
        currentState = initState;
        currentState.initializeState(telemetry, board, gamepad1, gamepad2);
        currentState.onStateStart();
    }
    /** Handles potential state changes, along with the current state's loop.
     * <p>This method should be called from the OpMode's {@link com.qualcomm.robotcore.eventloop.opmode.OpMode#loop() loop() method}</p>
     */
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
