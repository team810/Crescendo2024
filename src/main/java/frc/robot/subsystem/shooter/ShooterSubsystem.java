package frc.robot.subsystem.shooter;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {

    private ShooterSubsystem() {

    }
    private final static ShooterSubsystem INSTANCE = new ShooterSubsystem();
    public static ShooterSubsystem getInstance() {
        return INSTANCE;
    }
}


