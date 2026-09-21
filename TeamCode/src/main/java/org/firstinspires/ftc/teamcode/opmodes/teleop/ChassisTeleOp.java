package org.firstinspires.ftc.teamcode.opmodes.teleop;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drivercontrol.ChassisControl;
import org.firstinspires.ftc.teamcode.drivercontrol.DriverControl;
import org.firstinspires.ftc.teamcode.mechanisms.ChassisBoard;

@TeleOp
public class ChassisTeleOp extends OpMode {
    ChassisBoard board;
    DriverControl<ChassisBoard> dc;
    public ElapsedTime matchTimer;
    @Override
    public void init() {
        board = new ChassisBoard();
        board.init(hardwareMap);
        //this is just bare-bones driver control, so no states needed.
        matchTimer = new ElapsedTime();
        dc = new ChassisControl();
        dc.init(telemetry, board, gamepad1, gamepad2);
    }

    @Override
    public void loop() {
        board.loop();
        dc.loop();
    }
}
