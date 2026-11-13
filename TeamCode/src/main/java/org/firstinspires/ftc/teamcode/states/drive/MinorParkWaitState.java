package org.firstinspires.ftc.teamcode.states.drive;


import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.LeaveParkAutoBlue;
import org.firstinspires.ftc.teamcode.states.RobotState;

public class MinorParkWaitState extends RobotState<LM0Board, LeaveParkAutoBlue> {
    ElapsedTime transitionTimer;
    double transitionTime = 3.0;
    public MinorParkWaitState() {
        super();

    }
    @Override
    public void onStateStart() {
        transitionTimer = new ElapsedTime();
      }

    @Override
    public void onStateLoop() {
        telemetry.addData("Time Remaining", transitionTime - transitionTimer.seconds());
        if (transitionTimer.seconds() > transitionTime) {
            transitionState(MinorParkState.class);
        }
    }

    @Override
    public void onStateEnd() {
    }
}
