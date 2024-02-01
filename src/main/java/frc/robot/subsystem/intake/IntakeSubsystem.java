package frc.robot.subsystem.intake;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import org.littletonrobotics.junction.Logger;

public class IntakeSubsystem extends SubsystemBase {

    private static IntakeSubsystem INSTANCE = new IntakeSubsystem();

    private IntakeIO intake;

    private IntakeStates state;

    private double manualSpeed;


    public static IntakeSubsystem getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IntakeSubsystem();
        }
        return INSTANCE;
    }

    private IntakeSubsystem() {

        if (Robot.isReal()) {
            intake = new IntakeReal();
        }else{
            intake = new IntakeSim();
        }

        state = IntakeStates.stopped;
        manualSpeed = 0;
    }

    public void periodic() {

        intake.update();

        switch (this.state) {

            case runForward:
                intake.setVoltage(IntakeConstants.INTAKE_MAX_SPEED);
                break;
            case runReverse:
                intake.setVoltage(-IntakeConstants.INTAKE_MAX_SPEED);
                break;
            case manualInput:
                intake.setVoltage(manualSpeed);
                break;
            case stopped:
                intake.setVoltage(0);
                break;
            default:
                throw new RuntimeException("triggered default intake state");
        }
    }

    public void setManualSpeed(double speed) {
        this.manualSpeed = speed * 12;
    }

    public void setState(IntakeStates state) {
        this.state = state;
    }

}

