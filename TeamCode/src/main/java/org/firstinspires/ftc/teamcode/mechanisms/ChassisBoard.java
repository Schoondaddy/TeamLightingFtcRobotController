package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class ChassisBoard implements ProgrammingBoard {
    private static class MotorWrapper {
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
        private void write() {
            if (cachedPower != prevCachedPower) {
                motor.setPower(cachedPower);
            }
        }
    }
    private MotorWrapper flMotor;
    private MotorWrapper frMotor;
    private MotorWrapper blMotor;
    private MotorWrapper brMotor;
    private MotorWrapper[] motors;
    private ElapsedTime voltageTimer;
    private VoltageSensor voltageSensor;
    private double cachedVoltage = 12;
    @Override
    public void init(HardwareMap hwMap) {
        voltageSensor = hwMap.get(VoltageSensor.class, "Control Hub");
        voltageTimer = new ElapsedTime();

        flMotor = new MotorWrapper(hwMap.get(DcMotor.class, "fl_motor"));
        frMotor = new MotorWrapper(hwMap.get(DcMotor.class, "fr_motor"));
        blMotor = new MotorWrapper(hwMap.get(DcMotor.class, "bl_motor"));
        brMotor = new MotorWrapper(hwMap.get(DcMotor.class, "br_motor"));

        motors = new MotorWrapper[] {flMotor, frMotor, blMotor, brMotor};
        flMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        blMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        brMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        flMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        blMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        brMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    @Override
    public void readLoop() {
        if (voltageTimer.seconds() > 0.05) {
            cachedVoltage = voltageSensor.getVoltage();
        }
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
    public void setFlMotorPower(double power) {
        flMotor.cachePower(power);
    }
    public void setFrMotorPower(double power) {
        frMotor.cachePower(power);
    }

    public void setBlMotorPower(double power) {
        blMotor.cachePower(power);
    }

    public void setBrMotorPower(double power ) {
        brMotor.cachePower(power);
    }
}
