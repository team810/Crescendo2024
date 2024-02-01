package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.Deadband;
import frc.robot.IO.Controls;
import frc.robot.IO.IO;
import frc.robot.subsystem.drivetrain.DrivetrainConstants;
import frc.robot.subsystem.drivetrain.DrivetrainMode;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.drivetrain.SpeedMode;
import frc.robot.util.AutoTurnConstants;
import frc.robot.util.AutoTurnMode;
import frc.robot.util.AutoTurnUtil;
import frc.robot.util.RectangleSet;
import org.littletonrobotics.junction.Logger;

/**
 * The drive train command is meant to handle drivetrain inputs in telop
 */
public class DriveCommand extends Command {

	private final Deadband xDeadband = new Deadband(.1);
	private final Deadband yDeadband = new Deadband(.1);
	private final Deadband thetaDeadband = new Deadband(.1);

	/**
	 * The Slew Rate Limiter is applied when the input is between -1 and 1.
	 * If the slew rate is 1 that means that it will take 1 second to get to 1 if it was 2 this would mean it would take 1 second to get to 2
	 */
	private final SlewRateLimiter xSlewRate = new SlewRateLimiter(2,-2,0);
	private final SlewRateLimiter ySlewRate = new SlewRateLimiter(2,-2,0);
	private final SlewRateLimiter thetaSlewRate = new SlewRateLimiter(1,-1,0);


	public DriveCommand() {
		addRequirements(DrivetrainSubsystem.getInstance());
	}

	@Override
	public void execute() {

		// Joystick input

		double x = 0;
		double y = 0;
		double theta = 0;

		double currentAngle = 0;
		double setpointAngle = 0;

		x = IO.getJoystickValue(Controls.drive_x).get();
		y = IO.getJoystickValue(Controls.drive_y).get();

		if ((!(IO.getButtonValue(Controls.rotateToTarget).get())) ||
				(DrivetrainSubsystem.getInstance().getCurrentRectangle().getType()
						== AutoTurnMode.noRectangle))
		{

//			System.out.println("NOT ALIGNING");
			theta = IO.getJoystickValue(Controls.drive_theta).get();
			theta = thetaDeadband.apply(theta);
			theta = Math.pow(theta, 3);

			if (DrivetrainSubsystem.getInstance().getSpeedMode() == SpeedMode.normal) {
				theta = theta * DrivetrainConstants.NORMAL_SPEED;
				theta = MathUtil.clamp(theta, -DrivetrainConstants.NORMAL_SPEED, DrivetrainConstants.NORMAL_SPEED);
			}
			else if (DrivetrainSubsystem.getInstance().getSpeedMode() == SpeedMode.slow) {
				theta = theta * DrivetrainConstants.SLOW_SPEED;
				theta = MathUtil.clamp(theta, -DrivetrainConstants.SLOW_SPEED, DrivetrainConstants.SLOW_SPEED);
			}
		} else {

//			System.out.println("ALIGNING!!");
			currentAngle = DrivetrainSubsystem.getInstance().getRotation().getRadians();
			setpointAngle = AutoTurnUtil.calculateTargetAngle(DrivetrainSubsystem.getInstance().getPose())
								.getRadians();

			Logger.recordOutput("currentAngle", currentAngle);
			Logger.recordOutput("setPointAngle", setpointAngle);

			theta = DrivetrainSubsystem.getInstance().getThetaController()
					.calculate(currentAngle, setpointAngle);

			Logger.recordOutput("PIDtheta", theta);

//			theta = theta / Math.PI;
			theta = theta * DrivetrainConstants.AUTO_ROTATE_MAX_SPEED;
			theta = MathUtil.clamp(theta, -DrivetrainConstants.AUTO_ROTATE_MAX_SPEED, DrivetrainConstants.AUTO_ROTATE_MAX_SPEED);
		}

		x = xDeadband.apply(x);
		y = yDeadband.apply(y);
		Logger.recordOutput("RawY", y);


		x = Math.pow(x, 3);
		y = Math.pow(y, 3);
		Logger.recordOutput("LimitY", y);


		if (DrivetrainSubsystem.getInstance().getSpeedMode() == SpeedMode.normal) {
			x = x * DrivetrainConstants.NORMAL_SPEED;
			y = y * DrivetrainConstants.NORMAL_SPEED;

			x = MathUtil.clamp(x, -DrivetrainConstants.NORMAL_SPEED, DrivetrainConstants.NORMAL_SPEED);
			y = MathUtil.clamp(y, -DrivetrainConstants.NORMAL_SPEED, DrivetrainConstants.NORMAL_SPEED);
		}
		if (DrivetrainSubsystem.getInstance().getSpeedMode() == SpeedMode.slow) {
			x = x * DrivetrainConstants.SLOW_SPEED;
			y = y * DrivetrainConstants.SLOW_SPEED;

			x = MathUtil.clamp(x, -DrivetrainConstants.SLOW_SPEED, DrivetrainConstants.SLOW_SPEED);
			y = MathUtil.clamp(y, -DrivetrainConstants.SLOW_SPEED, DrivetrainConstants.SLOW_SPEED);
		}


		Logger.recordOutput("Drivetrain/finOutTheta", theta);


		DrivetrainSubsystem.getInstance().setTargetTeleopSpeeds(
				x,
				y,
				-theta
		);


		if (IO.getButtonValue(Controls.reset_gyro).get())
		{
			DrivetrainSubsystem.getInstance().zeroGyro();
		}

		if (IO.getButtonValue(Controls.slowMode).get())
		{
			DrivetrainSubsystem.getInstance().setSpeedMode(SpeedMode.slow);
		}

		if (IO.getButtonValue(Controls.normalMode).get())
		{
			DrivetrainSubsystem.getInstance().setSpeedMode(SpeedMode.normal);
		}
	}
}
