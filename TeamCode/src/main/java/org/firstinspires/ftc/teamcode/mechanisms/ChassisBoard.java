package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class ChassisBoard implements ProgrammingBoard {
    private DcMotor flMotor;
    private DcMotor frMotor;
    private DcMotor blMotor;
    private DcMotor brMotor;
    private DcMotor[] motors;
    private ElapsedTime voltageTimer;
    private VoltageSensor voltageSensor;
    private double cachedVoltage = 12;
    @Override
    public void init(HardwareMap hwMap) {
        voltageSensor = hwMap.get(VoltageSensor.class, "Control Hub");
        voltageTimer = new ElapsedTime();
        flMotor = hwMap.get(DcMotor.class, "fl_motor");
        frMotor = hwMap.get(DcMotor.class, "fr_motor");
        blMotor = hwMap.get(DcMotor.class, "bl_motor");
        brMotor = hwMap.get(DcMotor.class, "br_motor");

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
    public void loop() {
        if (voltageTimer.seconds() > 0.05) {
            cachedVoltage = voltageSensor.getVoltage();
        }
    }

    public void setFlMotorPower(double power) {
        flMotor.setPower(power);
    }
    public void setFrMotorPower(double power) {
        frMotor.setPower(power);
    }
    public void setBlMotorPower(double power) {
        blMotor.setPower(power);
    }
    public void setBrMotorPower(double power) {
        brMotor.setPower(power);
    }
    public double getCachedVoltage() {
        return cachedVoltage;
    }
}
