package org.firstinspires.ftc.teamcode.states.drive;


import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.LeaveParkAutoBlue;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.ShootLeaveAuto;
import org.firstinspires.ftc.teamcode.states.RobotState;

public class WaitShootState extends RobotState<LM0Board, ShootLeaveAuto> {
    ElapsedTime transitionTimer;
    double transitionTime = 3.0;
    public WaitShootState() {
        super();
    }
    @Override
    public void onStateStart() {
        transitionTimer = new ElapsedTime();
    }

    @Override
    public void onStateLoop() {

    }

    @Override
    public void onStateEnd() {
    }
}
