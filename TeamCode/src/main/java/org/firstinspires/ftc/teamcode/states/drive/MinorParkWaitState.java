package org.firstinspires.ftc.teamcode.states.drive;


import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.LeaveParkAuto;
import org.firstinspires.ftc.teamcode.states.RobotState;

public class MinorParkWaitState extends RobotState<LM0Board, LeaveParkAuto> {
    ElapsedTime transitionTimer;
    double transitionTime = 0.0;
    public final Pose startPose = opMode.p.of(133.3809, 10.866, 180);
    public MinorParkWaitState() {
        super();

    }
    @Override
    public void onStateStart() {
        transitionTimer = new ElapsedTime();
        opMode.follower.setPose(startPose);
    }

    @Override
    public void onStateLoop() {
        if (transitionTimer.seconds() > transitionTime) {
            transitionState(new MinorParkState());
        }
    }

    @Override
    public void onStateEnd() {
    }
}
