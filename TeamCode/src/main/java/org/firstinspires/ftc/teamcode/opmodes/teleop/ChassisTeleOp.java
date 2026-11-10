package org.firstinspires.ftc.teamcode.opmodes.teleop;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drivercontrol.ChassisControl;
import org.firstinspires.ftc.teamcode.drivercontrol.DriverControl;
import org.firstinspires.ftc.teamcode.mechanisms.ChassisBoard;

@Disabled
@TeleOp
public class ChassisTeleOp extends OpMode {
    /** The hardware the TeleOp uses */
    ChassisBoard board;
    /** The driver control logic fed the programming board */
    DriverControl<ChassisBoard> driverControl;
    /** The time that has elapsed during the match */
    public ElapsedTime matchTimer;
    @Override
    public void init() {
        board = new ChassisBoard();
        board.init(hardwareMap);
        //this is just bare-bones driver control, so no states needed.
        matchTimer = new ElapsedTime();
        driverControl = new ChassisControl();
        driverControl.init(telemetry, board, gamepad1, gamepad2);
    }

    @Override
    public void loop() {
        board.readLoop();
        driverControl.loop();
        board.writeLoop();
    }
}
