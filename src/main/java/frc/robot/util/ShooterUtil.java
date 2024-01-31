package frc.robot.util;

import frc.lib.LinearInterpolation;

import java.util.ArrayList;

public class ShooterUtil {
    private final LinearInterpolation topLinearInterpolation;
    private final LinearInterpolation bottomLinearInterpolation;

    private final ArrayList<LinearInterpolation.Point> topPoints = new ArrayList<>();
    private final ArrayList<LinearInterpolation.Point> bottomPoints = new ArrayList<>();


    private ShooterUtil()
    {
        topLinearInterpolation = new LinearInterpolation(topPoints);
        bottomLinearInterpolation = new LinearInterpolation(bottomPoints);
    }


    public static ShooterUtil getInstance() {
        if (Instance == null)
        {
            Instance = new ShooterUtil();
        }
        return Instance;
    }

    private static ShooterUtil Instance;
}
