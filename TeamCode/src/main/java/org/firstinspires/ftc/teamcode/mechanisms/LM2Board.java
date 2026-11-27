package org.firstinspires.ftc.teamcode.mechanisms;


import com.pedropathing.math.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class LM2Board implements ProgrammingBoard {
    private static class MotorWrapper {
        private boolean killSwitch = false;
        private boolean pastKillSwitch = false;
        private final DcMotor motor;
        private double cachedPower;
        private double prevCachedPower;

        MotorWrapper(DcMotor motor) {
            this.motor = motor;
        }

        private void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
            motor.setZeroPowerBehavior(behavior);
        }

        private void setDirection(DcMotorSimple.Direction direction) {
            motor.setDirection(direction);
        }
        private void cachePower(double power) {
            prevCachedPower = cachedPower;
            cachedPower = power;
        }
        private double getCachedPower() {
            return cachedPower;
        }
        private void write() {
            if (killSwitch){
                if (!pastKillSwitch) {
                    motor.setPower(0.0);
                    pastKillSwitch = true;
                }
            }
            else if (cachedPower != prevCachedPower) {
                motor.setPower(cachedPower);
            }
        }
    }
    private ElapsedTime voltageTimer;
    private VoltageSensor voltageSensor;
    private double cachedVoltage = 12;
    private Servo aimServo;
    private Servo depollinatorServo;
    private Servo blockerServo;
    private MotorWrapper intakeMotor;
    private MotorWrapper passthroughMotor;
    private MotorWrapper flMotor;
    private MotorWrapper frMotor;
    private
    MotorWrapper blMotor;
    private MotorWrapper brMotor;
    private DcMotorEx flywheelMotor1;
    private DcMotorEx flywheelMotor2;
    private MotorWrapper[] motors;
    private MotorWrapper[] driveMotors;
    private Pose2D currentPose;
    private GoBildaPinpointDriver odoPod;

    @Override public void init(HardwareMap hwMap) {
        voltageSensor = hwMap.get(VoltageSensor.class, "Control Hub");
        voltageTimer = new ElapsedTime();

        aimServo = hwMap.get(Servo.class, "aim_servo");
        aimServo.scaleRange(0.0,0.4);
        depollinatorServo = hwMap.get(Servo.class, "depollinator_servo");
        depollinatorServo.scaleRange(0,0.69);
        blockerServo = hwMap.get(Servo.class, "blocker_servo");
        blockerServo.scaleRange(0.55,0.8);
        intakeMotor = new MotorWrapper(hwMap.get(DcMotor.class, "intake_motor"));
        passthroughMotor = new MotorWrapper(hwMap.get(DcMotor.class, "passthrough_motor"));

        flMotor = new MotorWrapper(hwMap.get(DcMotor.class, "fl_motor"));
        frMotor = new MotorWrapper(hwMap.get(DcMotor.class, "fr_motor"));
        blMotor = new MotorWrapper(hwMap.get(DcMotor.class, "bl_motor"));
        brMotor = new MotorWrapper(hwMap.get(DcMotor.class, "br_motor"));

        motors = new MotorWrapper[] {intakeMotor, passthroughMotor, flMotor, frMotor, blMotor, brMotor};
        driveMotors = new MotorWrapper[] {flMotor, frMotor, blMotor, brMotor};
        passthroughMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        flMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        blMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        brMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        for (MotorWrapper motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        flywheelMotor1 = hwMap.get(DcMotorEx.class, "flywheel_motor_1");
        flywheelMotor2 = hwMap.get(DcMotorEx.class, "flywheel_motor_2");
        flywheelMotor1.setDirection(DcMotorSimple.Direction.FORWARD);
        flywheelMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheelMotor1.setVelocityPIDFCoefficients(8.0, 0.01, 0.5, 13.0);
        flywheelMotor2.setVelocityPIDFCoefficients(8.0, 0.01, 0.5, 13.0);

        odoPod = hwMap.get(GoBildaPinpointDriver.class, "odo_pod");
        odoPod.resetPosAndIMU();
    }

    @Override
    public void readLoop() {
        if (voltageTimer.seconds() > 0.05) {
            cachedVoltage = voltageSensor.getVoltage();
        }
        currentPose = odoPod.getPosition();
    }
    @Override
    public void writeLoop() {
        for (MotorWrapper motor : motors) {
            motor.write();
        }
    }
    public double getCachedVoltage() {
        return cachedVoltage;
    }

    public void cacheFlMotorPower(double power) {
        flMotor.cachePower(power);
    }
    public void cacheFrMotorPower(double power) {
        frMotor.cachePower(power);
    }
    public void cacheBlMotorPower(double power) {
        blMotor.cachePower(power);
    }
    public void cacheBrMotorPower(double power) {
        brMotor.cachePower(power);
    }
    public void cacheIntakeMotorPower(double power) {
        intakeMotor.cachePower(power);
    }
    public void cachePassthroughMotorPower(double power) {
        passthroughMotor.cachePower(power);
    }
    public double getIntakeMotorPower() {
        return intakeMotor.getCachedPower();
    }
    public double getPassthroughMotorPower() {
        return passthroughMotor.getCachedPower();
    }
    public void setFlywheelMotor1Vel(double velocity) {
        flywheelMotor1.setVelocity(velocity);
    }
    public void setFlywheelMotor2Vel(double velocity) {
        flywheelMotor2.setVelocity(velocity);
    }
    public double getFlywheelMotor1Vel() {
        return flywheelMotor1.getVelocity();
    }
    public double getFlywheelMotor2Vel() {
        return flywheelMotor2.getVelocity();
    }
    public void setAimServoPosition(double pos) {
        aimServo.setPosition(pos);
    }
    public double getAimServoPosition() {
        return aimServo.getPosition();
    }
    public void deployDepollinator() {
        depollinatorServo.setPosition(1.0);
    }
    public void retractDepollinator() {
        depollinatorServo.setPosition(0.0);
    }
    public void deployBlocker() {
        blockerServo.setPosition(1.0);
    }
    public void retractBlocker() {
        blockerServo.setPosition(0.0);
    }
    public Pose2D getCurrentFTCPose() {return currentPose; }
    public Pose getCurrentPedroPose() {
        double x = currentPose.getX(DistanceUnit.INCH);
        double y = currentPose.getY(DistanceUnit.INCH);
        double heading = currentPose.getHeading(AngleUnit.RADIANS);

        Pose convertedPose = new Pose(
                y + 72,
                72 - x,
                heading - Math.PI / 2
        );
        return convertedPose;
    }
    public void flipKillSwitch() {
        for (MotorWrapper motor : motors) {
            motor.killSwitch = !motor.killSwitch;
        }
    }
    public void flipDriveKillSwitch() {
        for (MotorWrapper motor : driveMotors) {
            motor.killSwitch = !motor.killSwitch;
        }
    }
    public void setDriveKillSwitch(boolean killSwitch) {
        for (MotorWrapper motor : driveMotors) {
            motor.killSwitch = killSwitch;
        }
    }
}
