package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.TestProgrammingBoard;
import org.firstinspires.ftc.teamcode.states.ExampleState;
import org.firstinspires.ftc.teamcode.states.StateMachine;

@TeleOp()
public class ExampleOp extends OpMode {
    TestProgrammingBoard board;
    StateMachine sm;
    public ElapsedTime matchTimer;

    @Override
    public void init() {
        board = new TestProgrammingBoard();
        sm = new StateMachine(telemetry, board, gamepad1, gamepad2);
        board.init(hardwareMap);

        matchTimer = new ElapsedTime();
        sm.init(new ExampleState());
    }

    @Override
    public void loop() {
        sm.update();
        board.loop();
    }
}
