package frc.robot.util.Shooting;

import frc.lib.MechanismState;

public class ShooterState {
    private double topRPM;
    private double bottomRPM;
    private MechanismState deflectorState;

    public ShooterState(double topRPM, double bottomRPM, MechanismState deflectorState)
    {
        this.topRPM = topRPM;
        this.bottomRPM = bottomRPM;
        this.deflectorState = deflectorState;
    }

    public double getTopRPM() {
        return topRPM;
    }

    public void setTopRPM(double topRPM) {
        this.topRPM = topRPM;
    }

    public double getBottomRPM() {
        return bottomRPM;
    }

    public void setBottomRPM(double bottomRPM) {
        this.bottomRPM = bottomRPM;
    }

    public MechanismState getDeflectorState() {
        return deflectorState;
    }

    public void setDeflectorState(MechanismState deflectorState) {
        this.deflectorState = deflectorState;
    }

}
