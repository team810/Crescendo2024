package frc.robot.subsystem.climber;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import org.littletonrobotics.junction.Logger;

public class ClimberReal implements ClimberIO {

    private final CANSparkMax climberMotor;

    private double inputVoltage;

    public ClimberReal() {

        climberMotor = new CANSparkMax(ClimberConstants.CLIMBER_ID,
                CANSparkLowLevel.MotorType.kBrushless);

        climberMotor.clearFaults();

        climberMotor.enableVoltageCompensation(12);

        climberMotor.setSmartCurrentLimit(40);

        climberMotor.setIdleMode(CANSparkBase.IdleMode.kBrake);

        inputVoltage = 0;

        setVoltage(0);
    }

    public void setVoltage(double voltage) {

        this.inputVoltage = voltage;
        climberMotor.set(voltage);

    }

    public void update() {
        Logger.recordOutput("Climber/Temperature", climberMotor.getMotorTemperature());
        Logger.recordOutput("Climber/CurrentDraw", climberMotor.getOutputCurrent());
        Logger.recordOutput("Climber/motorVoltage", climberMotor.getBusVoltage());
        Logger.recordOutput("Climber/inputVoltage", this.inputVoltage);
    }
}