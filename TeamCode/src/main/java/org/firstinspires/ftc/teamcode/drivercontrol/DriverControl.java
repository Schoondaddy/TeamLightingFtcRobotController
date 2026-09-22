package org.firstinspires.ftc.teamcode.drivercontrol;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard;


/**
 * Abstract class for  classes that handle driver input logic
 * @param <B> The implementation of interface {@link ProgrammingBoard} which is used by subclasses of DriverControl
 */
public abstract class DriverControl<B extends ProgrammingBoard> {
    /** FTC telemetry interface used to send data to the Driver Station */
    protected Telemetry telemetry;
    /** Robot hardware used by a DriverControl subclass */
    protected B board;

    /** First driver gamepad */
    protected Gamepad gamepad1;
    /** Second driver gamepad */
    protected Gamepad gamepad2;



    /** Initializes driver control, including telemetry, robot hardware, and gamepads
     *
     * <p>This method should be called from the OpMode's {@link com.qualcomm.robotcore.eventloop.opmode.OpMode#init() init() method}</p>
     *
     * @param telemetry FTC telemetry interface used to send data to the Driver Station
     * @param board robot hardware used by a DriverControl subclass
     * @param gamepad1 first driver gamepad
     * @param gamepad2 second driver gamepad
     */
    public abstract void init(Telemetry telemetry, B board, Gamepad gamepad1, Gamepad gamepad2);

    /**Executes an iteration of the DriverControl loop.
     * <p>This method should be called from the OpMode's {@link com.qualcomm.robotcore.eventloop.opmode.OpMode#loop() loop() method}</p>
     */
    public abstract void loop();
}