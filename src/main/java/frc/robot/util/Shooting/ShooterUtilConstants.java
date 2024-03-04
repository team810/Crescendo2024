package frc.robot.util.Shooting;

import frc.robot.util.Rectangles.RectangleSet;
import frc.robot.util.Rectangles.ShooterRectangle;

public class ShooterUtilConstants {


    //TODO ADJUST THESE ARE NEEDED WHEN TESTING

    public static final ShooterRectangle BLUE_SUB =
            new ShooterRectangle("BLUE SUBWOOFER", ShootingZone.subwoofer,
                    0, 2.00, 4.05, 7.00);

    public static final ShooterRectangle BLUE_TAPE =
            new ShooterRectangle("BLUE TAPE", ShootingZone.tape,
                    2.00, 3.20, 3.60, 7.40);

    public static final ShooterRectangle RED_SUB =
            new ShooterRectangle("RED SUBWOOFER", ShootingZone.subwoofer,
                    14.50, 16.50, 4.05, 7.00);

    public static final ShooterRectangle RED_TAPE =
            new ShooterRectangle("RED TAPE", ShootingZone.tape,
                    13.30,14.50, 3.60, 7.40);

//    public static final ShooterRectangle BLUE_TOP_SUB =
//            new ShooterRectangle("BLUE SUB TOP", ShootingZone.topSub,
//                    0, 0.85, 6.05, 7.00);
//
//    public static final ShooterRectangle BLUE_MID_SUB =
//            new ShooterRectangle("BLUE SUB MID", ShootingZone.midSub,
//                    0, 1.90, 5.00, 6.05);
//
//    public static final ShooterRectangle BLUE_BOT_SUB =
//            new ShooterRectangle("BLUE SUB BOT", ShootingZone.botSub,
//                    0, 0.85, 4.05, 5.00);
//
//    public static final ShooterRectangle BLUE_TOP_TAPE =
//            new ShooterRectangle("BLUE TAPE TOP", ShootingZone.topTape,
//                    2.50, 3.20, 6.35, 7.40);
//
//    public static final ShooterRectangle BLUE_MID_TAPE =
//            new ShooterRectangle("BLUE TAPE MID", ShootingZone.midTape,
//                    2.50, 3.20, 4.85, 6.35);
//
//    public static final ShooterRectangle BLUE_PODIUM =
//            new ShooterRectangle("BLUE PODIUM", ShootingZone.podium,
//                    2.50, 3.20, 3.60, 4.85);
//
//    public static final ShooterRectangle RED_TOP_SUB =
//            new ShooterRectangle("RED SUB TOP", ShootingZone.topSub,
//                    15.65, 16.50, 6.05, 7.00);
//
//    public static final ShooterRectangle RED_MID_SUB =
//            new ShooterRectangle("RED SUB MID", ShootingZone.midSub,
//                    14.60, 16.50, 5.00, 6.05);
//
//    public static final ShooterRectangle RED_BOT_SUB =
//            new ShooterRectangle("RED SUB BOT", ShootingZone.botSub,
//                    15.65, 16.50, 4.05, 5.00);
//
//    public static final ShooterRectangle RED_TOP_TAPE =
//            new ShooterRectangle("RED TAPE TOP", ShootingZone.topTape,
//                    13.30, 14.00, 6.35, 7.40);
//
//    public static final ShooterRectangle RED_MID_TAPE =
//            new ShooterRectangle("RED TAPE MID", ShootingZone.midTape,
//                    13.30, 14.00, 4.85, 6.35);
//
//    public static final ShooterRectangle RED_PODIUM =
//            new ShooterRectangle("RED PODIUM", ShootingZone.podium,
//                    13.30, 14.00, 3.60, 4.85);

    public static final ShooterRectangle NO_ZONE =
            new ShooterRectangle("NO SHOOTER ZONE", ShootingZone.none,
                    0, 0, 0, 0);

//    private static final ShooterRectangle[] RECTANGLES =
//            {NO_ZONE,
//            BLUE_TOP_SUB, BLUE_MID_SUB, BLUE_BOT_SUB,
//            BLUE_TOP_TAPE, BLUE_MID_TAPE, BLUE_PODIUM,
//            RED_TOP_SUB, RED_MID_SUB, RED_BOT_SUB,
//            RED_TOP_TAPE, RED_MID_TAPE, RED_PODIUM};

    private static final ShooterRectangle[] RECTANGLES =
            {NO_ZONE,
            BLUE_SUB, BLUE_TAPE,
            RED_SUB, RED_TAPE};

    public static final RectangleSet SHOOTING_ZONE_SET = new RectangleSet(RECTANGLES);


}
