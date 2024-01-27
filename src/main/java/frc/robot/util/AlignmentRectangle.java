package frc.robot.util;

import edu.wpi.first.math.geometry.Pose2d;

public class AlignmentRectangle {

    private double leftX, rightX, bottomY, topY;
    private String name;
    private AutoTurnMode type;

    public AlignmentRectangle(String name, AutoTurnMode type,
                              double leftX, double rightX, double bottomY, double topY) {
        this.name = name;
        this.type = type;
        this.leftX = leftX;
        this.rightX = rightX;
        this.bottomY = bottomY;
        this.topY = topY;
    }

    public boolean inRectangle(Pose2d currentPos) {

        double x = currentPos.getX();
        double y = currentPos.getY();

        return ((x >= leftX) && (x <= rightX) && (y > bottomY) && (y < topY));
    }

    public String getName() {
        return this.name;
    }

    public AutoTurnMode getType() {
        return this.type;
    }
}
