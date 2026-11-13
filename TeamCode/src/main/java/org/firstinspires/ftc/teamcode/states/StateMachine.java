package org.firstinspires.ftc.teamcode.states;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard;

import java.util.HashMap;

public class StateMachine<B extends ProgrammingBoard, O extends OpMode> {
    public HashMap<Class<? extends RobotState<B, O>>, RobotState<B,O>> stateCache = new HashMap<>();
    private final O opMode;
    /** FTC telemetry interface used to send data to the Driver Station*/
    private final Telemetry telemetry;
    /** Robot hardware used by a DriverControl subclass*/
    private final B board;
    /** First driver gamepad*/
    private final Gamepad gamepad1;
    /** Second driver gamepad*/
    private final Gamepad gamepad2;
    /** The current active state*/
    private RobotState<B, O> currentState;
    private final StateCategory stateCategory;
    public StateMachine (O opMode, Telemetry telemetry, B board, Gamepad gamepad1, Gamepad gamepad2, StateCategory category) {
        this.opMode = opMode;
        this.telemetry = telemetry;
        this.board = board;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.stateCategory = category;
    }

    /** Initializes the state machine, along with the initial state
     *
     * @param initState the state that the state machine will initiate
     */
    public void init(RobotState<B, O> initState) {
        currentState = initState;
        currentState.initializeState(opMode, telemetry, board, gamepad1, gamepad2);
        currentState.onStateStart();
    }
    /** Handles potential state changes, along with the current state's loop.
     * <p>This method should be called from the OpMode's {@link com.qualcomm.robotcore.eventloop.opmode.OpMode#loop() loop() method}</p>
     */
    public void loop() {
        Class<? extends RobotState<B, O>> nextStateClass = currentState.getNextState();
        if (nextStateClass != currentState.getClass()) {
            RobotState<B, O> nextState;
            if (stateCache.get(nextStateClass) == null) {
                try {
                    nextState = nextStateClass.getDeclaredConstructor().newInstance();
                    stateCache.put(nextStateClass, nextState);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                nextState = stateCache.get(nextStateClass);
            }
        }

        currentState.onStateLoop();
    }

    public RobotState<B, O> getCurrentState() {
        return currentState;
    }
}
