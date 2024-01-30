package frc.robot.util;

public class AutoTurnConstants {

    public static final AlignmentRectangle blueAmp =
            new AlignmentRectangle("blueAmp", AutoTurnMode.Amp,
                    0, 5, 7.40, 8.20);
    public static final AlignmentRectangle blueSpeaker =
            new AlignmentRectangle("blueSpeaker", AutoTurnMode.blueSpeaker,
                    0, 3.20, 2.0, 7.40);
    public static final AlignmentRectangle redSource =
            new AlignmentRectangle("redSource", AutoTurnMode.redSource,
                    0, 3.20, 0, 2.0);

    public static final AlignmentRectangle redAmp =
            new AlignmentRectangle("redAmp", AutoTurnMode.Amp,
                    11.50, 16.50, 7.40, 8.20);
    public static final AlignmentRectangle redSpeaker =
            new AlignmentRectangle("redSpeaker", AutoTurnMode.redSpeaker,
                    11.80, 16.50, 2.0, 7.40);
    public static final AlignmentRectangle blueSource =
            new AlignmentRectangle("blueSource", AutoTurnMode.blueSource,
                    11.80, 16.50, 0, 2.0);

    public static final AlignmentRectangle nullRectangle =
            new AlignmentRectangle("no rectangle", AutoTurnMode.noRectangle,
                    0, 0, 0, 0);

    public static final AlignmentRectangle[] rectangles =
            {nullRectangle,
            blueAmp, blueSpeaker, blueSource,
            redAmp, redSpeaker, redSource};

    public static final RectangleSet rectangleSet = new RectangleSet(rectangles);
}
