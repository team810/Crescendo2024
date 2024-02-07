package frc.robot.subsystem.shooter;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import frc.lib.MechanismState;
import frc.robot.util.Pneumatics;
import org.littletonrobotics.junction.Logger;

public class ShooterReal implements ShooterIO {

    private final CANSparkMax topMotor;
    private final CANSparkMax bottomMotor;

    private final CANSparkMax barMotor;

    private final RelativeEncoder topEncoder;
    private final RelativeEncoder bottomEncoder;

    private double topVoltage;
    private double bottomVoltage;
    private double barVoltage;

    private final DoubleSolenoid deflector;

    private MechanismState deflectorState;

    private Encoder barEncoder;

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

        deflector = Pneumatics.getInstance().createSolenoid(ShooterConstants.DEFLECTOR_FWD_CHANNEL,
                ShooterConstants.DEFLECTOR_REV_CHANNEL);

        barMotor = new CANSparkMax(ShooterConstants.BAR_ID, CANSparkLowLevel.MotorType.kBrushless);

        barMotor.setSmartCurrentLimit(30);
        barMotor.enableVoltageCompensation(12);
        barMotor.setIdleMode(CANSparkBase.IdleMode.kBrake);
        barMotor.clearFaults();

        barEncoder = new Encoder(0,1);

        deflectorState = MechanismState.stored;

        setTopVoltage(0);
        setBottomVoltage(0);
        setBarVoltage(0);
    }
    @Override
    public void update()
    {
        Logger.recordOutput("Shooter/Top/Voltage", topVoltage);
        Logger.recordOutput("Shooter/Top/CurrentDraw", topMotor.getOutputCurrent());
        Logger.recordOutput("Shooter/Top/Temperature", topMotor.getMotorTemperature());
        Logger.recordOutput("Shooter/Top/Velocity", topEncoder.getVelocity());

        Logger.recordOutput("Shooter/Bottom/Voltage", bottomVoltage);
        Logger.recordOutput("Shooter/Bottom/CurrentDraw", bottomMotor.getOutputCurrent());
        Logger.recordOutput("Shooter/Bottom/Temperature", bottomMotor.getMotorTemperature());
        Logger.recordOutput("Shooter/Bottom/Velocity", bottomEncoder.getVelocity());

        Logger.recordOutput("Shooter/Bar/BarVoltage", getBarVoltage());
        Logger.recordOutput("Shooter/Bar/Temp", barMotor.getMotorTemperature());
        Logger.recordOutput("Shooter/Bar/EncoderValue", getBarPosition());
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

    @Override
    public MechanismState isDeflector() {
        return deflectorState;
    }

    @Override
    public void setDeflector(MechanismState state) {
        this.deflectorState = state;
        if (deflectorState == MechanismState.deployed)
        {
            deflector.set(DoubleSolenoid.Value.kForward);
        } else if (deflectorState == MechanismState.stored) {
            deflector.set(DoubleSolenoid.Value.kReverse);
        }
    }

    @Override
    public void setBarVoltage(double voltage) {
        barVoltage = voltage;
        barMotor.setVoltage(voltage);
    }

    @Override
    public double getBarVoltage() {
        return barVoltage;
    }

    @Override
    public double getBarPosition() {
        return barEncoder.get();
    }
}
