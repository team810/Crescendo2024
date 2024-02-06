package frc.robot.subsystem.intake;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

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

        state = IntakeStates.off;
        manualSpeed = 0;
    }

    public void periodic() {

        intake.update();

        switch (state)
        {
            case fwd -> {
                intake.setVoltage(IntakeConstants.INTAKE_MAX_SPEED * 12);
            }
            case rev -> {
                intake.setVoltage(-IntakeConstants.INTAKE_MAX_SPEED * 12);
            }
            case off -> {
                intake.setVoltage(0);
            }
            case shoot -> {
                intake.setVoltage(IntakeConstants.INTAKE_SHOOT_SPEED * 12);
            }
            case manual ->
            {
                intake.setVoltage(manualSpeed * 12);
            }
        }
    }

    public void setManualSpeed(double speed) {
        this.manualSpeed = speed;
    }

    public void setState(IntakeStates state) {
        this.state = state;
    }

    public boolean isLimitSwitchTriggered()
    {
        return intake.isLimitSwitchTriggered();
    }
}

