/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//package frc.robot;

//not sure if the commment of "package frc.robot;" is correct I followed the suggestion
//if future code doesn't run after deploying more than once (sometimes its slow)
//then come back to this because it may be wrong and may just need an update for 2020

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */

  public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  Joystick logitechController = new Joystick(1);
  Joystick Gamepad;
  

 // GenericHID logitechControllerHID = new GenericHID(1);
  //GenericHID Gamepad;
  // Right Side
  JoystickButton sucker;
  JoystickButton pusher;
  JoystickButton wristDown;
  JoystickButton liftDown;
  JoystickButton wristUp;
  JoystickButton liftUp;
  
  //Left Side
  
  JoystickButton armDown;
  JoystickButton beakDown;
  JoystickButton armUp; 
  JoystickButton beakUp;
  JoystickButton topGoal;
  JoystickButton middleGoal;
  JoystickButton bottomGoal;
  JoystickButton topHatch;
  JoystickButton middleHatch;
  JoystickButton bottomHatch;

  //GenericHID xBox;

//    CameraServer server; 
    Spark m_left;
    Spark m_right;
    Spark beakMotor;
    DifferentialDrive myDrive;
    WPI_VictorSPX armMotor;
    WPI_VictorSPX liftMotor;
  	WPI_TalonSRX intakeMotor;
    WPI_TalonSRX wristMotor; 

    JoystickButton levelTwoForward;
    JoystickButton levelTwoReverse;
    DoubleSolenoid levelTwoSolenoid;

    AnalogPotentiometer armPot;
    Compressor compressor;
    int liftLimitHigh;
    int liftLimitLow;
    double armLimitHigh;
    double armLimitLow;
    int beakLimitHigh;
    int beakLimitLow; 
    int wristLimitHigh;
    int wristLimitLow;
    int count;
    int kForward;
    int kReverse;
    String gameData; 
	//int location;
	double goalAngle;   
 	
  //Auto Chooser
//	Command autonomousCommand; 
	SendableChooser<Integer> autoChooser;
	int autoMode;
	
//	public static OI oi; 
	//End Auto Chooser
 boolean alignButtonState;
 boolean onLeft;
 boolean onRight;
 boolean direction;
 boolean stopped;

 double rightSpeed;
 double leftSpeed;
 double distance;
 double period;
 double rate;

 enum Direction{turnLeft, turnRight};
 Direction turnDirection;
 boolean turn;
 
//Spark linearActuator;
JoystickButton in;
JoystickButton out;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

