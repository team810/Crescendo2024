package frc.robot.subsystem.vision;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;

public class VisionConstants {

    public static final double cameraX = -0.0254 * 4.3135;
    public static final double cameraY = - 0.0254 * 3.75;
    public static final double cameraZ = 0.0254 * 57.25;
    public static final Translation3d cameraPos = new Translation3d(cameraX, cameraY, cameraZ);

    public static final double cameraRoll = 0;
    public static final double cameraPitch = -0.0174533 * 23;
    public static final double cameraYaw = Math.PI;
    public static final Rotation3d cameraRot = new Rotation3d(cameraRoll, cameraPitch, cameraYaw);

    public static final Transform3d robotToCam = new Transform3d(cameraPos, cameraRot);

}
