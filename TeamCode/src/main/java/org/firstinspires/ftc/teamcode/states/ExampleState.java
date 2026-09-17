package org.firstinspires.ftc.teamcode.states;
<<<<<<< Updated upstream
=======

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard;

>>>>>>> Stashed changes
public class ExampleState extends RobotState {

    public ExampleState(ProgrammingBoard board, Gamepad gamepad1, Gamepad gamepad2) {
        super(board, gamepad1, gamepad2);
    }

    @Override
    public void onStateStart() {
    }

    @Override
    public void onStateLoop() {
<<<<<<< Updated upstream
        if (stateTimer.seconds() > 10) {
            transitionState(new ExampleState2());
        }
=======

>>>>>>> Stashed changes
    }

    @Override
    public void onStateEnd() {

    }
}
