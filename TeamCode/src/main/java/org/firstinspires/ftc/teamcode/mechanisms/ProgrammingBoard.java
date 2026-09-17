package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ProgrammingBoard {
    private DcMotor flMotor;
    private DcMotor frMotor;
    private DcMotor blMotor;
    private DcMotor brMotor;
    public ProgrammingBoard (HardwareMap hwMap){
        flMotor = hwMap.get(DcMotor.class, "flmotor");
        frMotor = hwMap.get(DcMotor.class, "frmotor");
        blMotor = hwMap.get(DcMotor.class, "blmotor");
        brMotor = hwMap.get(DcMotor.class, "brmotor");
        flMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setAllMotorSpeed(double speed) {
        flMotor.setPower(speed);
        blMotor.setPower(speed);
        frMotor.setPower(speed);
        brMotor.setPower(speed);
    }
}
