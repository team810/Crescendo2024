package frc.robot.subsystem.shooter;


import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.MechanismState;
import frc.robot.Robot;
import frc.robot.util.Shooting.ShooterState;
import org.littletonrobotics.junction.Logger;

public class ShooterSubsystem extends SubsystemBase {
    private static ShooterSubsystem INSTANCE;

    private final ShooterIO shooter;

    private double topTargetSpeed;
    private double bottomTargetSpeed;

    private ShooterMode shooterMode;

    private double targetTopTestRPM;
    private double targetBottomTestRPM;

    private ShooterState speakerState;

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

        shooterMode = ShooterMode.off;

//        SmartDashboard.putNumber("TopSpeedTest", 2000);
//        SmartDashboard.putNumber("BottomSpeedTest", 2000);

        setSpeakerState(new ShooterState(0,0, MechanismState.stored));
        targetTopTestRPM = 2000;
        targetBottomTestRPM = 2000;
    }

    @Override
    public void periodic() {

//        targetTopTestRPM = SmartDashboard.getNumber("TopSpeedTest", targetTopTestRPM);
//        targetBottomTestRPM = SmartDashboard.getNumber("BottomSpeedTest", targetBottomTestRPM);

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
                    topTargetSpeed = getSpeakerState().getTopRPM();
                    bottomTargetSpeed = getSpeakerState().getBottomRPM();
                }
                case test -> {
                    topTargetSpeed = targetTopTestRPM;
                    bottomTargetSpeed = targetBottomTestRPM;
                }
                case off -> {
                    topTargetSpeed = 0;
                    bottomTargetSpeed = 0;
                }
            }
        }else{
            topTargetSpeed = 0;
            bottomTargetSpeed = 0;
        }

        shooter.setTopTargetRPM(topTargetSpeed);
        shooter.setBottomTargetRPM(bottomTargetSpeed);

        Logger.recordOutput("Shooter/Top/TargetSpeedSub", topTargetSpeed);
        Logger.recordOutput("Shooter/Bottom/TargetSpeedSub", bottomTargetSpeed);
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

    public ShooterState getSpeakerState() {
        return speakerState;
    }

    public void setSpeakerState(ShooterState speakerState) {
        this.speakerState = speakerState;
    }
}


