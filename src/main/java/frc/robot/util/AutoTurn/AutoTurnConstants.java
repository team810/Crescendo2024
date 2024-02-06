package frc.robot.util.AutoTurn;

public class AutoTurnConstants {

    public static final AlignmentRectangle BLUE_AMP =
            new AlignmentRectangle("blueAmp", AutoTurnMode.amp,
                    0, 5, 7.40, 8.20);
    public static final AlignmentRectangle BLUE_SPEAKER =
            new AlignmentRectangle("blueSpeaker", AutoTurnMode.blueSpeaker,
                    0, 3.20, 2.0, 7.40);
    public static final AlignmentRectangle RED_SOURCE =
            new AlignmentRectangle("redSource", AutoTurnMode.redSource,
                    0, 3.20, 0, 2.0);

    public static final AlignmentRectangle RED_AMP =
            new AlignmentRectangle("redAmp", AutoTurnMode.amp,
                    11.50, 16.50, 7.40, 8.20);
    public static final AlignmentRectangle RED_SPEAKER =
            new AlignmentRectangle("redSpeaker", AutoTurnMode.redSpeaker,
                    11.80, 16.50, 2.0, 7.40);
    public static final AlignmentRectangle BLUE_SOURCE =
            new AlignmentRectangle("blueSource", AutoTurnMode.blueSource,
                    11.80, 16.50, 0, 2.0);

    public static final AlignmentRectangle NO_RECTANGLE =
            new AlignmentRectangle("no rectangle", AutoTurnMode.noRectangle,
                    0, 0, 0, 0);

    public static final AlignmentRectangle[] rectangles =
            {NO_RECTANGLE,
                    BLUE_AMP, BLUE_SPEAKER, BLUE_SOURCE,
                    RED_AMP, RED_SPEAKER, RED_SOURCE};

    public static final RectangleSet RECTANGLE_SET = new RectangleSet(rectangles);
}
