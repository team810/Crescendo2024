package frc.robot.subsystem.shooter;

public enum BarState {

    deployed, // This is for when the bar is already at the setpoint
    stored,
    hold,

    // These two states are for when the bar is in motion
    revs,
    fwd


}
