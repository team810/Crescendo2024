package frc.robot.util.AutoTurn;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.util.AutoTurn.AlignmentRectangle;

public class RectangleSet {

    private AlignmentRectangle[] rectangles;

    public RectangleSet(AlignmentRectangle[] rectangles) {
        this.rectangles = rectangles;
    }

    public boolean inRectangle(Pose2d robotPose) {
        for (AlignmentRectangle rectangle : rectangles) {
            if (rectangle.inRectangle(robotPose)) {
                return true;
            }
        }
        return false;
    }

    public AlignmentRectangle findRectangle(Pose2d robotPose) {
        for (AlignmentRectangle rectangle : rectangles) {
            if (rectangle.inRectangle(robotPose)) {
                return rectangle;
            }
        }
        return rectangles[0];
    }

}