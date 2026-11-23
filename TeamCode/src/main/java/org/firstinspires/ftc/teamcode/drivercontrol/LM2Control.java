package org.firstinspires.ftc.teamcode.drivercontrol;

import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.applyDeadzone;
import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.multiAbsMax;
import static org.firstinspires.ftc.teamcode.drivercontrol.ControlUtils.quadraticScale;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.mechanisms.LM2Board;
import org.firstinspires.ftc.teamcode.opmodes.teleop.LM0TeleOp;
import org.firstinspires.ftc.teamcode.states.RobotState;
import org.firstinspires.ftc.teamcode.states.StateCategory;
import org.firstinspires.ftc.teamcode.states.StateMachine;
import org.firstinspires.ftc.teamcode.states.intakepassthrough.IntakeForward;
import org.firstinspires.ftc.teamcode.states.intakepassthrough.IntakePassthrough;
import org.firstinspires.ftc.teamcode.states.intakepassthrough.IntakePassthroughForward;
import org.firstinspires.ftc.teamcode.states.intakepassthrough.IntakePassthroughIdle;
import org.firstinspires.ftc.teamcode.states.intakepassthrough.IntakePassthroughReverse;
import org.firstinspires.ftc.teamcode.states.intakepassthrough.IntakePassthroughState;
import org.firstinspires.ftc.teamcode.states.intakepassthrough.IntakeReverse;

import java.util.HashMap;


public class LM2Control extends DriverControl<LM2Board, LM0TeleOp> {
    /** Toggles Slow Mode */
    private StateMachine<LM2Board, LM0TeleOp> intakePassthroughStateMachine;
    public HashMap<IntakePassthrough, Class<? extends IntakePassthroughState>> IPStatesMap;
    private boolean killSwitch;
    /** The amount of time (nanoseconds) the touchpad needs to be held in order to flip the kill switch*/
    private double killSwitchHoldTime;
    /** The time stamp (nanoseconds) of when the touchpad began being pressed. -1.0 if touchpad is not currently being pressed*/
    private double killSwitchPressedTime;
    /** The theoretical voltage the robot should have */
    private double nominalVoltage;
    /** The actual voltage the robot has */
    private double currentVoltage;
    private double flywheelVelocity;
    /** Toggles field centric drive */
    private boolean fieldCentric;
    private boolean pedroBreak;
    private boolean wasHolding = false;
    private Pose lockedPose;
    public ElapsedTime matchTimer;
    @Override
    public void init(Telemetry telemetry, LM2Board board, LM0TeleOp opMode, Gamepad gamepad1, Gamepad gamepad2) {
        this.board = board;
        this.opMode = opMode;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.telemetry = telemetry;

        IPStatesMap = new HashMap<>();
        IPStatesMap.put(IntakePassthrough.INTAKE_PASSTHROUGH_IDLE, IntakePassthroughIdle.class);
        IPStatesMap.put(IntakePassthrough.INTAKE_PASSTHROUGH_FORWARD, IntakePassthroughForward.class);
        IPStatesMap.put(IntakePassthrough.INTAKE_PASSTHROUGH_REVERSE, IntakePassthroughReverse.class);
        IPStatesMap.put(IntakePassthrough.INTAKE_FORWARD,  IntakeForward.class);
        IPStatesMap.put(IntakePassthrough.INTAKE_REVERSE,  IntakeReverse.class);

        intakePassthroughStateMachine = new StateMachine<>(opMode, telemetry, board, gamepad1, gamepad2, StateCategory.INTAKE_PASSTHROUGH);
        intakePassthroughStateMachine.init(new IntakePassthroughIdle());

        killSwitch = false;
        killSwitchHoldTime = 3.0;
        killSwitchPressedTime = -1.0;
        nominalVoltage = 12.0;
        currentVoltage = nominalVoltage;
        matchTimer = new ElapsedTime();
        flywheelVelocity = 980.0;
        fieldCentric = true;
    }

    @Override
    public void start() {
        board.setAimServoPosition(0.66);
        board.setFlywheelMotor1Vel(flywheelVelocity);
        board.setFlywheelMotor2Vel(flywheelVelocity);
    }

