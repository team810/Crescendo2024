package frc.robot.subsystem.shooter;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.simulation.DoubleSolenoidSim;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import frc.lib.MechanismState;
import frc.robot.Robot;
import frc.robot.util.Pneumatics;
import org.littletonrobotics.junction.Logger;

public class ShooterSim implements ShooterIO{

    private final FlywheelSim topMotor;
    private final FlywheelSim bottomMotor;

    private double topVoltage;
    private double bottomVoltage;

    private final DoubleSolenoidSim deflector;
    private final DoubleSolenoidSim bar;

    private MechanismState deflectorState;
    private MechanismState barState;


    public ShooterSim()
    {
        topMotor = new FlywheelSim(DCMotor.getNEO(1), 1, 0.1);
        bottomMotor = new FlywheelSim(DCMotor.getNEO(1), 1, 0.1);

        deflectorState = MechanismState.stored;
        barState = MechanismState.stored;

        deflector = new DoubleSolenoidSim(Pneumatics.getInstance().getPneumaticsHubSim(), ShooterConstants.DEFLECTOR_FWD_CHANNEL, ShooterConstants.DEFLECTOR_REV_CHANNEL);
        bar = new DoubleSolenoidSim(Pneumatics.getInstance().getPneumaticsHubSim(), ShooterConstants.BAR_FWD_CHANNEL, ShooterConstants.DEFLECTOR_REV_CHANNEL);
    }

    @Override
    public void update() {
        topMotor.update(Robot.defaultPeriodSecs);
        bottomMotor.update(Robot.defaultPeriodSecs);

        Logger.recordOutput("Shooter/Top/Voltage", topVoltage);
        Logger.recordOutput("Shooter/Top/CurrentDraw", topMotor.getCurrentDrawAmps());
        Logger.recordOutput("Shooter/Top/Velocity", topMotor.getAngularVelocityRPM());

        Logger.recordOutput("Shooter/Bottom/Voltage", bottomVoltage);
        Logger.recordOutput("Shooter/Bottom/CurrentDraw", bottomMotor.getCurrentDrawAmps());
        Logger.recordOutput("Shooter/Bottom/Velocity", bottomMotor.getAngularVelocityRPM());
    }

    @Override
    public void setTopVoltage(double voltage) {
        topVoltage = voltage;
        topMotor.setInputVoltage(topVoltage);
    }

    @Override
    public void setBottomVoltage(double voltage) {
        bottomVoltage = voltage;
        bottomMotor.setInputVoltage(bottomVoltage);
    }

    @Override
    public double getTopRPM() {
        return topMotor.getAngularVelocityRPM();
    }

    @Override
    public double getBottomRPM() {
        return bottomMotor.getAngularVelocityRPM();
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
    public MechanismState getBarState() {
        return barState;
    }

    @Override
    public void setBarState(MechanismState state) {
        this.barState = state;
        if (barState == MechanismState.deployed)
        {
            bar.set(DoubleSolenoid.Value.kForward);
        } else if (barState == MechanismState.stored) {
            bar.set(DoubleSolenoid.Value.kReverse);
        }
    }
}
