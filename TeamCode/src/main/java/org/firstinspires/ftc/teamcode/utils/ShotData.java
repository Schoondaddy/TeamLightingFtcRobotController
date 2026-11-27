package org.firstinspires.ftc.teamcode.utils;

public class ShotData {
    public static final String HEADER = "fw,ha,dist";
    private final double hoodAngle;
    private final double flywheelSpeed;
    private final double dist;
    public ShotData(double hoodAngle, double flywheelSpeed, double dist) {
        this.hoodAngle = hoodAngle;
        this.flywheelSpeed = flywheelSpeed;
        this.dist = dist;
    }
    public String toCsvRow() {
        return hoodAngle + "," + flywheelSpeed + "," + dist;
    }
}