package frc.robot.subsystem.shooter;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.MechanismState;
import frc.robot.Robot;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.util.Shooting.ShooterUtil;
import org.littletonrobotics.junction.Logger;

public class ShooterSubsystem extends SubsystemBase {
    private static ShooterSubsystem INSTANCE;
    private final ShooterIO shooter;

    private double topTargetRPM;
    private double bottomTargetRPM;

    private final PIDController topController;
    private final PIDController bottomController;

    private MechanismState deflectorState;

    private BarState barState;

    private ShooterMode shooterState;

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

        shooterState = ShooterMode.off;
        barState = BarState.stored;
        deflectorState = MechanismState.deployed;
    }

    @Override
    public void periodic() {
        if (RobotState.isEnabled())
        {
            double topVoltage = 0;
            double bottomVoltage = 0;

            switch (shooterState)
            {
                case SourceIntake -> {
                    setDeflectorState(MechanismState.deployed);
                    topVoltage = 12 * ShooterConstants.SOURCE_INTAKE_SPEED;
                    bottomVoltage = 12 * ShooterConstants.SOURCE_INTAKE_SPEED;

                }
                case Amp -> {
                    topTargetRPM = ShooterConstants.AMP_SHOOTING_SPEED;
                    bottomTargetRPM = ShooterConstants.AMP_SHOOTING_SPEED;

                    topTargetRPM = MathUtil.clamp(topTargetRPM, ShooterConstants.TOP_MOTOR_MAX_RPM, -ShooterConstants.TOP_MOTOR_MAX_RPM);
                    bottomTargetRPM = MathUtil.clamp(bottomTargetRPM, ShooterConstants.BOTTOM_MOTOR_MAX_RPM, -ShooterConstants.BOTTOM_MOTOR_MAX_RPM);

                    topVoltage = topController.calculate(
                            shooter.getTopRPM(),
                            topTargetRPM
                    );
                    bottomVoltage = bottomController.calculate(
                            shooter.getBottomRPM(),
                            bottomTargetRPM
                    );
                    setDeflectorState(MechanismState.stored);
                }
                case Speaker -> {

                    ShooterState state = ShooterUtil.calculateTargetSpeeds(DrivetrainSubsystem.getInstance().getPose());
                    topTargetRPM = MathUtil.clamp(
                            state.getTopSpeed(),
                            ShooterConstants.TOP_MOTOR_MAX_RPM,
                            -ShooterConstants.TOP_MOTOR_MAX_RPM);

                    topTargetRPM = MathUtil.clamp(
                            state.getBottomSpeed(),
                            ShooterConstants.BOTTOM_MOTOR_MAX_RPM,
                            -ShooterConstants.BOTTOM_MOTOR_MAX_RPM);

                    topVoltage = topController.calculate(
                            shooter.getTopRPM(),
                            topTargetRPM
                    );
                    bottomVoltage = bottomController.calculate(
                            shooter.getBottomRPM(),
                            bottomTargetRPM
                    );

                    setDeflectorState(state.getDeflectorState());

                }
                case off -> {
                    topTargetRPM = 0;
                    bottomTargetRPM = 0;

                    bottomController.reset();
                    topController.reset();

                    topVoltage = 0;
                    bottomVoltage = 0;

                }
            }
            switch (barState)
            {
                case hold -> {
                    shooter.setBarVoltage(ShooterConstants.BAR_HOLD_PERCENT * 12);
                }
                case revs -> {
                    shooter.setBarVoltage(-ShooterConstants.BAR_SPEED * 12);
                }
                case fwd -> {
                    shooter.setBarVoltage(ShooterConstants.BAR_SPEED * 12);
                }
                default -> {
                    shooter.setBarVoltage(0);
                }
            }
            topVoltage = MathUtil.clamp(topVoltage, -12, 12);
            bottomVoltage = MathUtil.clamp(bottomVoltage, -12, 12);
            shooter.setTopVoltage(topVoltage);
            shooter.setBottomVoltage(bottomVoltage);

        } else if (RobotState.isDisabled()) {
            topController.reset();
            bottomController.reset();

            topTargetRPM = 0;
            bottomTargetRPM = 0;
        }

        shooter.update();

        Logger.recordOutput("Shooter/Top/TargetRPM", topTargetRPM);
        Logger.recordOutput("Shooter/Top/AtSetpoint", topController.atSetpoint());

        Logger.recordOutput("Shooter/Bottom/TargetRPM", bottomTargetRPM);
        Logger.recordOutput("Shooter/Bottom/AtSetpoint", bottomController.atSetpoint());

        Logger.recordOutput("Shooter/Bar/State", barState);
        Logger.recordOutput("Shooter/Deflector/State", deflectorState);
    }
    public MechanismState getDeflectorState() {
        return deflectorState;
    }

    public void setDeflectorState(MechanismState deflectorState) {
        this.deflectorState = deflectorState;
        shooter.setDeflector(this.deflectorState);
    }

    public void setShooterState(ShooterMode shooterState) {
        this.shooterState = shooterState;
    }

    public BarState getBarState() {
        return barState;
    }

    public void setBarState(BarState barState) {
        this.barState = barState;
    }

    public static ShooterSubsystem getInstance() {
        if (INSTANCE == null)
        {
            INSTANCE = new ShooterSubsystem();
        }
        return INSTANCE;
    }
}


