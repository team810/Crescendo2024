package frc.robot.subsystem.deflector;


import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.MechanismState;
import frc.robot.Robot;
import org.littletonrobotics.junction.Logger;

public class DeflectorSubsystem extends SubsystemBase {

    private static DeflectorSubsystem INSTANCE;
    private DeflectorIO deflector;
    private MechanismState deflectorState;

    private DeflectorSubsystem() {
        if (Robot.isReal())
        {
            deflector = new DeflectorReal();
        }else{
            deflector = new DeflectorSim();
        }
        deflectorState = MechanismState.stored;
        deflector.setState(DoubleSolenoid.Value.kReverse);
    }

    @Override
    public void periodic() {

        Logger.recordOutput("Deflector/MechanismState", deflectorState);
    }

    public MechanismState getDeflectorState() {
        return deflectorState;
    }

    public void setDeflectorState(MechanismState deflectorState) {
        this.deflectorState = deflectorState;
    }

    public void toggleDeflectorState()
    {
        if (deflectorState == MechanismState.deployed) {
            deflector.setState(DoubleSolenoid.Value.kForward);
            deflectorState = MechanismState.stored;
        } else if (deflectorState == MechanismState.stored) {
            deflector.setState(DoubleSolenoid.Value.kReverse);
            deflectorState = MechanismState.deployed;
        }
    }

    public static DeflectorSubsystem getInstance() {
        if (INSTANCE == null)
        {
            INSTANCE = new DeflectorSubsystem();
        }
        return INSTANCE;
    }
}

