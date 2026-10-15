package org.firstinspires.ftc.teamcode.states.misc;

import org.firstinspires.ftc.teamcode.mechanisms.TestBoard;
import org.firstinspires.ftc.teamcode.states.RobotState;
import org.firstinspires.ftc.teamcode.states.StateCategory;

public class ExampleState extends RobotState<TestBoard> {
    public ExampleState() { super(StateCategory.NONE); }

    @Override
    public void onStateStart() {
        System.out.println("This state was transitioned into!");
    }

    @Override
    public void onStateLoop() {
        System.out.println("This state was active during an op-mode loop!");
    }

    @Override
    public void onStateEnd() {
        System.out.println("This state was transitioned out of!");
    }
}
