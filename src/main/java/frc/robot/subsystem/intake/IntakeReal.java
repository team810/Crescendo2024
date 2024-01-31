package frc.robot.subsystem.intake;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;

public class IntakeReal implements IntakeIO {

    private CANSparkMax topMotor;
    private CANSparkMax bottomMotor;

    public IntakeReal() {

        topMotor = new CANSparkMax(IntakeConstants.TOP_ID,
                CANSparkLowLevel.MotorType.kBrushless);

        bottomMotor = new CANSparkMax(IntakeConstants.BOTTOM_ID,
                CANSparkLowLevel.MotorType.kBrushless);

        topMotor.clearFaults();
        bottomMotor.clearFaults();

        topMotor.setSmartCurrentLimit(40);
        bottomMotor.setSmartCurrentLimit(40);

        topMotor.setIdleMode(CANSparkBase.IdleMode.kBrake);
        bottomMotor.setIdleMode(CANSparkBase.IdleMode.kBrake);

        topMotor.set(0);
        bottomMotor.set(0);
    }

    public void setSpeed(double speed) {
        topMotor.set(-speed);
        bottomMotor.set(speed);
    }
}
