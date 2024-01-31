package frc.robot.subsystem.shooter;

public class ShooterState {
    public double TopRPM;
    public double BottomRPM;
    public boolean Deflector;

    public ShooterState(double topRPM, double bottomRPM, boolean deflector) {
        TopRPM = topRPM;
        BottomRPM = bottomRPM;
        Deflector = deflector;
    }
}
