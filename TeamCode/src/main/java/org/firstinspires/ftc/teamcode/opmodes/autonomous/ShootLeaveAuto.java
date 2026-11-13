package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.states.StateCategory;
import org.firstinspires.ftc.teamcode.states.StateMachine;
import org.firstinspires.ftc.teamcode.states.drive.OffWallDriveState;
import org.firstinspires.ftc.teamcode.states.drive.WaitShootState;
import org.firstinspires.ftc.teamcode.states.turretaim.ShootState;

@Autonomous
public class ShootLeaveAuto extends OpMode {
    public final boolean allianceFlip = false;
    public Follower follower;
    public PoseFactory p = PoseFactory.degrees();
    /** The hardware the Autonomous uses */
    LM0Board board;
    /** The state machine */
    StateMachine<LM0Board, ShootLeaveAuto> driveSM;
    StateMachine<LM0Board, ShootLeaveAuto> shootSM;
    /** The time that has elapsed during the match */
    public ElapsedTime matchTimer;
    @Override
    public void init() {
        if (allianceFlip) {
            p = p.mirrorY(70.75);
        }
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        board = new LM0Board();
        board.init(hardwareMap);
        driveSM = new StateMachine<>(this, telemetry, board, gamepad1, gamepad2, StateCategory.DRIVE);
        shootSM = new StateMachine<>(this, telemetry, board, gamepad1, gamepad2, StateCategory.TURRET_AIM);

    }
    @Override
    public void start() {
        driveSM.init(new OffWallDriveState());
        shootSM.init(new ShootState());

    }

    @Override
    public void loop() {
        board.readLoop();
        driveSM.loop();
        shootSM.loop();
        board.writeLoop();
    }
}
