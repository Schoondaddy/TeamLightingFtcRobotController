package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drivercontrol.DriverControl;
import org.firstinspires.ftc.teamcode.mechanisms.ChassisBoard;


public class TrigTuneTeleOp extends OpMode{
    /** The hardware the TeleOp uses */
    ChassisBoard board;
    /** The driver control logic fed the programming board */
    DriverControl<ChassisBoard> dc;
    /** The time that has elapsed during the match */
    public ElapsedTime matchTimer;
}
