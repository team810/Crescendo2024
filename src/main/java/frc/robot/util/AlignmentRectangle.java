package frc.robot.util;

import edu.wpi.first.math.geometry.Pose2d;

public class AlignmentRectangle {

    private double leftX, rightX, bottomY, topY;

    public AlignmentRectangle(double leftX, double rightX, double bottomY, double topY) {
        this.leftX = leftX;
        this.rightX = rightX;
        this.bottomY = bottomY;
        this.topY = topY;
    }

    private boolean inRectangle(Pose2d currentPos) {

        double x = currentPos.getX();
        double y = currentPos.getY();

        return ((x >= leftX) && (x <= rightX) && (y > bottomY) && (y < topY));
    }

}
