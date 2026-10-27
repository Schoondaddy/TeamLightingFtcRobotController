package org.firstinspires.ftc.teamcode.mechanisms;


import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class LM0Board implements ProgrammingBoard {
    private static class MotorWrapper {
        private boolean killSwitch = false;
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
            if (cachedPower != prevCachedPower) {
                motor.setPower(cachedPower);
            }
        }
    }
    private ElapsedTime voltageTimer;
    private VoltageSensor voltageSensor;
    private double cachedVoltage = 12;
    private Servo rotationServo1;
    private Servo rotationServo2;
    private Servo rotationServo3;
    private Servo aimServo;
    private Servo blockerServo;
    private MotorWrapper intakeMotor;
    private MotorWrapper passthroughMotor;
    private MotorWrapper flMotor;
    private MotorWrapper frMotor;
    private MotorWrapper blMotor;
    private MotorWrapper brMotor;
    private DcMotorEx flywheelMotor1;
    private DcMotorEx flywheelMotor2;
    private MotorWrapper[] motors;
    private Pose2D currentPose;
    private GoBildaPinpointDriver odoPod;
    private boolean blockerRotation = false;


    @Override public void init(HardwareMap hwMap) {
        voltageSensor = hwMap.get(VoltageSensor.class, "Control Hub");
        voltageTimer = new ElapsedTime();

        rotationServo1 = hwMap.get(Servo.class, "rot_servo_1");
        rotationServo2 = hwMap.get(Servo.class, "rot_servo_2");
        rotationServo3 = hwMap.get(Servo.class, "rot_servo_3");
        rotationServo3.setDirection(Servo.Direction.REVERSE);
        aimServo = hwMap.get(Servo.class, "aim_servo");
        blockerServo = hwMap.get(Servo.class, "blocker_servo");

        intakeMotor = new MotorWrapper(hwMap.get(DcMotor.class, "intake_motor"));
        passthroughMotor = new MotorWrapper(hwMap.get(DcMotor.class, "passthrough_motor"));

        flMotor = new MotorWrapper(hwMap.get(DcMotor.class, "fl_motor"));
        frMotor = new MotorWrapper(hwMap.get(DcMotor.class, "fr_motor"));
        blMotor = new MotorWrapper(hwMap.get(DcMotor.class, "bl_motor"));
        brMotor = new MotorWrapper(hwMap.get(DcMotor.class, "br_motor"));

        motors = new MotorWrapper[] {intakeMotor, passthroughMotor, flMotor, frMotor, blMotor, brMotor};
        passthroughMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        flMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        blMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        brMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        for (MotorWrapper motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        flywheelMotor1 = hwMap.get(DcMotorEx.class, "fl_motor");
        flywheelMotor2 = hwMap.get(DcMotorEx.class, "fr_motor");
        flywheelMotor1.setDirection(DcMotorSimple.Direction.FORWARD);
        flywheelMotor2.setDirection(DcMotorSimple.Direction.FORWARD);

        odoPod = hwMap.get(GoBildaPinpointDriver.class, "odo_pod");
        odoPod.resetPosAndIMU();

        aimServo.setPosition(0.5);
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
        for (LM0Board.MotorWrapper motor : motors) {
            motor.write();
        }
        if (blockerRotation){
            double position = blockerServo.getPosition();
            position += 0.05;
            if (position > 1.0) {
                position--;
            }
            blockerServo.setPosition(position);
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

    public void setTurretRotation(double rotation) {
        double pos = rotation * (1.0/360.0);
        rotationServo1.setPosition(pos);
        rotationServo2.setPosition(pos);
        rotationServo3.setPosition(pos);
    }
    public void setAimServoPosition(double pos) {
        aimServo.setPosition(pos);
    }
    public void setBlockerServoPosition(double pos) {
        blockerServo.setPosition(pos);
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

    public double getAimServoPosition() {
        return aimServo.getPosition();
    }
    public Pose2D getCurrentPose() {return currentPose; }

    public void flipKillSwitch() {
        for (MotorWrapper motor : motors) {
            motor.killSwitch = !motor.killSwitch;
        }
    }
    public double getFlywheelMotor1Velocity() {
        return flywheelMotor1.getVelocity();
    }
    public double getFlywheelMotor2Velocity() {
        return flywheelMotor2.getVelocity();
    }
    public void toggleBlockerRotation() {
        blockerRotation = !blockerRotation;
    }
}
