package org.firstinspires.ftc.teamcode.mechanisms;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class TurretBoard implements ProgrammingBoard {
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
    private MotorWrapper flywheelMotor1;
    private MotorWrapper flywheelMotor2;
    private MotorWrapper motors[];

    @Override public void init(HardwareMap hwMap) {
        voltageSensor = hwMap.get(VoltageSensor.class, "Control Hub");
        voltageTimer = new ElapsedTime();

        rotationServo1 = hwMap.get(Servo.class, "rot_servo_1");
        rotationServo2 = hwMap.get(Servo.class, "rot_servo_2");
        rotationServo3 = hwMap.get(Servo.class, "rot_servo_3");
        aimServo = hwMap.get(Servo.class, "aim_servo");
        blockerServo = hwMap.get(Servo.class, "blocker_servo");

        flywheelMotor1 = new TurretBoard.MotorWrapper(hwMap.get(DcMotor.class, "fl_motor"));
        flywheelMotor2 = new TurretBoard.MotorWrapper(hwMap.get(DcMotor.class, "fr_motor"));

        motors = new TurretBoard.MotorWrapper[] {flywheelMotor1, flywheelMotor2};
        flywheelMotor1.setDirection(DcMotorSimple.Direction.FORWARD);
        flywheelMotor2.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void readLoop() {
        if (voltageTimer.seconds() > 0.05) {
            cachedVoltage = voltageSensor.getVoltage();
        }
    }
    @Override
    public void writeLoop() {
        for (TurretBoard.MotorWrapper motor : motors) {
            motor.write();
        }
    }
    public double getCachedVoltage() {
        return cachedVoltage;
    }
    public void setFlywheelMotor1Power(double power) {
        flywheelMotor1.cachePower(power);
    }
    public void setFlywheelMotor2Power(double power) {
        flywheelMotor2.cachePower(power);
    }
}
