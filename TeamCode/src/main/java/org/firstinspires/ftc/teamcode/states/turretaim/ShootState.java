package org.firstinspires.ftc.teamcode.states.turretaim;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.mechanisms.TestBoard;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.ExampleAutonomous;
import org.firstinspires.ftc.teamcode.opmodes.autonomous.ShootLeaveAuto;
import org.firstinspires.ftc.teamcode.states.RobotState;
import org.firstinspires.ftc.teamcode.states.StateCategory;

public class ShootState extends RobotState<LM0Board, ShootLeaveAuto> {
    public ShootState() { super(StateCategory.TURRET_AIM); }
    public ElapsedTime stateTimer;
    @Override
    public void onStateStart() {
        stateTimer = new ElapsedTime();
        stateTimer.reset();
        board.setFlywheelMotor2Vel(992);
        board.setFlywheelMotor1Vel(992);
        board.setAimServoPosition(0.61);
    }

    @Override
    public void onStateLoop() {
        if (stateTimer.seconds() > 0.5) {
            board.setBlockerServoPower(1.0);
            board.cachePassthroughMotorPower(1);
            board.cacheIntakeMotorPower(0.75);
        }
    }

    @Override
    public void onStateEnd() {
    }
}
