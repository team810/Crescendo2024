package frc.robot.subsystem.tbone;

public interface TBoneIO {
    double getEncoderPosition();

    void setVoltage(double voltage);

    void update();
}
