package frc.robot.subsystem.tbone;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DutyCycleEncoder;

public class TboneReal implements TBoneIO {

    private CANSparkMax motor;

    private double inputVoltage;

    private DutyCycleEncoder encoder;

    public TboneReal() {

        motor = new CANSparkMax(TboneConstants.TBONE_MOTOR_ID,
                CANSparkLowLevel.MotorType.kBrushed);

        motor.setInverted(true);

        motor.enableVoltageCompensation(12);

        motor.clearFaults();

        motor.setSmartCurrentLimit(40);

        encoder = new DutyCycleEncoder(0);

        motor.setIdleMode(CANSparkBase.IdleMode.kBrake);

        inputVoltage = 0;
        setVoltage(0);
    }

    @Override
    public double getEncoderPosition() {
        return encoder.getAbsolutePosition();
    }

    public void setVoltage(double voltage) {
        inputVoltage = voltage;
        motor.set(inputVoltage);

    }

    public void update() {
    }
}
