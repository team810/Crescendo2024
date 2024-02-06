package frc.robot.subsystem.intake;

public interface IntakeIO {

    void setVoltage(double voltage);

    boolean isLimitSwitchTriggered();

    void update();
}
