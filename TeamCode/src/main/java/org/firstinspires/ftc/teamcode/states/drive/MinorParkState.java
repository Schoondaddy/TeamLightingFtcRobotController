package org.firstinspires.ftc.teamcode.states.drive;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.LeaveParkAuto;
import org.firstinspires.ftc.teamcode.states.RobotState;
import org.firstinspires.ftc.teamcode.states.StateCategory;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

public class MinorParkState extends RobotState<LM0Board, LeaveParkAuto> {

    private final PoseFactory p = opMode.p;
    private final Pose parkPose = p.of(133.2018, 34.567, 180);
    private final Follower follower = opMode.follower;
    public MinorParkState() {
        super(StateCategory.DRIVE); }

    @Override
    public void onStateStart() {
        schedule(follow(follower, park()));
    }

    @Override
    public void onStateLoop() {
        follower.update();
        Scheduler.execute();
    }

    @Override
    public void onStateEnd() {
    }
    private Path park() {
        Pose currentPose = board.getCurrentPedroPose();
        return line(currentPose, parkPose).linear(currentPose, parkPose);
    }
}
