package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drivercontrol.DriverControl;
import org.firstinspires.ftc.teamcode.drivercontrol.TuneControl;
import org.firstinspires.ftc.teamcode.mechanisms.LM2Board;

@TeleOp
public class TuneTeleOp extends OpMode {
    /** The hardware the TeleOp uses */
    LM2Board board;
    /** The driver control logic fed the programming board */
    DriverControl<LM2Board, TuneTeleOp> driverControl;
    /** The time that has elapsed during the match */
    public ElapsedTime matchTimer;
    public double deadLine;
    public ElapsedTime timer;
    public Follower follower;
    @Override
    public void init() {
        board = new LM2Board();
        board.init(hardwareMap);
        //this is just bare-bones driver control, so no states needed.
        matchTimer = new ElapsedTime();
        driverControl = new TuneControl();
        driverControl.init(telemetry, board, this ,gamepad1, gamepad2);
    }

    @Override
    public void start() {
        driverControl.start();
        timer = new ElapsedTime();
    }

    @Override
    public void loop() {
        if (timer.seconds() > deadLine) {
            timer.reset();
            deadLine = (int) (Math.random() * 3) + 0.5;
            int soundID = hardwareMap.appContext.getResources().getIdentifier("slimebeck", "raw", hardwareMap.appContext.getPackageName());
            if (soundID != 0) {
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, soundID);
            }
        }
        board.readLoop();
        driverControl.loop();

        board.writeLoop();
        Pose pedroPose = follower.pose();
        telemetry.addData("X", pedroPose.x());
        telemetry.addData("Y", pedroPose.y());
    }

}
