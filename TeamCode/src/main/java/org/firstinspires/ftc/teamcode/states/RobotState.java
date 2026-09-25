package org.firstinspires.ftc.teamcode.states;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard;


public abstract class RobotState<B extends ProgrammingBoard>{
    /** The state that will run in the next loop iteration; is the current state when not transitioning states */
    protected RobotState<B> nextState = this;
    /** First driver gamepad*/
    protected Gamepad gamepad1;
    /** Second driver gamepad*/
    protected Gamepad gamepad2;
    /** Robot hardware used by a DriverControl subclass */
    protected B board;
    /** FTC telemetry interface used to send data to the Driver Station */
    protected Telemetry telemetry;
    /** The time that has elapsed since the state was started
     *  <p>Starts tracking time the loop BEFORE the state's first loop</p>
     * */
    public final ElapsedTime stateTimer = new ElapsedTime();

    /** Called  to initialize the current state's member variables*/
    public void initializeState(Telemetry telemetry, B board, Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.board = board;
        this.telemetry = telemetry;
    }

    /** Called when the state is transitioned into*/
    public abstract void onStateStart();

    /** Called when the state is transitioned out of */
    public abstract void onStateEnd();
    /** Runs one instance of the current state's loop */
    public abstract void onStateLoop();

    /** Called to transition into the next state
     *
     * @param newState the next state
     */
    public void transitionState(RobotState<B> newState) {
        this.nextState = newState;
    }

    /** Getter method for the next state */
    public RobotState<B> getNextState() {
        return nextState;
    }
}
