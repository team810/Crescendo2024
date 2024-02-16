package frc.robot.subsystem.shooter;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import org.littletonrobotics.junction.Logger;

public class ShooterReal implements ShooterIO {

    private final CANSparkMax topMotor;
    private final CANSparkMax bottomMotor;

    private final RelativeEncoder topEncoder;
    private final RelativeEncoder bottomEncoder;

    private double topVoltage;
    private double bottomVoltage;

    public ShooterReal()
    {
        topMotor = new CANSparkMax(ShooterConstants.TOP_MOTOR_ID, CANSparkLowLevel.MotorType.kBrushless);
        bottomMotor = new CANSparkMax(ShooterConstants.BOTTOM_MOTOR_ID, CANSparkLowLevel.MotorType.kBrushless);

        topMotor.setSmartCurrentLimit(40);
        bottomMotor.setSmartCurrentLimit(40);

        topMotor.enableVoltageCompensation(12);
        bottomMotor.enableVoltageCompensation(12);

        topMotor.setIdleMode(CANSparkBase.IdleMode.kCoast);
        bottomMotor.setIdleMode(CANSparkBase.IdleMode.kCoast);

        topMotor.clearFaults();
        bottomMotor.clearFaults();

        topEncoder = topMotor.getEncoder();
        bottomEncoder = bottomMotor.getEncoder();

        setTopVoltage(0);
        setBottomVoltage(0);

        topMotor.setInverted(true);
    }
    @Override
    public void update()
    {
        topMotor.setVoltage(topVoltage);
        bottomMotor.setVoltage(bottomVoltage);

        Logger.recordOutput("Shooter/Top/Voltage", topVoltage);
        Logger.recordOutput("Shooter/Top/CurrentDraw", topMotor.getOutputCurrent());
        Logger.recordOutput("Shooter/Top/Temperature", topMotor.getMotorTemperature());
        Logger.recordOutput("Shooter/Top/Velocity", topEncoder.getVelocity());

        Logger.recordOutput("Shooter/Bottom/Voltage", bottomVoltage);
        Logger.recordOutput("Shooter/Bottom/CurrentDraw", bottomMotor.getOutputCurrent());
        Logger.recordOutput("Shooter/Bottom/Temperature", bottomMotor.getMotorTemperature());
        Logger.recordOutput("Shooter/Bottom/Velocity", bottomEncoder.getVelocity());
    }
    @Override
    public void setTopVoltage(double voltage) {
        topVoltage = voltage;
    }
    @Override
    public void setBottomVoltage(double voltage) {
        bottomVoltage = voltage;
    }
    @Override
    public double getTopRPM() {
        return topEncoder.getVelocity();
    }
    @Override
    public double getBottomRPM() {
        return bottomEncoder.getVelocity();
    }

}
