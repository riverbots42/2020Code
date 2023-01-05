/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
//import com.sun.org.apache.xalan.internal.xsltc.runtime.output.TransletOutputHandlerFactory;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
//import com.qualcomm.robotcore.hardware.DcMotor;
//possible missing imports
import edu.wpi.first.wpilibj.DriverStation;
 

//end of possible things missing
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
//import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C; //(Not Currently being used)
import edu.wpi.first.wpilibj.I2C.Port; //(Not Currently being Used)
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.livewindow.LiveWindow; (Not being used currently)
import edu.wpi.first.wpilibj.interfaces.Potentiometer;//(Not currently being used)
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.ColorSensor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Compressor;  
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

  
 
	
  Joystick left, right;
  
  //Right Side
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


 // I2C.Port colorSensorOnePort;
    ColorSensor colorSensorOne;
    ColorSensor colorSensorTwo;
  	ADXRS450_Gyro gyro; 
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
    VictorPotControl armAssist;
    VictorEncoderControl liftAssist;
    TalonEncoderControl wristAssist;
    SparkEncoderControl beakAssist;
    Encoder liftEncoder;
    Encoder wristEncoder;
    //Encoder beakEncoder;
    AnalogInput beakEncoder;
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

/*
 double distance = liftEnc.getRaw();
 double distance = liftEnc.getDistance();
 double period = liftEnc.getPeriod();
 double rate = liftEnc.getRate();
 */
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
    //m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    //m_chooser.addOption("My Auto", kCustomAuto);
    //SmartDashboard.putData("Auto choices", m_chooser);
    //colorSensorOnePort = new I2C.Port();
    //liftEnc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    /* pixy = new SerialPort(19200, port);
    packets = new PixyPacket[7];
    pExc = new PixyException(print);
    */

//Encoder Resets


// Encoders
  //Encoder liftEnc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
 /* 

  liftEnc.setMaxPeriod(0.1);
  
  liftEnc.setMinRate(10);

  liftEnc.setDistancePerPulse(5);
 
  liftEnc.setReverseDirection(true);
 
  liftEnc.setSamplesToAverage(7);
  liftEnc.reset();
  int count = liftEnc.get();

  
  double distance = liftEnc.getRaw();
  double distance = liftEnc.getDistance();
  double period = liftEnc.getPeriod();
  double rate = liftEnc.getRate();
  boolean direction = liftEnc.getDirection();
  boolean stopped = liftEnc.getStopped();
*/  


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
  

