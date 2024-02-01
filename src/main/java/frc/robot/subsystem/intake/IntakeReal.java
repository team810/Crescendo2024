package frc.robot.subsystem.intake;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import org.littletonrobotics.junction.Logger;

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

    public void setVoltage(double voltage) {
        topMotor.setVoltage(-voltage);
        bottomMotor.set(voltage);
    }

    public void update() {
        Logger.recordOutput("Intake/Top/Temperature", topMotor.getMotorTemperature());
        Logger.recordOutput("Intake/Top/CurrentDraw", topMotor.getOutputCurrent());
        Logger.recordOutput("Intake/Top/Voltage", topMotor.getBusVoltage());

        Logger.recordOutput("Intake/Bottom/Temperature", bottomMotor.getMotorTemperature());
        Logger.recordOutput("Intake/Bottom/CurrentDraw", bottomMotor.getOutputCurrent());
        Logger.recordOutput("Intake/Bottom/Voltage", bottomMotor.getBusVoltage());
    }
}
