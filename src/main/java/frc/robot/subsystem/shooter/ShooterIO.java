package frc.robot.subsystem.shooter;

import frc.lib.MechanismState;

public interface ShooterIO {
    public void setTopVoltage(double voltage);
    public void setBottomVoltage(double voltage);
    public double getTopRPM();
    public double getBottomRPM();
    public MechanismState isDeflector();
    public void setDeflector(MechanismState state);
    public BarState getBarState();
    public void setBarState(BarState state);
    public void update();
}
