/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6845.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	
	

	
	Joystick left, right;
	JoystickButton sucker;
	JoystickButton pusher;
	JoystickButton armUp;
	JoystickButton armDown;
	JoystickButton armClimb;
	JoystickButton pusherSlow;

	ADXRS450_Gyro gyro; 
//	CameraServer server;
	
	Spark m_left;
	Spark m_right;
	DifferentialDrive myDrive; 

	WPI_TalonSRX leftSucker;
	WPI_TalonSRX rightSucker;
	Spark arm;

	String gameData; 
	//int location;
	double goalAngle;
   
	   //Forward 
	long forwardTime; 
	
		//Side Switches 
	long alphaTime; 
	long bravoTime; 
	long charlieTime; 
	long deltaTime; 
	long echoTime; 
	long foxtrotTime; 
	
		//Center Switch
	long ITime; 
	long IITime; 
	long IIITime; 
	long IVTime; 
	long VTime; 
	long VITime; 
	
	   //Center Switch 2 cube auto
	long firstTime;   	   
	long secondTime; 
	long thirdTime;
	long fourthTime;
	long fifthTime;
	long sixthTime; 
	long seventhTime;
	long eighthTime;
	long ninthTime;
	long tenthTime;
	long eleventhTime;
	long twelthTime;
	long thirteenthTime;
	long fourteenthTime;
	long fifteenthTime;
	long sixteenthTime;
	long seventeenthTime;
	
	
		//DNE
	long unoTime; 
	long dosTime;
	long tresTime; 
	long cuatroTime; 
	long cincoTime; 
	
		//Left and Right Scale
	long aTime;
	long bTime;
	long cTime;
	long dTime; 
	long eTime; 
	
	
	
	//Auto Chooser
//	Command autonomousCommand; 
	SendableChooser<Integer> autoChooser;
	int autoMode;
	
//	public static OI oi; 
	//End Auto Chooser
	
	
	public void robotInit() {
	
		//Camera start
		CameraServer.getInstance().startAutomaticCapture();
		
		new Thread(() ->
		{
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			camera.setResolution(640, 480);
			CvSink cvSink = CameraServer.getInstance().getVideo();
			CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640,480);
			Mat source = new Mat();
			Mat output = new Mat();
			
				while(!Thread.interrupted())
				{
					cvSink.grabFrame(source);
					Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
					outputStream.putFrame(output);
				}
		}).start();
	
		//end Camera
		
		//Auto Chooser
		autoChooser = new SendableChooser<Integer>(); 
		autoChooser.addDefault("Forward Auto", 1);
		autoChooser.addObject("Switch Right", 2);
		autoChooser.addObject("Switch Left", 3);
		autoChooser.addObject("Switch Center", 4);
		//autoChooser.addObject("Switch Center Left", 5);
		autoChooser.addObject("Scale Right", 6);
		autoChooser.addObject("Scale Left", 7);
		autoChooser.addObject("Switch Two-Cube", 8);
		SmartDashboard.putData("Autonomous mode chooser", autoChooser);
		//End Auto Chooser
		
		
		m_left = new Spark(1);
		m_left.setInverted(true);
		m_right = new Spark(3);
		m_right.setInverted(true);
		myDrive = new DifferentialDrive(m_left, m_right);
		left = new Joystick(0);
		right = new Joystick(1);
		sucker = new JoystickButton(right,2);
		pusher = new JoystickButton(right,1);
		pusherSlow = new JoystickButton(right,3);
		armUp = new JoystickButton(right,6);
		armDown = new JoystickButton(right,4);
		armClimb = new JoystickButton(right,11);
		leftSucker = new WPI_TalonSRX(1);
		rightSucker = new WPI_TalonSRX(2);
		arm = new Spark(5);
		
		gyro = new ADXRS450_Gyro(); 
