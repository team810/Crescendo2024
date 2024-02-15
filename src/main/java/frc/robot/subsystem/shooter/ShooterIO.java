package frc.robot.subsystem.shooter;

import frc.lib.MechanismState;

public interface ShooterIO {
    public void setTopVoltage(double voltage);
    public void setBottomVoltage(double voltage);
    public double getTopRPM();
    public double getBottomRPM();

    public void update();
}
