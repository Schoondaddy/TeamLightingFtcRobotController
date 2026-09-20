package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.mechanisms.TestBoard;
import org.firstinspires.ftc.teamcode.states.StateMachine;

@Disabled
@Autonomous()
public class ExampleAutonomous extends OpMode {
    TestBoard board;
    StateMachine<TestBoard> sm;
    public ElapsedTime matchTimer;

    @Override
    public void init() {
        board = new TestBoard();
        board.init(hardwareMap);
        sm = new StateMachine<>(telemetry, board, gamepad1, gamepad2);
    }

    @Override
    public void loop() {
        board.loop();
        sm.loop();
    }
}
