package frc.robot.subsystem.shooter;

public interface ShooterIO {

    public void setTopTargetRPM(double targetRPM);
    public void setBottomTargetRPM(double targetRPM);

    public void update();
}
