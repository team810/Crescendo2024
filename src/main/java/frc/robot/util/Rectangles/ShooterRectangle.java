package frc.robot.util.Rectangles;

import frc.robot.util.Shooting.ShootingZone;

public class ShooterRectangle extends Rectangle {

    private final String name;

    private final ShootingZone zone;

    public ShooterRectangle(String name, ShootingZone zone,
                            double leftX, double rightX, double bottomY, double topY) {

        super(leftX, rightX, bottomY, topY);
        this.name = name;
        this.zone = zone;
    }

    public String getName() {
        return name;
    }

    public ShootingZone getZone() {
        return zone;
    }
}
