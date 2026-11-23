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
    private MediaPlayer music;
    private boolean musicStarted = false;
    /** The hardware the TeleOp uses */
    LM2Board board;
    /** The driver control logic fed the programming board */
    DriverControl<LM2Board, LM0TeleOp> dc;
    /** The time that has elapsed during the match */
    public ElapsedTime matchTimer;
    public Follower follower;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        board = new LM2Board();
        board.init(hardwareMap);
        matchTimer = new ElapsedTime();
        dc = new LM2Control();
        dc.init(telemetry, board, this, gamepad1, gamepad2);


    }

    @Override
    public void start() {
        dc.start();
    }

    @Override
    public void loop() {
        /*
        if (music != null && !musicStarted) {
            music.start();
            musicStarted = true;
        }
        */
        board.readLoop();
        dc.loop();
        board.writeLoop();
        Pose pedroPose = board.getCurrentPedroPose();
        telemetry.addData("X", pedroPose.x());
        telemetry.addData("Y", pedroPose.y());
    }
    @Override
    public void stop() {
        /*
        if (music != null) {
            music.stop();
            music.release();
            music = null;
       } */
    }

}
