/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

//color Sensor
import edu.wpi.first.wpilibj.util.Color;

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
  private DifferentialDrive m_myRobot; 
  private DifferentialDrive myDrive; 

  //Pneumatics
  private DoubleSolenoid intakeSolenoid;
 // private DoubleSolenoid controlSolenoid; 
  private Compressor compressor; 

  //Joysticks
  private Joystick m_leftStick; 
  private Joystick m_rightStick;
  
  //Christina Added 
  //Joystick logitechController = new Joystick (1);
  //Joystick logitechController2 = new Joystick (2);
  //Joystick Gamepad;   No way im using this garbage
   
  

  //Buttons
  private JoystickButton shooter; 
  private JoystickButton slowShooter; 
  private JoystickButton intake; 
  private JoystickButton intakeOut; 
  private JoystickButton storage; 
  private JoystickButton storageOut; 
  private JoystickButton shooterMax;
  private JoystickButton shooterMid; 
  private JoystickButton shooterMin; 
  private JoystickButton retractIntake;
  private JoystickButton deployIntake;


  //Sparks
  private Spark rightShooter; 
  private Spark leftShooter; 
  private Spark intakeMotor; 
  private Spark storageMotor;
  private Spark shooterAngleMotor; 



  //limelight
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tv = table.getEntry("tv");
  NetworkTableEntry tx = table.getEntry("tx");
  NetworkTableEntry ty = table.getEntry("ty");
  NetworkTableEntry ta = table.getEntry("ta");

  String gameData;
  boolean moveForward;
  boolean moveBack;
  boolean turnLeft;
  boolean turnRight;
  boolean isCentered;
  boolean tiltUp;
  boolean tiltDown;
  boolean spinnerLeft;
  boolean spinnerRight;

  //autonomous 
  long firstTime;
  long secondTime;
  long thirdTime;
  long fourthTime; 
  long fifthTime; 
  long sixthTime;
  long seventhTime; 
  long eigthTime; 
  //double leftSpeed, rightSpeed; 
  double leftSpeed;
  double rightSpeed; 
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    System.out.println("robotInit()");
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    m_myRobot = new DifferentialDrive(new Spark(0), new Spark(1));
    //myDrive = new DifferentialDrive(new Spark(0), new Spark(1));
    m_leftStick = new Joystick(1); 
    m_rightStick = new Joystick(2); 
   
     

    intakeSolenoid = new DoubleSolenoid(0,7); 
    compressor = new Compressor(0); 
    //Sparks
    leftShooter = new Spark(3); 
    rightShooter = new Spark(2); 
    intakeMotor = new Spark(6);
    storageMotor = new Spark(5);
    shooterAngleMotor = new Spark(4);

    //Right Buttons
    
    shooter = new JoystickButton (m_rightStick, 1); 
    slowShooter = new JoystickButton (m_rightStick, 5);
    storageOut = new JoystickButton (m_rightStick, 4);
    deployIntake = new JoystickButton (m_rightStick, 2);
    //retractIntake = new JoystickButton (m_rightStick, 3);

    //Left Buttons
    intake = new JoystickButton (m_leftStick, 1);
    storage = new JoystickButton (m_leftStick, 5); 
    intakeOut = new JoystickButton (m_leftStick, 2); 
    shooterMax = new JoystickButton (m_leftStick, 6);
    shooterMid = new JoystickButton (m_leftStick, 4);
    shooterMin = new JoystickButton (m_leftStick, 3); 
    
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
  @Override
  public void autonomousInit() {
    intakeSolenoid.set(DoubleSolenoid.Value.kForward);
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    firstTime = System.currentTimeMillis() + 1500; 
    secondTime = System.currentTimeMillis() + 1750; 
    thirdTime = System.currentTimeMillis() + 4500; 
    fourthTime = System.currentTimeMillis() + 6000; 
    fifthTime = System.currentTimeMillis() + 6250; 
    sixthTime = System.currentTimeMillis() + 6750;
    seventhTime = System.currentTimeMillis() + 7000;
    eigthTime = System.currentTimeMillis() + 7500;
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    //Negative Values for the drive train means that it's going forward. 
    if(System.currentTimeMillis() <= firstTime){
      m_myRobot.tankDrive(.5, .5);
    }else if(firstTime < System.currentTimeMillis() && secondTime > System.currentTimeMillis()){
      m_myRobot.tankDrive(-0.5, 0.5); 
    }else if(secondTime < System.currentTimeMillis() && thirdTime > System.currentTimeMillis()){
      m_myRobot.tankDrive(-.5, -.5); 
    }else if(thirdTime < System.currentTimeMillis() && fourthTime > System.currentTimeMillis()){
      m_myRobot.tankDrive(0.5,-0.5);
    }else if(fourthTime < System.currentTimeMillis() && fifthTime > System.currentTimeMillis()){
      shooterAngleMotor.set(-1.0);
    }else if (fifthTime < System.currentTimeMillis() && sixthTime > System.currentTimeMillis()){
      leftShooter.set(1.0);
      rightShooter.set(1.0);
    }else if (sixthTime < System.currentTimeMillis() && seventhTime > System.currentTimeMillis()){
      storageMotor.set(0.9);
      leftShooter.set(1.0);
      rightShooter.set(1.0); 
    }else if (seventhTime < System.currentTimeMillis() && eigthTime > System.currentTimeMillis()){
      m_myRobot.tankDrive(.5, .5); 
    }
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  //General Information
    //leftSpeed = logitechController.getY();
    //rightSpeed = logitechController2.getY();
  leftSpeed = m_leftStick.getY();
  rightSpeed = m_rightStick.getY();  
  compressor.start(); 
  m_myRobot.tankDrive(m_leftStick.getY(), m_rightStick.getY());
//m_myRobot.tankDrive(leftStick.get(), rightStick.get());
  //myDrive.tankDrive(logitechController.getY(), logitechController2.getY());
  //Limelight

  final double a = ta.getDouble(0.0);
  final double x = tx.getDouble(0.0);
  final double y = ty.getDouble(0.0);
  final double area = ta.getDouble(0.0); 

  SmartDashboard.putNumber("LimelightA", a);
  SmartDashboard.putNumber("LimelightX", x);
  SmartDashboard.putNumber("LimelightY", y);
  SmartDashboard.putNumber("LimelightArea", area);
  
  turnRight = false;
  turnLeft = false; 
  isCentered = false;
  tiltDown = false;
  tiltUp = false;
  moveForward = false;
  moveBack = false;
  spinnerLeft = false;
  spinnerRight = false;

  if (x  < -5) {
      turnLeft = true;
    }
  if (x > 5) {
     turnRight = true;
    }

  if (y > 3 ) {
     tiltUp = true;
    }
  if (y < -1 ) {
     tiltDown = true;
    }
  if (a > 6 ) {
      moveBack = true;
    }
  if (a < 4 ) {
      moveForward = true;
    }
    if (!turnRight && !turnLeft && !tiltUp && !tiltDown && !moveForward && !moveBack ){
      isCentered = true;
    }

    SmartDashboard.putBoolean("turn left", turnLeft);
    SmartDashboard.putBoolean("turn right", turnRight);
    SmartDashboard.putBoolean("is centered", isCentered);
    SmartDashboard.putBoolean("tilt up", tiltUp);
    SmartDashboard.putBoolean("tilt down", tiltDown);
    SmartDashboard.putBoolean("move forward", moveForward);
    SmartDashboard.putBoolean("move back", moveBack); 
    //Limelight Ends

    m_myRobot.tankDrive(m_leftStick.getY(), m_rightStick.getY()); 
    //myDrive.tankDrive(logitechController.getY(), logitechController2.getY());
    //Shooter
    /*
    if(shooter.get() && !slowShooter.get()){
      leftShooter.set(1.0);
      rightShooter.set(1.0); 
    }else if(slowShooter.get() && !shooter.get()){
      leftShooter.set(0.3);
      rightShooter.set(0.3); 
    }else{
      leftShooter.set(0.0);
      rightShooter.set(0.0); 
    }
    
    //Intake
    if(intake.get() && !intakeOut.get()){
      intakeMotor.set(0.9);
    }else if(intakeOut.get() && !intake.get()){
      intakeMotor.set(-0.9);
    }else{
      intakeMotor.set(0.0); 
    }
    
    //Hopper to Shooter
    if(storage.get() && !storageOut.get()){
      storageMotor.set(0.9);
    }else if(storageOut.get() && !storage.get()){
      storageMotor.set(-0.9); 
    }else{
      storageMotor.set(0.0); 
    }
    
    //Shooter Angles 
    if (shooterMax.get()){
      shooterAngleMotor.set(-1.0);  
       }else if (shooterMid.get()){
      shooterAngleMotor.set(0.0);
    }else if (shooterMin.get()){
      shooterAngleMotor.set(1.0);
    }

    //Pneumatic stuff 
    if(deployIntake.get() && !retractIntake.get()){
      intakeSolenoid.set(Value.kForward);
    }else if(retractIntake.get() && !deployIntake.get()){
      intakeSolenoid.set( Value.kReverse);
    }
    */
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
