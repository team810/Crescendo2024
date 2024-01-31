package frc.robot.IO;

import frc.lib.StadiaController;

import java.util.HashMap;
import java.util.function.Supplier;

public abstract class IO {
    private static final StadiaController primary = new StadiaController(0);
    private static final StadiaController secondary = new StadiaController(1);

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
        controlsButtons.put(Controls.normalMode, () -> (.75 > primary.getRightTriggerAxis()));
        controlsButtons.put(Controls.rotateToTarget, primary::getAButton);

        controlsButtons.put(Controls.intakeFwd, secondary::getAButton);
        controlsButtons.put(Controls.intakeRev, secondary::getYButton);

        controlsButtons.put(Controls.climberDown, () -> (secondary.getPOV() == 180));
        controlsButtons.put(Controls.climberUp, () -> (secondary.getPOV() == 0));
        controlsButtons.put(Controls.climberAdjUp, () -> (secondary.getPOV() == 90));
        controlsButtons.put(Controls.climberAdjDown, () -> (secondary.getPOV() == 270));
    }

    public static Supplier<Double> getJoystickValue(Controls control)
    {
        return controlsJoystick.get(control);
    }

    public static Supplier<Boolean> getButtonValue(Controls control)
    {
        return controlsButtons.get(control);
    }
}
