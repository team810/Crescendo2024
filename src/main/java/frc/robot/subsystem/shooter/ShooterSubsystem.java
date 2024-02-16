package frc.robot.subsystem.shooter;


import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import org.littletonrobotics.junction.Logger;

public class ShooterSubsystem extends SubsystemBase {
    private static ShooterSubsystem INSTANCE;

    private final ShooterIO shooter;

    private double topTargetSpeed;
    private double bottomTargetSpeed;

    private ShooterMode shooterMode;

    private double topVoltage;
    private double bottomVoltage;

    private ShooterSubsystem()
    {

        if (Robot.isReal())
        {
            shooter = new ShooterReal();

        }else{
            shooter = new ShooterSim();

        }

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
                    topTargetSpeed = -2000;
                    bottomTargetSpeed = -2000;
                }
                case Amp -> {
                    topTargetSpeed = 2000;
                    bottomTargetSpeed = 2000;
                }
                case Speaker -> {
                    topTargetSpeed = 1 ;
                    bottomTargetSpeed = 1;
                }
                case test -> {
                    topTargetSpeed = 2000;
                    bottomTargetSpeed = 2400;
                }
                case off -> {
                    topTargetSpeed = 0;
                    bottomTargetSpeed = 0;
                }
            }
        }else{
            topTargetSpeed = 0;
            bottomTargetSpeed =0;
        }

        shooter.setTopTargetRPM(topTargetSpeed);
        shooter.setBottomTargetRPM(bottomTargetSpeed);

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


