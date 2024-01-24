package frc.robot.subsystem.shooter;

public class ShooterState {
    private double topRPM;
    private double bottomRPM;

    public ShooterState(double topRPM, double bottomRPM)
    {
        this.topRPM = topRPM;
        this.bottomRPM = bottomRPM;
    }

    public double getTopRPM() {
        return topRPM;
    }

    public void setTopRPM(double topRPM) {
        this.topRPM = topRPM;
    }

    public double getBottomRPM() {
        return bottomRPM;
    }

    public void setBottomRPM(double bottomRPM) {
        this.bottomRPM = bottomRPM;
    }
}
