package org.firstinspires.ftc.teamcode.mechanisms;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

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
    private DcMotorEx flywheelMotor1;
    private DcMotorEx flywheelMotor2;
    private MotorWrapper motors[];

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
        motors = new MotorWrapper[] {intakeMotor, passthroughMotor};

        flywheelMotor1 = hwMap.get(DcMotorEx.class, "fl_motor");
        flywheelMotor2 = hwMap.get(DcMotorEx.class, "fr_motor");
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
        for (LM0Board.MotorWrapper motor : motors) {
            motor.write();
        }
    }
    public double getCachedVoltage() {
        return cachedVoltage;
    }
    public void setTurretRotation(double rotation) {
        double pos = rotation * (1.0/360.0);
        rotationServo1.setPosition(pos);
        rotationServo2.setPosition(pos);
        rotationServo3.setPosition(pos);
    }
    public void setAimServoPostion(double pos) {
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
}