//Camera start

		 CameraServer.getInstance().startAutomaticCapture();
	
		new Thread(() ->
		{
		  UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			camera.setResolution(320, 240);
			CvSink cvSink = CameraServer.getInstance().getVideo();
			CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
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

  //Motor Controllers
    //Drive Train
      m_left = new Spark(1);
      m_left.setInverted(true); 
      m_right = new Spark(3);
      m_right.setInverted(true);
    
      //Lift
      liftMotor = new WPI_VictorSPX(4); 
      //Arm
      armMotor = new WPI_VictorSPX(3);
      armPot = new AnalogPotentiometer(0, 6.0);

    //Level 2 Climb
      levelTwoSolenoid = new DoubleSolenoid(0, 1);

    //Intake
      intakeMotor = new WPI_TalonSRX(1); 

    // Compressor
      compressor = new Compressor(0);
    //Wrist
      wristMotor = new WPI_TalonSRX(2);
      
    //Beak
      beakMotor = new Spark(5);

  //Joysticks
    myDrive = new DifferentialDrive(m_left, m_right);
    Gamepad = new Joystick(0);
    

    //  right = new Joystick(1);
    //  left = new Joystick(0);
  
    
    
    beakUp = new JoystickButton(Gamepad, 1);
    beakDown = new JoystickButton(Gamepad, 3);
    wristUp = new JoystickButton(Gamepad, 4);
    wristDown = new JoystickButton(Gamepad, 2);
    liftUp = new JoystickButton(Gamepad, 5);
    liftDown = new JoystickButton(Gamepad, 6);
    armUp = new JoystickButton(Gamepad, 7);
    armDown = new JoystickButton(Gamepad, 8);
    sucker = new JoystickButton(Gamepad, 9);
    pusher = new JoystickButton(Gamepad, 10); 
   // levelTwoForward = new JoystickButton(Gamepad, 9);//pist-out
   // levelTwoReverse = new JoystickButton(Gamepad, 10);//pist-in

	   /* sucker = new JoystickButton(Gamepad,4);
      pusher = new JoystickButton(Gamepad,3);
      wristDown = new JoystickButton(Gamepad,11);
      wristUp = new JoystickButton(Gamepad,12);
      liftDown = new JoystickButton(Gamepad,5);
      liftUp = new JoystickButton(Gamepad,6);
      //levelTwoForward = new JoystickButton(Gamepad, 10);
      //levelTwoReverse = new JoystickButton(Gamepad, 9);
      armUp = new JoystickButton(Gamepad,7); 
      armDown = new JoystickButton(Gamepad,8);
      beakDown = new JoystickButton(Gamepad,2);
      beakUp = new JoystickButton(Gamepad,1);*/

//	gyro = new ADXRS450_Gyro(); 
//		server = CameraServer.getInstance();
//		server.setQuality(50);
//		server.startAutomaticCapture();
 
/*
    in = new JoystickButton(right, 11);
    out = new JoystickButton(right, 12);
    */
}

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
 
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */

   /*
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    //teleopInit();
    
  }
*/
  /**
   * This function is called periodically during autonomous.
   */

  @Override
  public void autonomousPeriodic() {
  
    teleopPeriodic();
    
  }

  @Override
  public void teleopInit() {
 
  alignButtonState = false;
  onLeft = false;
  onRight = false;
  leftSpeed = 0.0;
  rightSpeed = 0.0;
  turnDirection = Direction.turnLeft;
  turn = false;
  
}
  /**
   * This function is called periodically during operator control.
   */

  @Override
  public void teleopPeriodic() {

    myDrive.setSafetyEnabled(false);
//    leftSpeed = Gamepad.getX();
    // left.getY();
//    rightSpeed = Gamepad.getY();
    myDrive.tankDrive(Gamepad.getY(), Gamepad.getZ());
    Gamepad.getRawAxis(1);
   Gamepad.getRawAxis(3);

/*
//Pneumatics
if (levelTwoForward.get() && !levelTwoReverse.get()){
  levelTwoSolenoid.set(Value.kForward);
}
  else if(levelTwoReverse.get() && !levelTwoForward.get()){
    levelTwoSolenoid.set(Value.kReverse);
  }
    else {
     levelTwoSolenoid.set(Value.kOff);
}
compressor.start();
*/
//Right Joystick 
//Manual Controls

  //Pusher & Sucker
  
  if (sucker.get() && !pusher.get()) {
        intakeMotor.set(1.0);
    } else if (pusher.get() && !sucker.get()) {
        intakeMotor.set(-1.0);
    } else {
        intakeMotor.set(0.0);
    }

  //Lift Up & Down
    if (liftUp.get() && !liftDown.get()){
        liftMotor.set(-1.0);
    } else if (liftDown.get() && !liftUp.get()){
        liftMotor.set(0.5);
    } else {
        liftMotor.set(-0.1);
    }

    //Wirst Down & Up
    if (wristUp.get() && !wristDown.get()){
        wristMotor.set(0.55); 
    } else if (wristDown.get() && !wristUp.get()){
        wristMotor.set(-0.45);
    } else {
        wristMotor.set(0.2);
    }

//Left Joystick

    //Arm Up & Down
      if (armUp.get() && !armDown.get()){
          armMotor.set(.75);
      } else if (armDown.get() && !armUp.get()){
          armMotor.set(-.35);
      } else {
          armMotor.set(0.0);
      }

    //Beak Up & Down
      if (beakUp.get() && !beakDown.get()){
          beakMotor.set(-0.5);
      } else if (beakDown.get() && !beakUp.get()){
          beakMotor.set(0.5);
      } else{
          beakMotor.set(0.0);
      }
     
  }
}