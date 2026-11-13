package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drivercontrol.DriverControl;
import org.firstinspires.ftc.teamcode.drivercontrol.TuneControl;
import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;

@TeleOp
public class tuneTeleOp extends OpMode {
    /** The hardware the TeleOp uses */
    LM0Board board;
    /** The driver control logic fed the programming board */
    DriverControl<LM0Board> driverControl;
    /** The time that has elapsed during the match */
    public ElapsedTime matchTimer;
    @Override
    public void init() {
        board = new LM0Board();
        board.init(hardwareMap);
        //this is just bare-bones driver control, so no states needed.
        matchTimer = new ElapsedTime();
        driverControl = new TuneControl();
        driverControl.init(telemetry, board, gamepad1, gamepad2);
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
        Pose pedroPose = board.getCurrentPedroPose();
        telemetry.addData("X", pedroPose.x());
        telemetry.addData("Y", pedroPose.y());
    }

}
