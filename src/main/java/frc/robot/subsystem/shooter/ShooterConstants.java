package frc.robot.subsystem.shooter;

import com.pathplanner.lib.util.PIDConstants;

public class ShooterConstants {
    public static final int TOP_MOTOR_ID = 11;
    public static final int BOTTOM_MOTOR_ID = 10;

    public static final PIDConstants TOP_CONTROLLER_REAL = new PIDConstants(.003,0,0);
    public static final PIDConstants BOTTOM_CONTROLLER_REAL = new PIDConstants(.003,.005,0);

    public static final PIDConstants TOP_CONTROLLER_SIM = new PIDConstants(0,0,0);
    public static final PIDConstants BOTTOM_CONTROLLER_SIM = new PIDConstants(0,0,0);

    public static final double SOURCE_INTAKE_SPEED = -.5;
    public final static double TOP_MOTOR_MAX_RPM = 5676;
    public final static double BOTTOM_MOTOR_MAX_RPM = 5676;

    public final static double PID_CONTROLLER_TOLERANCE = 25;
}
