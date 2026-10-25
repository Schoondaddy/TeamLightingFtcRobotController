package org.firstinspires.ftc.teamcode.drivercontrol;

import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.applyDeadzone;
import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.multiAbsMax;
import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.quadraticScale;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;

import java.util.ArrayList;

public class TuneControl extends DriverControl<LM0Board>{
    ArrayList<Double> xList = new ArrayList<>();
    ArrayList<Double> yList = new ArrayList<>();
    private boolean driveStop;
    private boolean turretStop;
    private boolean rotateStop;

    public void init(Telemetry telemetry, LM0Board board, Gamepad gamepad1, Gamepad gamepad2) {
        this.board = board;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.telemetry = telemetry;
    }
    @Override
    public void loop() {
        if (!driveStop) {handleDrivetrain();}

        if (!turretStop) {handleTurret();}
        if (gamepad1.startWasPressed()) {
            board.flipKillSwitch();
            gamepad1.rumbleBlips(3);
        }
    }
    private void handleDrivetrain() {
        double axial = -0.5 * quadraticScale(applyDeadzone(gamepad1.left_stick_y, 0.05));
        double lateral = 0.5 *quadraticScale(applyDeadzone(gamepad1.left_stick_x, 0.05));
        double yaw = 0.5 * quadraticScale(applyDeadzone(gamepad1.right_stick_x, 0.05));

        double flPower = axial + lateral + yaw;
        double frPower = axial - lateral - yaw;
        double blPower = axial - lateral + yaw;
        double brPower = axial + lateral - yaw;


        double max = multiAbsMax(flPower, frPower, blPower, brPower);

        if (max  > 1.0) {
            flPower /= max;
            frPower /= max;
            blPower /= max;
            brPower /= max;
        }


        board.cacheFlMotorPower(flPower);
        board.cacheFrMotorPower(frPower);
        board.cacheBlMotorPower(blPower);
        board.cacheBrMotorPower(brPower);



        telemetry.addData("Front left/Right", "%4.2f, %4.2f", flPower, frPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", blPower, brPower);
        telemetry.addData("Voltage", board.getCachedVoltage());
    }

    public static void handleTurret() {



    }
}
