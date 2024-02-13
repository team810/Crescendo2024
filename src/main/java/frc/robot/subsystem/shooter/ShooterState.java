package frc.robot.subsystem.shooter;

import frc.lib.MechanismState;

public record ShooterState(double topSpeed, double bottomSpeed, MechanismState deflectorState) {

}