//Pixy Cam
 // Pixy2 pixy = Pixy2.createInstance(SPI);

  //Motor Controllers
    //Drive Train
      m_left = new Spark(1);
      m_left.setInverted(true); 
      m_right = new Spark(3);
      m_right.setInverted(true);
    
      //Lift
      liftMotor = new WPI_VictorSPX(4); 
      liftEncoder = new Encoder(2, 3);
      //liftEncoder.reset();
      liftEncoder.setDistancePerPulse(27.75/1548);
     // liftLimitHigh = 3000;
     // liftLimitLow = -200;
     //Top is 2905 Bottom is 52
      liftAssist = new VictorEncoderControl(liftMotor, liftEncoder);
      //Arm
      armMotor = new WPI_VictorSPX(3);
      armPot = new AnalogPotentiometer(0, 6.0);
      //(port number, scaling factor, X)
      armAssist = new VictorPotControl(armMotor, armPot);
      //armLimitHigh = 7.45;
      //armLimitLow = 0.8;  
    //Level 2 Climb
      levelTwoSolenoid = new DoubleSolenoid(0, 1);

    //Intake
      intakeMotor = new WPI_TalonSRX(1); 

    // COmprssor
        compressor = new Compressor(0);
    //Wrist
      wristMotor = new WPI_TalonSRX(2);
      wristEncoder = new Encoder(0, 1);
     
      //(port, scaling factor )
      wristAssist = new TalonEncoderControl(wristMotor, wristEncoder);          
    //Beak
      beakMotor = new Spark(5);
      //beakEncoder = new Encoder(250, 500);
     // beakEncoder = new AnalogInput(1);
      //change beak encoder to analog input
      //then record horizontal voltage and verticle voltage to use as limits
      beakAssist = new SparkEncoderControl(beakMotor, beakEncoder);

  //Joysticks
    myDrive = new DifferentialDrive(m_left, m_right);
    right = new Joystick(1);
    left = new Joystick(0);

    //Joystick right
		
      
      sucker = new JoystickButton(right,1);
      pusher = new JoystickButton(right,2);
      wristDown = new JoystickButton(right,3);
      liftDown = new JoystickButton(right,4);
      wristUp = new JoystickButton(right,5);
      liftUp = new JoystickButton(right,6);
     
		  
    //Joystick left
      levelTwoForward = new JoystickButton(left, 1);
      levelTwoReverse = new JoystickButton(left, 2);
      armDown = new JoystickButton(left,3);
      beakDown = new JoystickButton(left,4);
      armUp = new JoystickButton(left,5);
      beakUp = new JoystickButton(left,6);
      topGoal = new JoystickButton(left,7);
      topHatch = new JoystickButton(left, 8);
      middleGoal = new JoystickButton(left,9);
      middleHatch = new JoystickButton(left, 10);
      bottomGoal = new JoystickButton(left,11);
      bottomHatch = new JoystickButton(left, 12);
    

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

  
 // SmartDashboard.putNumber("armPot", armPot.get());
 // SmartDashboard.putNumber("liftEncoder", liftEncoder.get());
  //SmartDashboard.putNumber("wristEncoder", wristEncoder.get());
  //SmartDashboard.putNumber("beakEncoder", beakEncoder.get());
  
}
  /**
   * This function is called periodically during operator control.
   */


  @Override
  public void teleopPeriodic() {

    //myDrive.setSafetyEnabled(false);
    rightSpeed = left.getY();
    leftSpeed = right.getY();
    myDrive.tankDrive(left.getY(), right.getY());

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
    // && liftEncoder.get() < liftLimitHigh){
        liftMotor.set(-1.0);
       // System.out.println("liftEncoder="+ liftEncoder.getDistance());
      //  liftAssist.cancel();
    } else if (liftDown.get() && !liftUp.get()){
      //&& liftEncoder.get() > liftLimitLow) {
        liftMotor.set(0.5);
       // System.out.println("liftEncoder="+ liftEncoder.getDistance());
       // liftAssist.cancel();
    } else {
        liftMotor.set(-0.1);
    }
/*
  //Lift Auto height
   if (topGoal.get()){
     liftAssist.setGoal(-1.0, 2905);
   } else if(middleGoal.get()){
     liftAssist.setGoal(-1.0, 1903);
   } else if(bottomGoal.get()){
     liftAssist.setGoal(-1.0, 443);
   }   else {
     liftAssist.setGoal(0.0, 0);
   }
   liftAssist.moveActuator();
*/
/*    //Stall
    if (stall.get() && !liftUp.get() && !liftDown.get()){
      liftMotor.set(-0.1);
    }
*/
    //Wirst Down & Up
    if (wristUp.get() && !wristDown.get()){
    // && wristEncoder.get() < wristLimitHigh) {
        wristMotor.set(0.55); 
       // System.out.println("wrist encoder=" + wristEncoder.get());
       // wristAssist.cancel();
    } else if (wristDown.get() && !wristUp.get()){
    // && wristEncoder.get() > wristLimitLow) {
        wristMotor.set(-0.45);
       // System.out.println("wrist encoder=" + wristEncoder.get());
      //  wristAssist.cancel();
    } else {
        wristMotor.set(0.2);
    }

