package frc.robot.subsystem.intake;

import com.revrobotics.CANSparkMax;

public interface IntakeIO {

    void setVoltage(double voltage);

    void update();
}
