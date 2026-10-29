package org.firstinspires.ftc.teamcode.drivercontrol;

import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.applyDeadzone;
import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.multiAbsMax;
import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.quadraticScale;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.ChassisBoard;
import org.firstinspires.ftc.teamcode.mechanisms.LM0Board;

public class LM0Control extends DriverControl<LM0Board> {
    /** Toggles Slow Mode */
    private boolean slowMode;
    /**Toggles the second driver's override fast mode */
    private boolean fastMode;
    /** The mult applied to motor power when Slow Mode is toggled */
    private double slowModeMult;
    /** The amount of time (nanoseconds) the touchpad needs to be held in order to flip the kill switch*/
    private double killSwitchHoldTime;
    /** The time stamp (nanoseconds) of when the touchpad began being pressed. -1.0 if touchpad is not currently being pressed*/
    private double killSwitchPressedTime;
    /** The theoretical voltage the robot should have */
    private double nominalVoltage;
    /** The actual voltage the robot has */
    private double currentVoltage;

    public ElapsedTime matchTimer;
    @Override
    public void init(Telemetry telemetry, LM0Board board, Gamepad gamepad1, Gamepad gamepad2) {
        this.board = board;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.telemetry = telemetry;
        slowMode = false;
        slowModeMult = 0.33;
        killSwitchHoldTime = 1000000.0;
        killSwitchPressedTime = -1.0;
        nominalVoltage = 12.0;
        currentVoltage = nominalVoltage;
        matchTimer = new ElapsedTime();
    }
    @Override
    public void loop() {
        currentVoltage = board.getCachedVoltage();

        if (gamepad1.yWasPressed()) { //slow mode
            slowMode = !slowMode;
        }
        if (gamepad1.leftTriggerWasPressed() || gamepad1.leftTriggerWasReleased()) { //Hold mode switch
            slowMode = !slowMode;
        }
        handleDrivetrain();

        if (gamepad2.touchpad) { //Kill switch
            telemetry.addData("Pressed time", matchTimer.time() - killSwitchPressedTime);
            if (killSwitchPressedTime == -1.0) {
                killSwitchPressedTime = matchTimer.time();
            } else if (matchTimer.time() - killSwitchPressedTime >= killSwitchHoldTime) {
                board.flipKillSwitch();
                gamepad1.rumbleBlips(3);
                killSwitchPressedTime = -1.0;
            }
        } else if (killSwitchPressedTime != -1.0){
            killSwitchPressedTime = -1.0;
        }

        // Will make states for these
        double intakePower = board.getIntakeMotorPower();
        double passthroughPower = board.getPassthroughMotorPower();
        if (gamepad2.x){ //eject
            intakePower -= 0.05;
            passthroughPower -= 0.05;
        } else if (gamepad1.right_trigger > 0.1) { //Intake
            intakePower += 0.05;
            passthroughPower += 0.05;
        } else {
            if (intakePower != 0.0) {
                intakePower -= 0.10 * Math.signum(intakePower);
                if (Math.abs(intakePower) < 0.10) {
                    intakePower = 0.0;
                }
            }
            if (passthroughPower != 0.0) {
                passthroughPower -= 0.10 * Math.signum(passthroughPower);
                if (Math.abs(passthroughPower) < 0.10) {
                    passthroughPower = 0.0;
                }
            }
        }
        board.cacheIntakeMotorPower(Math.max(-0.75, Math.min(intakePower, 0.75)));
        board.cachePassthroughMotorPower(Math.max(0.0, Math.min(passthroughPower, 0.75)));
        board.setFlywheelMotor1Vel(1.0);
        board.setFlywheelMotor2Vel(1.0);
        /*
        if (gamepad2.right_trigger_pressed){ //Launch
            board.setFlywheelMotor1Vel(1.0);
            board.setFlywheelMotor2Vel(1.0);
        }
        if (gamepad2.right_trigger < 0.1) {
            board.setFlywheelMotor2Vel(0.0);
            board.setFlywheelMotor1Vel(0.0); 
            board.setBlockerRotation(false);
        } else {
            if (board.getFlywheelMotor1Velocity() > 0.9 && board.getFlywheelMotor2Velocity() > 0.9) {
                board.setBlockerRotation(true);
                board.cachePassthroughMotorPower(0.5);
            }
        }
        */

    }

    /**
     * Helper method used to handle the drive train and its logic
     * <p>Applies a deadzone to input, quadratically scales input, compensates for voltage,
     * normalizes motors, and applies Slow Mode </p>
     */
    private void handleDrivetrain() {
        double axial;
        double lateral;
        double yaw;
        double overrideDeadzone = 0.1;
        if ((gamepad2.left_stick_x > overrideDeadzone) || (gamepad2.left_stick_y > overrideDeadzone) || (gamepad2.right_stick_x > overrideDeadzone)) {
            telemetry.addData("Gamepad 2 ", "is controlling yay!");
            telemetry.addData("Gamepad 2 left stick x, y", "%4.2f, %4.2f", gamepad2.left_stick_y, gamepad2.left_stick_y);
            axial = -0.5 * quadraticScale(applyDeadzone(gamepad2.left_stick_y, 0.05));
            lateral = 0.5 * quadraticScale(applyDeadzone(gamepad2.left_stick_x, 0.05));
            yaw = 0.5 * quadraticScale(applyDeadzone(gamepad2.right_stick_x, 0.05));
        } else {
            axial = -quadraticScale(applyDeadzone(gamepad1.left_stick_y, 0.05));
            lateral = quadraticScale(applyDeadzone(gamepad1.left_stick_x, 0.05));
            yaw = quadraticScale(applyDeadzone(gamepad1.right_stick_x, 0.05));
        }
        currentVoltage = board.getCachedVoltage();


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

        flPower = voltageClamp(flPower);
        frPower = voltageClamp(frPower);
        blPower = voltageClamp(blPower);
        brPower = voltageClamp(brPower);



        if (slowMode) {
            flPower *= slowModeMult;
            frPower *= slowModeMult;
            blPower *= slowModeMult;
            brPower *= slowModeMult;
        }


        board.cacheFlMotorPower(flPower);
        board.cacheFrMotorPower(frPower);
        board.cacheBlMotorPower(blPower);
        board.cacheBrMotorPower(brPower);



        telemetry.addData("Front left/Right", "%4.2f, %4.2f", flPower, frPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", blPower, brPower);
        telemetry.addData("Voltage", board.getCachedVoltage());
    }


    /** Adjusts for voltage, scaling up power if voltage is below the norm, scaling down if above
     *
     * @param value an input, typically within {@code [-1, -1]}
     * @return returns the scaled and clamped value
     */
    private double voltageClamp(double value) {
        double compFactor = nominalVoltage / currentVoltage;
        return Math.min(1.0, Math.max(-1.0, compFactor * value));
    }
}


