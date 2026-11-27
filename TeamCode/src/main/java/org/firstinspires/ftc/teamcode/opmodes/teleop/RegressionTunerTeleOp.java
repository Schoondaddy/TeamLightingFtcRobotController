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

@TeleOp
public class RegressionTunerTeleOp extends OpMode {
    /**
     * The hardware the TeleOp uses
     */
    LM2Board board;
    /**
     * The driver control logic fed the programming board
     */
    DriverControl<LM2Board, OpMode> driverControl;
    public Follower follower;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        board = new LM2Board();
        board.init(hardwareMap);
        //this is just bare-bones driver control, so no states needed.
        driverControl = new RegressionTuneControl();
        driverControl.init(telemetry, board, this, gamepad1, gamepad2);
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
        Pose pedroPose = follower.pose();
        telemetry.addData("X", pedroPose.x());
        telemetry.addData("Y", pedroPose.y());
    }

}