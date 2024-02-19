package frc.robot.subsystem.climber;

public interface ClimberIO {

    void setVoltage(double voltage);
    void update();
    void release();

    void pin();


}
