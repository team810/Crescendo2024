package frc.robot.subsystem.laser;


import au.grapplerobotics.LaserCan;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class LaserSubsystem extends SubsystemBase {
    private static LaserSubsystem INSTANCE;

    private final LaserCan sensor;
    double distance; // Distance Measurement form sensor in mm

    private LaserState state;

    private LaserSubsystem() {
        sensor = new LaserCan(LaserConstants.ID);


        distance = sensor.getMeasurement().distance_mm;
        if (MathUtil.isNear(LaserConstants.EXPECTED_DISTANCE, distance, LaserConstants.TOLERANCE))
        {
            state = LaserState.Detected;
        }else{
            state = LaserState.Undetected;
        }
    }
    public LaserState getLaserState()
    {
        return state;
    }

    @Override
    public void periodic() {
        distance = sensor.getMeasurement().distance_mm;
        if (MathUtil.isNear(LaserConstants.EXPECTED_DISTANCE, distance, LaserConstants.TOLERANCE))
        {
            state = LaserState.Detected;
        }else{
            state = LaserState.Undetected;
        }

        Logger.recordOutput("LaserSensor/Distance", distance);
        Logger.recordOutput("LaserSensor/State", state);
    }

    public static LaserSubsystem getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new LaserSubsystem();
        }
        return INSTANCE;
    }
}

