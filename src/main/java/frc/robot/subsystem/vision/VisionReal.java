package frc.robot.subsystem.vision;

import org.photonvision.PhotonCamera;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonPipelineResult;

public class VisionReal implements VisionIO {

    private final PhotonCamera limelight;

    public VisionReal() {
        limelight = new PhotonCamera("cam");
        
        limelight.setLED(VisionLEDMode.kOff);

    }

    @Override
    public void update() {

    }

    @Override
    public PhotonPipelineResult getPipelineResults() {
        return limelight.getLatestResult();
    }
}
