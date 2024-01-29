package frc.robot.subsystem.shooter;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import frc.robot.Robot;

public class ShooterSim implements ShooterIO{

    private final FlywheelSim topMotor;
    private final FlywheelSim bottomMotor;

    public ShooterSim()
    {
        topMotor = new FlywheelSim(DCMotor.getNEO(1), 1, 0.1);
        bottomMotor = new FlywheelSim(DCMotor.getNEO(1), 1, 0.1);
    }

    @Override
    public void update() {
        topMotor.update(Robot.defaultPeriodSecs);
        bottomMotor.update(Robot.defaultPeriodSecs);
    }

    @Override
    public void setTopVoltage(double voltage) {

    }

    @Override
    public void setBottomVoltage(double voltage) {

    }

    @Override
    public double getTopRPM() {
        return 0;
    }

    @Override
    public double getBottomRPM() {
        return 0;
    }
}