    @Override
    public void loop() {
        currentVoltage = board.getCachedVoltage();

        handleDrivetrain();

        if (gamepad2.touchpad) { //Kill switch
            gamepad2.rumbleBlips(3);
            telemetry.addData("Pressed time", matchTimer.time() - killSwitchPressedTime);
            if (killSwitchPressedTime == -1.0) {
                killSwitchPressedTime = matchTimer.time();
            } else if (matchTimer.time() - killSwitchPressedTime >= killSwitchHoldTime) {
                board.flipKillSwitch();
                killSwitch = !killSwitch;
                if (killSwitch) {
                    board.setFlywheelMotor1Vel(0.0);
                    board.setFlywheelMotor2Vel(0.0);
                } else {
                    board.setFlywheelMotor1Vel(flywheelVelocity);
                    board.setFlywheelMotor2Vel(flywheelVelocity);
                }

                gamepad1.rumbleBlips(3);
                gamepad2.rumbleBlips(3);
                killSwitchPressedTime = -1.0;
            }

        } else if (killSwitchPressedTime != -1.0){
            killSwitchPressedTime = -1.0;
        }
        RobotState<LM2Board, LM0TeleOp> currentState = intakePassthroughStateMachine.getCurrentState();
        IntakePassthrough intendedState;
        if (gamepad1.x || gamepad1.right_trigger > 0.1) {
            intendedState = IntakePassthrough.INTAKE_PASSTHROUGH_FORWARD;
            currentState.transitionState(IntakePassthroughForward.class);
        } else if (gamepad1.left_trigger > 0.1) {
            intendedState = IntakePassthrough.INTAKE_FORWARD;
            currentState.transitionState(IntakeForward.class);
        } else if (gamepad1.right_bumper) {
            intendedState = IntakePassthrough.INTAKE_PASSTHROUGH_REVERSE;
            currentState.transitionState(IntakePassthroughReverse.class);
        } else if (gamepad1.left_bumper) {
            intendedState = IntakePassthrough.INTAKE_REVERSE;
            currentState.transitionState(IntakeReverse.class);
        } else {
            intendedState = IntakePassthrough.INTAKE_PASSTHROUGH_IDLE;
            currentState.transitionState(IntakePassthroughIdle.class);
        }


        telemetry.addData("Intended state: ", intendedState.toString());
        telemetry.addData("Actual State: ", currentState);
        intakePassthroughStateMachine.loop();

        telemetry.addData("TPS: ", board.getFlywheelMotor1Vel());


        if (killSwitch) {
            board.setBlockerServoPower(0.0);
            pedroBreak = false;
        } else if (gamepad1.x ) { //Launch
            board.setBlockerServoPower(1.0);
            pedroBreak = true;
        } else {
            board.setBlockerServoPower(-1.0);
            pedroBreak = false;
        }
    }

    /**
     * Helper method used to handle the drive train and its logic
     * <p>Applies a deadzone to input, quadratically scales input, compensates for voltage,
     * normalizes motors, and applies Slow Mode </p>
     */
    private void handleDrivetrain() {
        opMode.follower.update();
        if (pedroBreak) {
            if (!wasHolding) {
                lockedPose = opMode.follower.pose();
            }
            wasHolding = true;
            opMode.follower.hold(lockedPose);
            return;
        } else {
            if (wasHolding) {
                opMode.follower.idle();
            }
            wasHolding = false;

        }
        double axial;
        double lateral;
        double yaw;
        double overrideDeadzone = 0.1;
        axial = -quadraticScale(applyDeadzone(gamepad1.left_stick_y, 0.05));
        lateral = quadraticScale(applyDeadzone(gamepad1.left_stick_x, 0.05));
        yaw = quadraticScale(applyDeadzone(gamepad1.right_stick_x, 0.05));

        if (fieldCentric) {
            double theta = -board.getCurrentPedroPose().heading() - Math.PI/2;
            double tempLateral = lateral * Math.cos(theta) - axial * Math.sin(theta);
            double tempAxial   = lateral * Math.sin(theta) + axial * Math.cos(theta);

            lateral = tempLateral;
            axial = tempAxial;

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


