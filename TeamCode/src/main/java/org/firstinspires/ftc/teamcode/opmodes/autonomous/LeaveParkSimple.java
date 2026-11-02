package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;
import org.firstinspires.ftc.teamcode.opmodes.teleop.LM0TeleOp;

@Autonomous
public class LeaveParkSimple extends OpMode {
    private LM0Board board;
    private ElapsedTime matchTimer;
    private double timeStarted;

    @Override
    public void init() {
        board = new LM0Board();
        board.init(hardwareMap);
        matchTimer = new ElapsedTime();
    }

    @Override
    public void start() {
        timeStarted = matchTimer.time();
    }

    @Override public void loop() {
        telemetry.addData("Value: ", matchTimer.time() - timeStarted);
        if (matchTimer.time() - timeStarted < 0.5) {
            telemetry.addData("Should be moving: ", "yes");
            board.cacheFlMotorPower(0.25);
            board.cacheFrMotorPower(0.25);
            board.cacheBlMotorPower(0.25);
            board.cacheBrMotorPower(0.25);
        } else {
            board.cacheFlMotorPower(0.0);
            board.cacheFrMotorPower(0.0);
            board.cacheBlMotorPower(0.0);
            board.cacheBrMotorPower(0.0);
        }
        board.writeLoop();
    }
}
