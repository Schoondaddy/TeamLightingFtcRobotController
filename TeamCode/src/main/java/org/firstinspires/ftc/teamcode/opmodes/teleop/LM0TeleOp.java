package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drivercontrol.DriverControl;
import org.firstinspires.ftc.teamcode.drivercontrol.LM0Control;
import org.firstinspires.ftc.teamcode.drivercontrol.LM1Control;
import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;

@TeleOp
public class LM0TeleOp extends OpMode {
    /** The hardware the TeleOp uses */
    LM0Board board;
    /** The driver control logic fed the programming board */
    DriverControl<LM0Board, LM0TeleOp> dc;
    /** The time that has elapsed during the match */
    public ElapsedTime matchTimer;
    @Override
    public void init() {
        board = new LM0Board();
        board.init(hardwareMap);
        //this is just bare-bones driver control, so no states needed.
        matchTimer = new ElapsedTime();
        dc = new LM1Control();
        dc.init(telemetry, board, this, gamepad1, gamepad2);
    }

    @Override
    public void start() {
        dc.start();
    }

    @Override
    public void loop() {
        board.readLoop();
        dc.loop();
        board.writeLoop();
    }

}
