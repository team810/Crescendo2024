package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.navx.Navx;
import frc.lib.navx.NavxReal;
import frc.robot.IO.Controls;
import frc.robot.IO.IO;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.vision.VisionSubsystem;
import org.littletonrobotics.junction.Logger;


public class AlignCommand extends Command {

    private final double[] AprilTagAngles = new double[]
            {120, 120, 180, 180, 270, 270, 0, 0, 60, 60,
             300, 60, 180, 0, 120, 240};

    private double angleDiff;

    private Navx navx;
    public AlignCommand() {

        addRequirements(VisionSubsystem.getInstance(),
        DrivetrainSubsystem.getInstance());

        angleDiff = 0;
    }

    @Override
    public void execute() {

        if (IO.getButtonValue(Controls.autoAlignment).get()
            && VisionSubsystem.getInstance().targetData[3] != -1
            && VisionSubsystem.getInstance().getAngle() != 0)
        {

            angleDiff = AprilTagAngles[(int) VisionSubsystem.getInstance().targetData[3] - 1]
                    - VisionSubsystem.getInstance().getAngle();

//            angleDiff = AprilTagAngles[(int) VisionSubsystem.getInstance().targetData[3] - 1]
//                    - DrivetrainSubsystem.getInstance().getRotation().getDegrees();


            if (angleDiff <= -180) {
                angleDiff += 180;
            } else if (angleDiff >= 180) {
                angleDiff -= 180;
            }

            Logger.recordOutput("Vision/angleDiff", angleDiff);

            DrivetrainSubsystem.getInstance()
                    .setTargetTeleopSpeeds(
                            0, 0,
                            Math.toRadians(angleDiff) / 2);

        }
    }

}
