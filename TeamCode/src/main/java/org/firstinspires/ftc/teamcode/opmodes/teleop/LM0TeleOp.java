package org.firstinspires.ftc.teamcode.opmodes.teleop;

import android.media.MediaPlayer;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drivercontrol.DriverControl;
import org.firstinspires.ftc.teamcode.drivercontrol.LM2Control;
import org.firstinspires.ftc.teamcode.mechanisms.LM2Board;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class LM0TeleOp extends OpMode {
    /** The hardware the TeleOp uses */
    LM2Board board;
    /** The driver control logic fed the programming board */
    DriverControl<LM2Board, LM0TeleOp> dc;
    /** The time that has elapsed during the match */
    public ElapsedTime matchTimer;
    public Follower follower;
    public double deadLine = 1.5;
    private Pose startPose = new Pose(0, 0, 90);
    public Pose blueHivePose = new Pose(0, 10);
    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        board = new LM2Board();
        board.init(hardwareMap);
        matchTimer = new ElapsedTime();
        dc = new LM2Control();
        dc.init(telemetry, board, this, gamepad1, gamepad2);
        follower.setPose(startPose);

    }

    @Override
    public void start() {
        dc.start();
    }

    @Override
    public void loop() {

        if (matchTimer.seconds() > deadLine) {
            deadLine = (int) (Math.random() * 3) + 0.5;
            int soundID = hardwareMap.appContext.getResources().getIdentifier("slimebeck", "raw", hardwareMap.appContext.getPackageName());
            if (soundID != 0) {
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, soundID);
            }
        }
        board.readLoop();
        dc.loop();
        board.writeLoop();
        Pose pedroPose = follower.pose();
        telemetry.addData("X", pedroPose.x());
        telemetry.addData("Y", pedroPose.y());
    }

}
