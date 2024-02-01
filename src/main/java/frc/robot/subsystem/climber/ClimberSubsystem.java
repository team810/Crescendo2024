package frc.robot.subsystem.climber;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.subsystem.intake.IntakeSubsystem;
import org.littletonrobotics.junction.Logger;

public class ClimberSubsystem extends SubsystemBase {

    private static ClimberSubsystem INSTANCE = new ClimberSubsystem();

    private ClimberIO climber;

    private ClimberStates state;

    public static ClimberSubsystem getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClimberSubsystem();
        }
        return INSTANCE;
    }

    private ClimberSubsystem() {

        if (Robot.isReal()) {
            climber = new ClimberReal();
        } else {
            climber = new ClimberSim();
        }

        state = ClimberStates.down;
    }

    public void periodic() {

        Logger.recordOutput("Climber/state", this.state);

        climber.update();

        switch (this.state) {

            case down:
                climber.setVoltage(ClimberConstants.CLIMBER_DOWN_SPEED);
                break;
            case up:
                climber.setVoltage(ClimberConstants.CLIMBER_UP_SPEED);
                break;
            case manualUp:
                climber.setVoltage(ClimberConstants.CLIMBER_ADJ_SPEED);
                break;
            case manualDown:
                climber.setVoltage(-ClimberConstants.CLIMBER_ADJ_SPEED);
                break;
            case stopped:
                climber.setVoltage(0);
                break;
            default:
                throw new RuntimeException("trigger default climber state");
        }
    }

    public void setState(ClimberStates state) {
        this.state = state;
    }
}

