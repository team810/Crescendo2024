package frc.robot.subsystem.tbone;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.MechanismState;
import frc.robot.Robot;
import org.littletonrobotics.junction.Logger;

public class TBoneSubsystem extends SubsystemBase {
    private static TBoneSubsystem INSTANCE;
    public static TBoneSubsystem getInstance() {
        if (INSTANCE == null) { INSTANCE = new TBoneSubsystem(); }
        return INSTANCE;
    }
    private final TBoneIO tBone;

    private MechanismState state;
    private final PIDController controller = new PIDController(4,0.1,0);

    private double setpoint;

    private TBoneSubsystem() {
        if (Robot.isReal())
        {
            tBone = new TboneReal();
        }else{
            tBone = new TBoneSim();
        }

        state = MechanismState.stored;
    }

    @Override
    public void periodic() {

        switch (getState())
        {
            case deployed -> {
                setpoint = TboneConstants.DEPLOY_SETPOINT;
            }
            case stored -> {
                setpoint = TboneConstants.STORED_SETPOINT;
            }
        }

        if (RobotState.isEnabled())
        {
            tBone.setVoltage(
                    MathUtil.clamp(
                            controller.calculate(tBone.getEncoderPosition(), setpoint),-12,12
                    )
            );
        }

        Logger.recordOutput("T-Bone/Setpoint", setpoint);
        tBone.update();
    }

    public MechanismState getState() {
        return state;
    }

    public void setState(MechanismState state) {
        this.state = state;
    }

    public void toggleState() {
        if (this.state == MechanismState.deployed) {
            this.state = MechanismState.stored;
        } else { this.state = MechanismState.deployed; }
    }
}

