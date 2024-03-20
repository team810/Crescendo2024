package frc.robot.subsystem.vision;

import org.photonvision.targeting.PhotonPipelineResult;

public interface VisionIO {

    void update();
    PhotonPipelineResult getPipelineResults();
}
