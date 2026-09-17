package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

<<<<<<< Updated upstream:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/opmodes/teleop/ExampleOp.java
import org.firstinspires.ftc.teamcode.states.ExampleState;
import org.firstinspires.ftc.teamcode.states.RobotState;

=======
import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard;
import org.firstinspires.ftc.teamcode.states.RobotState;
import org.firstinspires.ftc.teamcode.states.ExampleState;
>>>>>>> Stashed changes:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/ExampleOp.java
@TeleOp()
public class ExampleOp extends OpMode {
    private ProgrammingBoard board;
    private RobotState currentState;
    public ElapsedTime matchTimer;

    @Override
    public void init() {
        board = new ProgrammingBoard(hardwareMap);
        matchTimer = new ElapsedTime();
        currentState = new ExampleState(board, gamepad1, gamepad2);
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
