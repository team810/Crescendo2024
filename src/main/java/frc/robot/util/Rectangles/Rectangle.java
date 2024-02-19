package frc.robot.util.Rectangles;

import edu.wpi.first.math.geometry.Pose2d;

public class Rectangle {

    private final double leftX;
    private final double rightX;
    private final double bottomY;
    private final double topY;

    public Rectangle(double leftX, double rightX, double bottomY, double topY) {
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
}