//Left Joystick

    //Arm Up & Down
       // System.out.println("armUp ="+ armUp.get());
       // System.out.println("armDown ="+ armDown.get());
        //System.out.println("armPot ="+ armPot.get());
      if (armUp.get() && !armDown.get()){
      // && armPot.get() > armLimitLow) {
        //opposite of what I think because the potentiometer is inverted
          armMotor.set(.85);
         // System.out.println("arm pot="+ armPot.get());
         // armAssist.cancel();
      } else if (armDown.get() && !armUp.get()){
        //&& armPot.get() <armLimitHigh) {
          armMotor.set(-.45);
        //  System.out.println("arm pot="+ armPot.get());
         // armAssist.cancel();
      } else {
          armMotor.set(0.0);
      }



    //Beak Up & Down
      if (beakUp.get() && !beakDown.get()){
          //&& beakEncoder.get() < beakLimitHigh) {
          beakMotor.set(-0.5);
        //System.out.println("beak Encoder="+ beakEncoder.getAverageVoltage());
         // beakAssist.cancel();
      } else if (beakDown.get() && !beakUp.get()){
     // && beakEncoder.get() > beakLimitLow) {
          beakMotor.set(0.5);
       // System.out.println("beak Encoder="+ beakEncoder.getAverageVoltage());
          //beakAssist.cancel();
      } else{
          beakMotor.set(0.0);
      }
      
      //Auto Assists
  /*    if(middleGoal.get()){
        armAssist.setGoal(0.5, 3.0);
        liftAssist.setGoal(0.5, 21.0);
        wristAssist.setGoal(0.2, 45.0); 
   } 
     if(topGoal.get()){
         armAssist.setGoal(0.5, 3.0);
         liftAssist.setGoal(0.5, 21.0);
         wristAssist.setGoal(0.2, 45.0);
     }
     if(bottomGoal.get()){
         armAssist.setGoal(0.5, 3.0);
         liftAssist.setGoal(0.5, 21.0);
         wristAssist.setGoal(0.2, 45.0);
        }
     if(topHatch.get()){
         armAssist.setGoal(0.5, 4.59);
         liftAssist.setGoal(-0.5, 53.69);
        // wristAssist.setGoal(0.2, 45.0);
     }
     if(middleHatch.get()){
        armAssist.setGoal(0.5, 4.59);
        liftAssist.setGoal(-0.5, 19.889);
       // wristAssist.setGoal(0.2, 45.0);
     }
     if(bottomHatch.get()){
         armAssist.setGoal(0.5, 4.76);
         liftAssist.setGoal(-0.5, 0.0);
        // wristAssist.setGoal(0.2, 45.0);
     }

    //Need to be last lines in Assist Section
        armAssist.moveActuator();
        liftAssist.moveActuator();
       // beakAssist.moveActuator();
       // wristAssist.moveActuator();
*/
        //if bad blame paz 
    //    SmartDashboard.updateValues();
/* 
 drive.tankdrive(leftDriveAccumulator,rightDriveAccumulator);
           Encoder enc;
           enc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
           Encoder sampleEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
           sampleEncoder.setMaxPeriod(.1);
           sampleEncoder.setMinRate(10);
           sampleEncoder.setDistancePerPulse(5);
           sampleEncoder.setReverseDirection(true);
           sampleEncoder.setSamplesToAverage(7);
    
  //Middle Goal Orignial, Will not Work now
  
    if (middleGoal.get()) {
        liftControl = new WPI_VictorSPX(autoMode);
        armControl.setGoal(0.5, 3.0);
        double d = 21.0;
        ((VictorPotControl) liftControl.setGoal(0.5, 21.0));
        wristControl.setGoal(0.5, -15.0);
      }

      armControl.moveActuator();
      liftControl.moveActuator();
      wristControl.moveActuator();
      beakControl.moveActuator();
      */
  }



    
    //  This function is called periodically during test mode.
     
    
  @Override
  public void testPeriodic() {
  }
} 
