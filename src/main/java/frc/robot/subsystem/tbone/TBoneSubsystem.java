package frc.robot.subsystem.tbone;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.MechanismState;

public class TBoneSubsystem extends SubsystemBase {
    private static TBoneSubsystem INSTANCE;
    public static TBoneSubsystem getInstance() {
        return INSTANCE;
    }

    private final TBoneIO tBone;

    private MechanismState state;
    private final PIDController controller = new PIDController(0,0,0);

    double setpoint;

    private TBoneSubsystem() {
        tBone = new TboneReal();
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
                            controller.calculate(tBone.getEncoderPosition(),setpoint),-12,12
                    )
            );
        }

    }

    public MechanismState getState() {
        return state;
    }

    public void setState(MechanismState state) {
        this.state = state;
    }


}

