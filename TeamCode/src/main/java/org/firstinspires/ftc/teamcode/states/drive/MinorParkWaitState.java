package org.firstinspires.ftc.teamcode.states.drive;


import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.LeaveParkAutoBlue;
import org.firstinspires.ftc.teamcode.states.RobotState;

public class MinorParkWaitState extends RobotState<LM0Board, LeaveParkAutoBlue> {
    ElapsedTime transitionTimer;
    double transitionTime = 0.0;
    public MinorParkWaitState() {
        super();

    }
    @Override
    public void onStateStart() {
        transitionTimer = new ElapsedTime();
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
