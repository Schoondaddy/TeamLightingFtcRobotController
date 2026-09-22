package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 *
 */
public interface ProgrammingBoard {
     void readLoop();
     void writeLoop();

     void init(HardwareMap hwMap);
}