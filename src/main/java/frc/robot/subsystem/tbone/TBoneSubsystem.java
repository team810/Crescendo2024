package frc.robot.subsystem.tbone;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TBoneSubsystem extends SubsystemBase {
    private static TBoneSubsystem INSTANCE;
    public static TBoneSubsystem getInstance() {
        return INSTANCE;
    }


    private TBoneSubsystem() {

    }
}

