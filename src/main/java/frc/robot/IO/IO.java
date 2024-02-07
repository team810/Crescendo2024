package frc.robot.IO;

import edu.wpi.first.wpilibj.XboxController;

import java.util.HashMap;
import java.util.function.Supplier;

public abstract class IO {
    private static final XboxController primary = new XboxController(0);
    private static final XboxController secondary = new XboxController(1);

    private static final HashMap<Controls,Supplier<Double>> controlsJoystick = new HashMap<>();
    private static final HashMap<Controls,Supplier<Boolean>> controlsButtons = new HashMap<>();

    public static void Initialize()
    {
        controlsJoystick.put(Controls.drive_x, primary::getLeftX);
        controlsJoystick.put(Controls.drive_y, primary::getLeftY);
        controlsJoystick.put(Controls.drive_theta, primary::getRightX);
        controlsJoystick.put(Controls.manualIntake, secondary::getRightY);


        controlsButtons.put(Controls.reset_gyro, primary::getLeftBumper);
        controlsButtons.put(Controls.slowMode, primary::getRightBumper);
        controlsButtons.put(Controls.normalMode, () -> (.75 < primary.getRightTriggerAxis()));
        controlsButtons.put(Controls.rotateToTarget, primary::getAButton);

        controlsButtons.put(Controls.intakeFwd, secondary::getAButton);

        controlsButtons.put(Controls.revShooter, () -> (secondary.getLeftTriggerAxis() > 0.75));
        controlsButtons.put(Controls.fire, () -> (secondary.getRightTriggerAxis() > 0.75));

        controlsButtons.put(Controls.climberDown, () -> (secondary.getPOV() == 180));
        controlsButtons.put(Controls.climberUp, () -> (secondary.getPOV() == 0 && primary.getPOV() == 0));
        controlsButtons.put(Controls.climberAdjUp, () -> (secondary.getPOV() == 90 && primary.getPOV() == 90));
        controlsButtons.put(Controls.climberAdjDown, () -> (secondary.getPOV() == 270 & primary.getPOV() == 270));
        controlsButtons.put(Controls.releaseClimber, secondary::getBButton);
    }

    public static Supplier<Double> getJoystickValue(Controls control)
    {
        return controlsJoystick.get(control);
    }

    public static Supplier<Boolean> getButtonValue(Controls control)
    {
        return controlsButtons.get(control);
    }

    public static XboxController getPrimary() {
        return primary;
    }

    public static XboxController getSecondary() {
        return secondary;
    }
}
