package frc.robot.subsystem.shooter;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.MechanismState;
import frc.robot.Robot;
import org.littletonrobotics.junction.Logger;

public class ShooterSubsystem extends SubsystemBase {
    private final static ShooterSubsystem INSTANCE = new ShooterSubsystem();
    private final ShooterIO shooter;

    private double topTargetRPM;
    private double bottomTargetRPM;

    private final PIDController topController;
    private final PIDController bottomController;

    private MechanismState deflectorState;
    private MechanismState barState;

    private ShooterSubsystem() {

        topController = new PIDController(0,0,0);
        bottomController = new PIDController(0,0,0);

        if (Robot.isReal())
        {
            shooter = new ShooterNeo();

            topController.setP(ShooterConstants.TOP_CONTROLLER_REAL.kP);
            topController.setI(ShooterConstants.TOP_CONTROLLER_REAL.kI);
            topController.setD(ShooterConstants.TOP_CONTROLLER_REAL.kD);

            bottomController.setP(ShooterConstants.BOTTOM_CONTROLLER_REAL.kP);
            bottomController.setI(ShooterConstants.BOTTOM_CONTROLLER_REAL.kI);
            bottomController.setD(ShooterConstants.BOTTOM_CONTROLLER_REAL.kD);
        }else{
            shooter = new ShooterSim();

            topController.setP(ShooterConstants.TOP_CONTROLLER_SIM.kP);
            topController.setI(ShooterConstants.TOP_CONTROLLER_SIM.kI);
            topController.setD(ShooterConstants.TOP_CONTROLLER_SIM.kD);

            bottomController.setP(ShooterConstants.BOTTOM_CONTROLLER_SIM.kP);
            bottomController.setI(ShooterConstants.BOTTOM_CONTROLLER_SIM.kI);
            bottomController.setD(ShooterConstants.BOTTOM_CONTROLLER_SIM.kD);
        }

        topController.setTolerance(ShooterConstants.PID_CONTROLLER_TOLERANCE);
        bottomController.setTolerance(ShooterConstants.PID_CONTROLLER_TOLERANCE);

        barState = MechanismState.stored;
        deflectorState = MechanismState.deployed;
    }

    @Override
    public void periodic() {

        topTargetRPM = MathUtil.clamp(topTargetRPM, ShooterConstants.TOP_MOTOR_MAX_RPM, -ShooterConstants.TOP_MOTOR_MAX_RPM);
        bottomTargetRPM = MathUtil.clamp(topTargetRPM, ShooterConstants.BOTTOM_MOTOR_MAX_RPM, -ShooterConstants.BOTTOM_MOTOR_MAX_RPM);
        if (RobotState.isEnabled())
        {
            double topVoltage = topController.calculate(
                    shooter.getTopRPM(),
                    topTargetRPM
            );
            topVoltage = MathUtil.clamp(topVoltage, -12, 12);
            shooter.setTopVoltage(topVoltage);

            double bottomVoltage = bottomController.calculate(
                    shooter.getBottomRPM(),
                    bottomTargetRPM
            );
            bottomVoltage = MathUtil.clamp(bottomVoltage, -12, 12);
            shooter.setBottomVoltage(bottomVoltage);

        } else if (RobotState.isDisabled()) {
            topController.reset();
            bottomController.reset();
        }

        shooter.update();

        Logger.recordOutput("Shooter/Top/TargetRPM", topTargetRPM);
        Logger.recordOutput("Shooter/Top/AtSetpoint", topController.atSetpoint());

        Logger.recordOutput("Shooter/Bottom/TargetRPM", bottomTargetRPM);
        Logger.recordOutput("Shooter/Bottom/AtSetpoint", bottomController.atSetpoint());

        Logger.recordOutput("Shooter/Bar/State", barState);
        Logger.recordOutput("Shooter/Deflector/State", deflectorState);
    }
    public static ShooterSubsystem getInstance() {
        return INSTANCE;
    }

    public MechanismState getDeflectorState() {
        return deflectorState;
    }

    public void setDeflectorState(MechanismState deflectorState) {
        this.deflectorState = deflectorState;
        shooter.setDeflector(this.deflectorState);
    }

    public MechanismState getBarState() {
        return barState;
    }

    public void setBarState(MechanismState barState) {
        this.barState = barState;
        shooter.setBarState(this.barState);
    }
}


