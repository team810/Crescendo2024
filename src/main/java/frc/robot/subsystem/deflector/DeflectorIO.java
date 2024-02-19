package frc.robot.subsystem.deflector;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public interface DeflectorIO {
    public void setState(DoubleSolenoid.Value value);
    public void update();
}
