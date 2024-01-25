package frc.lib.navx;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SerialPort;

public class NavxReal implements Navx{
	private final AHRS navx;
	private double offset = 0;
	public NavxReal()
	{
		navx = new AHRS(SerialPort.Port.kMXP);
	}
	@Override
	public Rotation2d getRotation2d() {
		return new Rotation2d(MathUtil.angleModulus((Math.toRadians(navx.getYaw() + 180) + offset)));
	}

	@Override
	public void zeroYaw() {
		navx.zeroYaw();
	}
	@Override
	public void setOffset(double offset) {
		this.offset = offset;
	}
}