//		server = CameraServer.getInstance();
//		server.setQuality(50);
//		server.startAutomaticCapture();
	}
	
	public void autonomousInit() {
		//myDrive.setSafetyEnabled(false);
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		//location = DriverStation.getInstance().getLocation();
		
		//Auto Chooser
		autoMode = (int) autoChooser.getSelected();
		//End Auto Chooser
		
		//Forward Auto
		forwardTime = System.currentTimeMillis () + 4000;
		
		//Side Switches
		alphaTime = System.currentTimeMillis() + 3000; 
		bravoTime = System.currentTimeMillis() + 6000; 
		charlieTime = System.currentTimeMillis() + 8000; 
		deltaTime = System.currentTimeMillis() + 10000; 
		echoTime = System.currentTimeMillis() + 12000; 
		foxtrotTime = System.currentTimeMillis() + 14000; 
		
		//Center  Switch
		ITime = System.currentTimeMillis() + 1000; 
		IITime = System.currentTimeMillis() + 3000; 
		IIITime = System.currentTimeMillis() + 7000; 
		IVTime = System.currentTimeMillis() + 9000; 
		VTime = System.currentTimeMillis() + 110000; 
		VITime = System.currentTimeMillis() + 13000; 
		
		//Center Switch two cube auto
		firstTime = System.currentTimeMillis () + 1000;
		secondTime = System.currentTimeMillis() +2000; 
		thirdTime = System.currentTimeMillis() +3000; 
		fourthTime = System.currentTimeMillis() +4000;
		fifthTime = System.currentTimeMillis() +5000;
		sixthTime = System.currentTimeMillis() +6000; 
		seventhTime = System.currentTimeMillis() + 7000; 
		eighthTime = System.currentTimeMillis() + 8000; 
		ninthTime = System.currentTimeMillis() + 9000; 
		tenthTime = System.currentTimeMillis() + 10000; 
		eleventhTime = System.currentTimeMillis() + 11000; 
		twelthTime = System.currentTimeMillis() + 12000; 
		thirteenthTime = System.currentTimeMillis() + 13000; 
		fourteenthTime = System.currentTimeMillis() + 14000; 
		fifteenthTime = System.currentTimeMillis() + 15000; 
		sixteenthTime = System.currentTimeMillis() + 16000; 
		seventeenthTime = System.currentTimeMillis() + 17000; 

		
		//DNE
		unoTime = System.currentTimeMillis() + 1000; 
		dosTime = System.currentTimeMillis() +4000; 
		tresTime = System.currentTimeMillis() +8000; 
		cuatroTime = System.currentTimeMillis() +9000;
		cincoTime = System.currentTimeMillis() +11000;

		//Right and Left Scale
		aTime = System.currentTimeMillis() + 4000; 
		bTime = System.currentTimeMillis() +8000; 
		cTime = System.currentTimeMillis() +10000; 
		dTime = System.currentTimeMillis() +11000;
		eTime = System.currentTimeMillis() +13000;

	}
	
	
	public void autonomousPeriodic() {
		double leftSpeed = 0, rightSpeed = 0;
		boolean correctSide = false; 
		
		switch(autoMode) {
			case 1:
				//Forward Auto
				if(System.currentTimeMillis()>forwardTime) {
					rightSpeed = .60; 
					rightSpeed = leftSpeed; 
				}
				
				break;

			case 2:
				//Switch Right
				//if (location == 3) {
					if (gameData.charAt(0) == 'R') {
							correctSide=true;
							
					}
					if( alphaTime>System.currentTimeMillis()) {
						
						
						//raise arm
						//arm moves at 8.5 inches/second;
						if (correctSide) { 
							arm.set(0.6); 
						}
					}
				
					if( bravoTime>System.currentTimeMillis () && alphaTime<System.currentTimeMillis()) {	
					
						arm.set(0.0);
						leftSpeed = .60;
						rightSpeed = leftSpeed; 
					
					}
				
					if(charlieTime>System.currentTimeMillis() && bravoTime<System.currentTimeMillis()) {
						
						
						if (correctSide)	{
							rightSpeed = .30;
							leftSpeed = -.30;
						}
					}
						
					if(deltaTime>System.currentTimeMillis() && charlieTime<System.currentTimeMillis()) {
							
						if (correctSide) { 
							rightSpeed = .30;
							leftSpeed = .30;
						}
							
					}
						
					if(echoTime>System.currentTimeMillis() && deltaTime<System.currentTimeMillis()) {
							
						if (correctSide) {
								rightSpeed = 0;
								leftSpeed = 0;
					
								leftSucker.set(.60);
								rightSucker.set(-.60);
						}
			
					}
				//}
				
				break;
				
			case 3:
				//Switch Left
				//if (location == 1) {
					if(gameData.charAt(0) == 'L') {
						correctSide=true;
					}
					
					if( alphaTime>System.currentTimeMillis()) {
					
						if (correctSide) {
						//raise arm
						//arm moves at 8.5 inches/second;
							arm.set(0.6);
						}
					}
					
					if( bravoTime>System.currentTimeMillis () && alphaTime<System.currentTimeMillis()) {	
					
						arm.set(0.0);
						leftSpeed = .60;
						rightSpeed = leftSpeed; 
					
					}
				
					if(charlieTime>System.currentTimeMillis() && bravoTime<System.currentTimeMillis()) {
						
						if (correctSide) {
							rightSpeed = -.30;
							leftSpeed = .30;
						}
					}
						
					if(deltaTime>System.currentTimeMillis() && charlieTime<System.currentTimeMillis()) {
						
						if (correctSide) {
							rightSpeed = .30;
							leftSpeed = .30;
						}
							
					}
						
					if(echoTime>System.currentTimeMillis() && deltaTime<System.currentTimeMillis()) {
							
						if (correctSide) {
							leftSucker.set(.60);
							rightSucker.set(-.60);
						}
			
					}	
				//}
				
				break;
				
			case 4:
				//System.out.print("case 4-switch center");
				//Switch Center
				//true is right, false is left
				//if(location == 2) { 
					if (gameData.charAt(0) == 'R') {
						correctSide = true; 
						//System.out.print("case 4-switch center");
					}
					
					if ( ITime>System.currentTimeMillis()) {
						//Drive forward
						leftSpeed = .30;
						rightSpeed = .30; 
						
					}
					
					if(IITime>System.currentTimeMillis () && ITime<System.currentTimeMillis()) {
						
						
						//raise arm
						//arm moves at 8.5 inches/second;
						arm.set(0.6);
						
						if (correctSide) {

							leftSpeed = .30;
							rightSpeed = -.30;
						} else {
						
							leftSpeed = -.30;
							rightSpeed = .30;
						}
						
					}
						
					if(IIITime>System.currentTimeMillis() && IITime<System.currentTimeMillis())	{
								
						rightSpeed = .65;
						leftSpeed = rightSpeed - 0.0;
					}
						
					if(IVTime>System.currentTimeMillis() && IIITime<System.currentTimeMillis()) {
							
						arm.set(0.0);
						if(correctSide) {
							leftSpeed = -.30;
							rightSpeed = .30;
						}
						else {
							leftSpeed = .30;
							rightSpeed = -.30;
						}
					}
							
					if(VTime>System.currentTimeMillis() && IVTime<System.currentTimeMillis()) {
								
						leftSpeed = .60; 
						rightSpeed = .60; 
					}
								
					if(VITime>System.currentTimeMillis() && VTime<System.currentTimeMillis()) {
								
						rightSpeed = 0;
						leftSpeed = 0;
							
						leftSucker.set(.60);
						rightSucker.set(-.60);
								
					}
				//}
				
				break;
		
			/*
			  case 5:
			 
				//Switch Center Left
				if (location == 2 && gameData.charAt(0) == 'L') {
					
					if ( ITime>System.currentTimeMillis()) {
						//Drive forward
						leftSpeed = .30;
						rightSpeed = .30; 
						
					}
				
					if(IITime>System.currentTimeMillis () && ITime<System.currentTimeMillis()) {	
				
						//raise arm
						//arm moves at 8.5 inches/second;
						arm.set(0.6);
						leftSpeed = -.30;
						rightSpeed = .30;
				
					}
					
					if(IIITime>System.currentTimeMillis() && IITime<System.currentTimeMillis()) {
					
						rightSpeed = .65;
						leftSpeed = rightSpeed; 
					}
			
					if(IVTime>System.currentTimeMillis() && IIITime<System.currentTimeMillis()) {
					
						arm.set(0.0);
						leftSpeed = .30;
						rightSpeed = -.30;
					}
					
					if(VTime>System.currentTimeMillis() && IVTime<System.currentTimeMillis()) {
						
						leftSpeed = .60; 
						rightSpeed = .60; 
					}
						
					if(VITime>System.currentTimeMillis() && VTime<System.currentTimeMillis()) {
						
						rightSpeed = 0;
						leftSpeed = 0;
						
						leftSucker.set(.60);
						rightSucker.set(-.60);
						
					}
				}
				
				break;	*/
				
			case 6:
				//Scale Right
				//if(location == 3) {
					
					if ( gameData.charAt(1) == 'R') {
						correctSide = true;
					}
			
					if( aTime>System.currentTimeMillis()) {
				
						leftSpeed = .60;
						rightSpeed = leftSpeed; 
					}
			
					if( bTime>System.currentTimeMillis () && aTime>System.currentTimeMillis()) {
				
						leftSpeed = 0; 
						rightSpeed = leftSpeed; 
						
						if(correctSide) {
							arm.set(.60);
						}
				
					}
			
					if(cTime>System.currentTimeMillis() && bTime<System.currentTimeMillis()) {
						
						arm.set(0);
						
						if (correctSide) {
							rightSpeed = .30;
							leftSpeed = -.30;
						}
					}
					
					if(dTime>System.currentTimeMillis() && cTime<System.currentTimeMillis()) {
						
						if (correctSide) {
							rightSpeed = .30;
							leftSpeed = .30;
						}
						
					}
					
					if(eTime>System.currentTimeMillis() && dTime<System.currentTimeMillis()) {
				
						if (correctSide) {
							leftSucker.set(.60);
							rightSucker.set(-.60);
						}
					}	
				//}
				
				break;
			case 7:
				//Scale left
				//if(location == 1) {
					if (gameData.charAt(1) == 'L') {
						correctSide = true;
					}
			
					if( aTime>System.currentTimeMillis()) {
				
						leftSpeed = .60;
						rightSpeed = leftSpeed; 
					}
			
					if( bTime>System.currentTimeMillis () && aTime>System.currentTimeMillis()) {	
						
						if (correctSide) {
							arm.set(.60);
						}
					}
			
					if(cTime>System.currentTimeMillis() && bTime<System.currentTimeMillis()) {
				
						arm.set(0);
						if (correctSide) {
							rightSpeed = -.30;
							leftSpeed = .30;
						}
					}
					
					if(dTime>System.currentTimeMillis() && cTime<System.currentTimeMillis()) {
				
						if (correctSide) {
							rightSpeed = .30;
							leftSpeed = .30;
						}
						
					}
					
					if(eTime>System.currentTimeMillis() && dTime<System.currentTimeMillis()) {
				
						if (correctSide) {
						leftSucker.set(.60);
						rightSucker.set(-.60);
						}
					}	
				//}
				
				break;
				
			case 8:
				//Experimental 2 cube Auto
				
				if(gameData.charAt(0) == 'R') { 
					correctSide = true; 
					
					if( firstTime>System.currentTimeMillis()) {
					
						//raise arm
						//arm moves at 8.5 inches/second;
						arm.set(0.6);
						leftSpeed = .30;
						rightSpeed = -.30;
					}
			
					if(secondTime>System.currentTimeMillis () && firstTime<System.currentTimeMillis())	{
							
						rightSpeed = .65;
						leftSpeed = rightSpeed - 0.0;
					}
			
					if(thirdTime>System.currentTimeMillis() && secondTime<System.currentTimeMillis()) {
						
						arm.set(0.0);
						leftSpeed = -.30;
						rightSpeed = .30;
					}
						
					if(fourthTime>System.currentTimeMillis() && thirdTime<System.currentTimeMillis()) {
					
						leftSpeed = .60; 
						rightSpeed = .60; 
					}
							
					if(fifthTime>System.currentTimeMillis() && fourthTime<System.currentTimeMillis()) {
							
						rightSpeed = 0;
						leftSpeed = 0;
						
						leftSucker.set(.60);
						rightSucker.set(-.60);
							
					}
						
					if(sixthTime>System.currentTimeMillis() && fifthTime<System.currentTimeMillis()) {
						
						arm.set(-.60);
						leftSpeed = .60;
						leftSpeed = rightSpeed;
					}
						
					if(seventhTime>System.currentTimeMillis() && sixthTime<System.currentTimeMillis()) {
							
						leftSpeed = -.60;
						rightSpeed = .60; 
					}
							
					if(eighthTime>System.currentTimeMillis() && seventhTime<System.currentTimeMillis()) {
							
						arm.set(0);
						leftSpeed = .60;
						leftSpeed = rightSpeed;
					}
								
					if(ninthTime>System.currentTimeMillis() && eighthTime<System.currentTimeMillis()) {
							
						leftSpeed = .60;
						rightSpeed = -.60; 
					}
									
					if(tenthTime>System.currentTimeMillis() && ninthTime<System.currentTimeMillis()) {
						
						leftSpeed = .60;
						leftSpeed = rightSpeed; 
						
						leftSucker.set(-.30);
						rightSucker.set(.30);
					}
								
					if(eleventhTime>System.currentTimeMillis() && tenthTime<System.currentTimeMillis()) {
							
						rightSucker.set(0);
						leftSucker.set(0);
							
						leftSpeed = -.60;
						leftSpeed = rightSpeed;
					}
							
					if(twelthTime>System.currentTimeMillis() && eleventhTime<System.currentTimeMillis()) {
							
						arm.set(.60);
						leftSpeed = .60;
						rightSpeed = -.60;
				
					}
						
					if(thirteenthTime>System.currentTimeMillis() && twelthTime<System.currentTimeMillis()) {
							
						leftSpeed = .60;
						leftSpeed = rightSpeed;
							
					}
									
					if(fourteenthTime>System.currentTimeMillis() && thirteenthTime<System.currentTimeMillis()) {
							
						arm.set(0);
						leftSpeed = -.60;
						rightSpeed = .60;
					}
										
					if(fifteenthTime>System.currentTimeMillis() && fourteenthTime<System.currentTimeMillis()) {
						
						leftSpeed = .60;
						leftSpeed = rightSpeed; 
					}
						
					if(fifteenthTime>System.currentTimeMillis() && fourteenthTime<System.currentTimeMillis()) {
						
						leftSpeed = 0;
						rightSpeed = leftSpeed;
							
						leftSucker.set(.60);
						rightSucker.set(.60);
					}
						
					if(sixteenthTime>System.currentTimeMillis() && fifteenthTime<System.currentTimeMillis()) {
							
						leftSucker.set(0);
						rightSucker.set(0);
					}
				}
				
				break;
				//End Experimental 2 Cube Auto
	
		}
		myDrive.tankDrive(leftSpeed,rightSpeed);
	}

	
	public void teleopPeriodic() { 
//		myDrive.setSafetyEnabled(false);
		myDrive.tankDrive(left.getY(), right.getY());
			
		if (sucker.get() && !pusher.get()) {
			leftSucker.set(0.7);
			rightSucker.set(-0.7);
		} else {
			if (pusher.get() && !sucker.get()) {
				leftSucker.set(-0.7);
				rightSucker.set(0.7);
			} else {
				leftSucker.set(0.0);
				rightSucker.set(0.0);
			}
		}

		if (armUp.get() && !armDown.get()) {
			arm.set(1.0);
		} else { 
			if (armDown.get() && !armUp.get()) {
				arm.set(-0.7);
			} else {
				arm.set(0.0);
			}
		}			
			
		if (armClimb.get() && !armUp.get()) {
			arm.set(-1.0);
		}
			
		if (pusherSlow.get() && !sucker.get()) {
			leftSucker.set(0.45);
			rightSucker.set(-0.45);
		}
	}
}
