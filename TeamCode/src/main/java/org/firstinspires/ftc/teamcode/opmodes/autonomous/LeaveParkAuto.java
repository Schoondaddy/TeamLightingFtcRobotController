package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.mechanisms.TestBoard;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.states.StateCategory;
import org.firstinspires.ftc.teamcode.states.StateMachine;

public class LeaveParkAuto extends OpMode {
    public Follower follower;
    public final PoseFactory p = PoseFactory.degrees();
    /** The hardware the Autonomous uses */
    LM0Board board;
    /** The state machine */
    StateMachine<LM0Board, LeaveParkAuto> driveSM;
    /** The time that has elapsed during the match */
    public ElapsedTime matchTimer;
    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        board = new LM0Board();
        board.init(hardwareMap);
        driveSM = new StateMachine<>(this, telemetry, board, gamepad1, gamepad2, StateCategory.DRIVE);
    }

    @Override
    public void loop() {
        board.readLoop();
        driveSM.loop();
        board.writeLoop();
    }
}
