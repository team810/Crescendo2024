package frc.robot.subsystem.shooter;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.MechanismState;
import frc.robot.Robot;
import org.littletonrobotics.junction.Logger;

public class ShooterSubsystem extends SubsystemBase {
    private static ShooterSubsystem INSTANCE;
    private final ShooterIO shooter;

    private double topTargetRPM;
    private double bottomTargetRPM;

    private final PIDController topController; // Velocity controller
    private final PIDController bottomController; // Velocity controller
    private final PIDController barController; // Bar Controller

    private MechanismState deflectorState;
    private BarState barState;

    private double barSetpoint;

    private ShooterMode shooterMode;

    private double kP = 0, kI = 0, kD = 0;

    private ShooterState testingState;

    private ShooterSubsystem() {

        topController = new PIDController(kP,kI,kD);
        bottomController = new PIDController(0,0,0);
        barController = new PIDController(0,0,0);

        if (Robot.isReal())
        {
            shooter = new ShooterReal();

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

        shooterMode = ShooterMode.off;
        barState = BarState.stored;
        deflectorState = MechanismState.deployed;
        barSetpoint = 0;


        SmartDashboard.putNumber("Shooter Top Speed", 0);
        SmartDashboard.putNumber("Shooter Bottom Speed", 0);
        SmartDashboard.putBoolean("Shooter Deflector Deployed", false);

        testingState = new ShooterState(0,0,MechanismState.stored);
    }

    @Override
    public void periodic() {

//        topController.setP(SmartDashboard.getNumber("Shooter P", 0));
//        topController.setI(SmartDashboard.getNumber("Shooter I", 0));
//        topController.setD(SmartDashboard.getNumber("Shooter D", 0));

        if (RobotState.isEnabled())
        {
            double topVoltage = 0;
            double bottomVoltage = 0;

            switch (shooterMode)
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
                    ShooterState state;
//                    state = ShooterUtil.calculateTargetSpeeds(DrivetrainSubsystem.getInstance().getPose());

                    double topSpeed = SmartDashboard.getNumber("Shooter Top Speed",0);
                    double bottomSpeed = SmartDashboard.getNumber("Shooter Bottom Speed",0);
                    boolean deflectorState = SmartDashboard.putBoolean("Shooter Deflector Deployed", false);

                    if (deflectorState)
                    {
                        state = new ShooterState(topSpeed, bottomSpeed, MechanismState.deployed);
                    }else{
                        state = new ShooterState(topSpeed, bottomSpeed, MechanismState.stored);
                    }

                    topTargetRPM = MathUtil.clamp(
                            state.bottomSpeed(),
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

                    setDeflectorState(state.deflectorState());

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

//            shooter.setBarVoltage(
//                    MathUtil.clamp(barController.calculate(shooter.getBarPosition(), getBarSetpoint()),-12,12)
//            );

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

        Logger.recordOutput("Shooter/Bar/Setpoint", getBarSetpoint());
    }
    public MechanismState getDeflectorState() {
        return deflectorState;
    }

    public void setDeflectorState(MechanismState deflectorState) {
        this.deflectorState = deflectorState;
        shooter.setDeflector(this.deflectorState);
    }

    public void setShooterMode(ShooterMode shooterMode) {
        this.shooterMode = shooterMode;
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

    public double getBarSetpoint() {
        return barSetpoint;
    }

    public void setBarSetpoint(double barSetpoint) {
        this.barSetpoint = barSetpoint;
    }
}


