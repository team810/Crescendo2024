package frc.robot.util.Rectangles;

import edu.wpi.first.math.geometry.Pose2d;

public class RectangleSet {

    private final Rectangle[] rectangles;

    public RectangleSet(Rectangle[] rectangles) {
        this.rectangles = rectangles;
    }

    public boolean inRectangle(Pose2d robotPose) {
        for (Rectangle rectangle : rectangles) {
            if (rectangle.inRectangle(robotPose)) {
                return true;
            }
        }
        return false;
    }

    public Rectangle findRectangle(Pose2d robotPose) {
        for (Rectangle rectangle : rectangles) {
            if (rectangle.inRectangle(robotPose)) {
                return rectangle;
            }
        }
        return rectangles[0];
    }

}
