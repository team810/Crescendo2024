package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.Deadband;
import frc.robot.IO.Controls;
import frc.robot.IO.IO;
import frc.robot.subsystem.drivetrain.DrivetrainConstants;
import frc.robot.subsystem.drivetrain.DrivetrainMode;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.drivetrain.SpeedMode;
import frc.robot.util.AutoTurn.AutoTurnUtil;
import org.littletonrobotics.junction.Logger;

/**
 * The drive train command is meant to handle drivetrain inputs in telop
 */
public class DriveCommand extends Command {

	private final Deadband xDeadband = new Deadband(.1);
	private final Deadband yDeadband = new Deadband(.1);
	private final Deadband thetaDeadband = new Deadband(.1);


	public DriveCommand() {
		addRequirements(DrivetrainSubsystem.getInstance());
		IO.getPrimary().setRumble(GenericHID.RumbleType.kBothRumble, 0);
	}

	@Override
	public void execute() {

		if (RobotState.isTeleop())
		{
			DrivetrainSubsystem.getInstance().setMode(DrivetrainMode.teleop);
			double x = 0;
			double y = 0;
			double theta = 0;

			double currentAngle = 0;
			double setpointAngle = 0;

			boolean aligning = IO.getButtonValue(Controls.autoAlignAmp).get()
					|| IO.getButtonValue(Controls.autoAlignPodium).get()
					|| IO.getButtonValue(Controls.autoAlignSource).get();

			x = IO.getJoystickValue(Controls.drive_x).get();
			y = IO.getJoystickValue(Controls.drive_y).get();
			theta = theta;

			if (!aligning)
			{
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
				currentAngle = -MathUtil.angleModulus(DrivetrainSubsystem.getInstance().getRotation().getRadians());

				setpointAngle = 0;

				if (IO.getButtonValue(Controls.autoAlignAmp).get()) {
					setpointAngle = AutoTurnUtil.getAmpAngle().getRadians();
				} else if (IO.getButtonValue(Controls.autoAlignSource).get()) {
					setpointAngle = AutoTurnUtil.getSourceAngle().getRadians();
				} else if (IO.getButtonValue(Controls.autoAlignPodium).get()) {
					setpointAngle = AutoTurnUtil.getPodiumAngle().getRadians();
				}

				Logger.recordOutput("Theta/Target", currentAngle);
				Logger.recordOutput("Theta/Setpoint", setpointAngle);

				theta = DrivetrainSubsystem.getInstance().getThetaController()
						.calculate(currentAngle, setpointAngle);

				theta = theta * DrivetrainConstants.AUTO_ROTATE_MAX_SPEED;
				theta = MathUtil.clamp(theta, -DrivetrainConstants.AUTO_ROTATE_MAX_SPEED, DrivetrainConstants.AUTO_ROTATE_MAX_SPEED);
			}

			x = xDeadband.apply(x);
			y = yDeadband.apply(y);

			x = Math.pow(x, 3);
			y = Math.pow(y, 3);


			if (DrivetrainSubsystem.getInstance().getSpeedMode() == SpeedMode.normal) {
				x = x * DrivetrainConstants.NORMAL_SPEED;
				y = y * DrivetrainConstants.NORMAL_SPEED;

				x = MathUtil.clamp(x, -DrivetrainConstants.NORMAL_SPEED, DrivetrainConstants.NORMAL_SPEED);
				y = MathUtil.clamp(y, -DrivetrainConstants.NORMAL_SPEED, DrivetrainConstants.NORMAL_SPEED);

			} else if (DrivetrainSubsystem.getInstance().getSpeedMode() == SpeedMode.slow) {

				x = x * DrivetrainConstants.SLOW_SPEED;
				y = y * DrivetrainConstants.SLOW_SPEED;

				x = MathUtil.clamp(x, -DrivetrainConstants.SLOW_SPEED, DrivetrainConstants.SLOW_SPEED);
				y = MathUtil.clamp(y, -DrivetrainConstants.SLOW_SPEED, DrivetrainConstants.SLOW_SPEED);
			}

			DrivetrainSubsystem.getInstance().setTelopSpeeds(
					x,
					y,
					theta
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
		DrivetrainSubsystem.getInstance().drive();

	}
}
