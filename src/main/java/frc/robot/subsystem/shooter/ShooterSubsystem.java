package frc.robot.subsystem.shooter;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import org.littletonrobotics.junction.Logger;

public class ShooterSubsystem extends SubsystemBase {
    private static ShooterSubsystem INSTANCE;

    private final ShooterIO shooter;

    private final PIDController topController;
    private final PIDController bottomController;

    private double topTargetSpeed;
    private double bottomTargetSpeed;

    private ShooterMode shooterMode;

    private double topVoltage;
    private double bottomVoltage;

    private ShooterSubsystem()
    {
        topController = new PIDController(0,0,0);
        bottomController = new PIDController(0,0,0);

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

            topController.setP(ShooterConstants.BOTTOM_CONTROLLER_SIM.kP);
            topController.setI(ShooterConstants.BOTTOM_CONTROLLER_SIM.kI);
            topController.setD(ShooterConstants.BOTTOM_CONTROLLER_SIM.kD);

            bottomController.setP(ShooterConstants.BOTTOM_CONTROLLER_SIM.kP);
            bottomController.setI(ShooterConstants.BOTTOM_CONTROLLER_SIM.kI);
            bottomController.setD(ShooterConstants.BOTTOM_CONTROLLER_SIM.kD);
        }

        topController.setTolerance(ShooterConstants.PID_CONTROLLER_TOLERANCE);
        bottomController.setTolerance(ShooterConstants.PID_CONTROLLER_TOLERANCE);

        topTargetSpeed = 0;
        bottomTargetSpeed = 0;

        topVoltage = 0;
        bottomVoltage = 0;

        shooterMode = ShooterMode.off;
    }
    @Override
    public void periodic() {
        if (RobotState.isEnabled())
        {
            switch (shooterMode)
            {
                case SourceIntake -> {
                    topVoltage = - ShooterConstants.SOURCE_INTAKE_SPEED * 12;
                    bottomVoltage = - ShooterConstants.SOURCE_INTAKE_SPEED * 12;
                }
                case Amp -> {
                    topTargetSpeed = 1000;
                    bottomTargetSpeed = 1000;
                }
                case Speaker -> {
                    topTargetSpeed = 0 ;
                    bottomTargetSpeed = 0;
                }
                case test -> {
                    topTargetSpeed = 2000;
                    bottomTargetSpeed = 2000;
                }
                case off -> {
                    topVoltage = 0;
                    bottomVoltage = 0;
                }
            }

            if (shooterMode == ShooterMode.test || shooterMode == ShooterMode.Speaker || shooterMode == ShooterMode.Amp)
            {
                topVoltage = topController.calculate(shooter.getTopRPM(), topTargetSpeed);
                bottomVoltage = bottomController.calculate(shooter.getBottomRPM(), bottomTargetSpeed);

                topVoltage = MathUtil.clamp(topVoltage, -12, 12);
                bottomVoltage = MathUtil.clamp(bottomVoltage, -12, 12);
            }

            shooter.setTopVoltage(topVoltage);
            shooter.setBottomVoltage(bottomVoltage);
        }else{
            topVoltage = 0;
            bottomVoltage = 0;

            shooter.setTopVoltage(topVoltage);
            shooter.setBottomVoltage(bottomVoltage);

        }
        Logger.recordOutput("Shooter/Top/TargetSpeed", topTargetSpeed);
        Logger.recordOutput("Shooter/Bottom/TargetSpeed", bottomTargetSpeed);
        Logger.recordOutput("Shooter/Mode/ShooterMode", shooterMode);

        shooter.update();
    }

    public void setShooterMode(ShooterMode shooterMode) {
        this.shooterMode = shooterMode;
    }


    public static ShooterSubsystem getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ShooterSubsystem();
        }
        return INSTANCE;
    }
}


