package frc.robot.subsystem.shooter;

import frc.lib.MechanismState;

public class ShooterState {

    private final double topSpeed;
    private final double bottomSpeed;
    private final MechanismState deflectorState;

    public ShooterState(double topSpeed, double bottomSpeed, MechanismState deflectorState)
    {
        this.topSpeed = topSpeed;
        this.bottomSpeed = bottomSpeed;
        this.deflectorState = deflectorState;
    }

    public double getTopSpeed() {
        return topSpeed;
    }

    public double getBottomSpeed() {
        return bottomSpeed;
    }

    public MechanismState getDeflectorState() {
        return deflectorState;
    }
}
