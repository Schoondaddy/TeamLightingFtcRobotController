package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drivercontrol.DriverControl;
import org.firstinspires.ftc.teamcode.drivercontrol.RegressionTuneControl;
import org.firstinspires.ftc.teamcode.drivercontrol.TuneControl;
import org.firstinspires.ftc.teamcode.mechanisms.LM2Board;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.utils.CSVWebServer;

import java.io.File;
import java.io.IOException;

@TeleOp
public class RegressionTunerTeleOp extends OpMode {
    /**
     * The hardware the TeleOp uses
     */
    LM2Board board;
    /**
     * The driver control logic fed the programming board
     */
    RegressionTuneControl driverControl;
    public Follower follower;
    public Pose targetPose = new Pose(74, 74);

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        follower.setPose(new Pose(10,10,Math.PI));
        board = new LM2Board();
        board.init(hardwareMap);
        //this is just bare-bones driver control, so no states needed.
        driverControl = new RegressionTuneControl();
        driverControl.init(telemetry, board, this, gamepad1, gamepad2);
        driverControl.start();
    }
    @Override
    public void start() {
        driverControl.start();
    }
    @Override
    public void loop() {
        board.readLoop();
        driverControl.loop();

        board.writeLoop();
        follower.update();

        Pose pedroPose = follower.pose();
        telemetry.addData("X", pedroPose.x());
        telemetry.addData("Y", pedroPose.y());
    }
    @Override
    public void stop() {
        driverControl.stop();
    }
}