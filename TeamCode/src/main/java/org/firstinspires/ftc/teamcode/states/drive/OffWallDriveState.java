package org.firstinspires.ftc.teamcode.states.drive;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.mechanisms.TestBoard;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.ExampleAutonomous;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.ShootLeaveAuto;
import org.firstinspires.ftc.teamcode.states.RobotState;
import org.firstinspires.ftc.teamcode.states.StateCategory;

public class OffWallDriveState extends RobotState<LM0Board, ShootLeaveAuto> {
    public OffWallDriveState() { super(StateCategory.NONE); }
    public ElapsedTime stateTimer = new ElapsedTime();
    @Override
    public void onStateStart() {
        stateTimer.reset();
    }

    @Override
    public void onStateLoop() {
        if (stateTimer.seconds() < 5.3 && stateTimer.seconds() > 5) {
            board.cacheFlMotorPower(0.25);
            board.cacheFrMotorPower(0.25);
            board.cacheBlMotorPower(0.25);
            board.cacheBrMotorPower(0.25);

        } else {
            board.cacheFlMotorPower(0);
            board.cacheFrMotorPower(0);
            board.cacheBlMotorPower(0);
            board.cacheBrMotorPower(0);
        }
    }

    @Override
    public void onStateEnd() {
        System.out.println("This state was transitioned out of!");
    }
}
