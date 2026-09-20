package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


public class TestBoard implements ProgrammingBoard {
    private Servo servo;
    private DigitalChannel touchSensor;
    private DcMotor motor;
    private AnalogInput pot;
    private double ticksPerRevolution;
    private boolean prevTouchSensorPressed;
    private boolean touchSensorPressed;

    @Override
    public void loop() {
        prevTouchSensorPressed = touchSensorPressed;
        touchSensorPressed = !touchSensor.getState();
    }

    @Override
    public void init(HardwareMap hwMap) {
        touchSensor = hwMap.get(DigitalChannel.class, "touch_sensor");
        motor = hwMap.get(DcMotor.class, "motor");
        servo = hwMap.get(Servo.class, "servo");
        pot = hwMap.get(AnalogInput.class, "pot");

        touchSensor.setMode(DigitalChannel.Mode.INPUT);
        prevTouchSensorPressed = false;
        touchSensorPressed = false;
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        ticksPerRevolution = motor.getMotorType().getTicksPerRev();
        motor.getPower();
    }


    public void setMotorSpeed(double speed) {
        motor.setPower(speed);
    }
    public double getMotorRevolutions() {
        return motor.getCurrentPosition() / ticksPerRevolution;
    }

    public boolean getTouchSensorState() {
        return touchSensorPressed;
    }
    public boolean isTouchSensorReleased() {
        return (prevTouchSensorPressed && !touchSensorPressed);
    }
    public double getPotAngle() {
        return Range.scale(pot.getVoltage(), 0, pot.getMaxVoltage(), 0, 270);
    }
    public void setServoPosition(double position) {
        servo.setPosition(position);
    }
}
