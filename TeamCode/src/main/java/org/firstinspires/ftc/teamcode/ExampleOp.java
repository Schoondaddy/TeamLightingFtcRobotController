package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp()
public class ExampleOp extends OpMode {
    private RobotState currentState;
    public ElapsedTime matchTimer;

    @Override
    public void init() {
        matchTimer = new ElapsedTime();
        currentState = new ExampleState();
        currentState.onStateStart();
    }

    @Override
    public void loop() {
        RobotState nextState = currentState.getNextState();

        if (nextState != currentState) {
            currentState.onStateEnd();
            currentState = nextState;
            currentState.onStateStart();
        } else {
            currentState.onStateLoop();
        }

        telemetry.addData("Active State", currentState.getClass().getSimpleName());
        telemetry.addData("State Time", currentState.stateTimer.seconds());
        telemetry.addData("Match Time", matchTimer.seconds());
    }
}
