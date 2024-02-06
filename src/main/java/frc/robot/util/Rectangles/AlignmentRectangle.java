package frc.robot.util.Rectangles;

import frc.robot.util.AutoTurn.AutoTurnMode;
import frc.robot.util.Rectangles.Rectangle;

public class AlignmentRectangle extends Rectangle  {

    private double leftX, rightX, bottomY, topY;
    private String name;
    private AutoTurnMode type;

    public AlignmentRectangle(String name, AutoTurnMode type,
                              double leftX, double rightX, double bottomY, double topY) {
        super(leftX, rightX, bottomY, topY);
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public AutoTurnMode getType() {
        return this.type;
    }
}
