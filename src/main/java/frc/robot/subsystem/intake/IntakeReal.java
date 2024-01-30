package frc.robot.subsystem.intake;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;

public class IntakeReal implements IntakeIO {

    private CANSparkMax intakeMotor;

    public IntakeReal() {

        intakeMotor = new CANSparkMax(IntakeConstants.INTAKE_ID,
                CANSparkLowLevel.MotorType.kBrushless);

        intakeMotor.clearFaults();

        intakeMotor.setSmartCurrentLimit(40);

        intakeMotor.setIdleMode(CANSparkBase.IdleMode.kBrake);

        intakeMotor.set(0);
    }

    public void setSpeed(double speed) {
        intakeMotor.set(speed);
    }
}
