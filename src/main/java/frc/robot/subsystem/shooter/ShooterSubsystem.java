package frc.robot.subsystem.shooter;


import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.LinearInterpolation;
import frc.robot.Robot;

import java.util.ArrayList;

public class ShooterSubsystem extends SubsystemBase {

    private final ShooterIO shooter;

    private double topTargetRPM;
    private double bottomTargetRPM;

    private final LinearInterpolation topLinearInterpolation;
    private final LinearInterpolation bottomLinearInterpolation;

    private final ArrayList<LinearInterpolation.Point> topData = new ArrayList<>();
    private final ArrayList<LinearInterpolation.Point> bottomData = new ArrayList<>();

    private final PIDController topController;
    private final PIDController bottomController;

    private ShooterSubsystem() {

        topController = new PIDController(0,0,0);
        bottomController = new PIDController(0,0,0);

        topLinearInterpolation = new LinearInterpolation(topData);
        bottomLinearInterpolation = new LinearInterpolation(bottomData);

        if (Robot.isReal())
        {
            shooter = new ShooterNeo();
        }else{
            shooter = new ShooterSim();
        }

    }

    @Override
    public void periodic() {
        shooter.update();
    }

    private final static ShooterSubsystem INSTANCE = new ShooterSubsystem();
    public static ShooterSubsystem getInstance() {
        return INSTANCE;
    }
}


