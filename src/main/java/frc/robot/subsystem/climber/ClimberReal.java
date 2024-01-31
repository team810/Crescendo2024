package frc.robot.subsystem.climber;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;

public class ClimberReal implements ClimberIO {

    private CANSparkMax climberMotor;

    public ClimberReal() {

        climberMotor = new CANSparkMax(ClimberConstants.CLIMBER_ID,
                CANSparkLowLevel.MotorType.kBrushless);

        climberMotor.clearFaults();

        climberMotor.setSmartCurrentLimit(40);

        climberMotor.setIdleMode(CANSparkBase.IdleMode.kBrake);

        climberMotor.set(0);
    }

    public void setSpeed(double speed) {

        climberMotor.set(speed);

    }
}
