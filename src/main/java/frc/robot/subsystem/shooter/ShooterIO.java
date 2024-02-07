package frc.robot.subsystem.shooter;

import frc.lib.MechanismState;

public interface ShooterIO {
    public void setTopVoltage(double voltage);
    public void setBottomVoltage(double voltage);
    public double getTopRPM();
    public double getBottomRPM();
    public MechanismState isDeflector();
    public void setDeflector(MechanismState state);

    public void setBarVoltage(double voltage);
    public double getBarVoltage();
    public double getBarPosition();
    public void update();
}
