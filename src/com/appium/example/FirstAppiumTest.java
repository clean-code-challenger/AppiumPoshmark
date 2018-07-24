package com.appium.example;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class FirstAppiumTest extends JFrame implements ActionListener, Runnable {
	
	private static AndroidDriver driver;
	private static WebDriverWait wait;
	
	private Thread realtimeThread;

	//UI Controls
	private Button btn_Run, btn_Start;

	private Label lbl_Email, lbl_Password;
	private TextField txt_Email, txt_Password;
	
	//Variables for Global
	private static String strEmail, strPass;
	private static String filename;
	private static String item_info;
	
	private boolean isRun = false;
	
	public static void main(String[] args)  throws MalformedURLException, InterruptedException {
		// TODO Auto-generated method stub
		FirstAppiumTest main = new FirstAppiumTest();
		
		
	}
 
	public FirstAppiumTest() throws HeadlessException {
		super();
		_InitControls();
		_InitControlsEvent();
	}
	
	public void Setup()
	{

		DesiredCapabilities dc = new DesiredCapabilities();
		
		dc.setCapability("deviceName", "emulator-5554");
		dc.setCapability("BROWSER_NAME", "Android");
		dc.setCapability("platformName", "Android");
		dc.setCapability("appPackage", "com.poshmark.app");
		dc.setCapability("appActivity", "com.poshmark.ui.MainActivity");

		try {

			driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), dc);

			wait = new WebDriverWait(driver, 10);
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	}
	
	public void basicTest() throws InterruptedException {

		System.out.println(driver.toString());
		
		//Login To the Poshmark App
		WebElement loginbtn = driver.findElement(By.id("logInOption"));
		loginbtn.click();
		
		//Type Info and Click Sign In
		WebElement userName= driver.findElement(By.id("usernameTxt"));
		userName.sendKeys("Milan444");
		
		WebElement password= driver.findElement(By.id("passwordTxt"));
		password.sendKeys("Zhujingxiu123");
		
		WebElement nextBtn= driver.findElement(By.id("nextButton"));
		nextBtn.click();
		
		//******** Now in Main Page ************
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement shopTab= driver.findElement(By.id("shopTab"));
		shopTab.click();
		
		//******** Now in Shop Page ************
		System.out.println("Shop Page");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement ListView = driver.findElement(By.id("shop_feed"));
		//("//android.widget.LinearLayout[0]/android.widget.LinearLayout[3]/android.widget.LinearLayout[0]/android.widget.HorizontalScrollView/android.widget.LinearLayout[0]/android.widget.LinearLayout[1]/android.widget.ImageView"));
		
		java.util.List<WebElement> CityBtn = ListView.findElements(By.id("coverShotViewBig_v2"));
		System.out.println("CityBtn " + CityBtn.size());
		
		//
		//CityBtn.click();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRun)
		{
			GatherName();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource()==btn_Run) {

			btn_Run.setEnabled(false);
			Setup();
			try {
				basicTest();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(arg0.getSource()==btn_Start) {
			isRun = true;
			realtimeThread = new Thread(this);
			realtimeThread.start();
		}
	}

	public void scrollDown() {
		
		System.out.println("Scrolling Down Function");
		
		org.openqa.selenium.Dimension size = driver.manage().window().getSize();
//		int startx = (int) (size.width * 0.8);
//		int endx = (int) (size.width * 0.2);
		
		int startx = (int)(size.width*0.5);
		
		int starty = (int) (size.height * 0.8); 
		int endy = (int) (size.height * 0.2); 
		
		
		io.appium.java_client.TouchAction action = new TouchAction(driver);
	    action.press(PointOption.point(startx, starty)).moveTo(PointOption.point(startx, endy)).release().perform();
		
	}
 
	public int GatherName()
	{
		List<WebElement> vendors = driver.findElements(By.id("user_handle"));
		
		org.openqa.selenium.Dimension size = driver.manage().window().getSize();
		
		System.out.println("VENDOR COUNT" + vendors.size());
		
		for(int i = 0; i < vendors.size(); i++)
		{
			System.out.println("VENDOR " + i + ":" + vendors.get(i).getText());
			String str = vendors.get(i).getText();
			writeProductUrl(str);
		}

		scrollDown();
		
		return vendors.size();
	}
	
	private void writeProductUrl(String item_info)
	{
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileOutputStream(
				    new File("output.csv"), true /* append = true */));
	        writer.append(item_info);
			writer.append("\n");
	
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void _InitControls() {
		FlowLayout fl = new FlowLayout();
		setLayout(fl);

		btn_Run = new Button("Run");
		btn_Start = new Button("Start");
		
		this.add(btn_Run);

		Panel labelpanel = new Panel();

		lbl_Email = new Label("Email :");
		labelpanel.add(lbl_Email);
		txt_Email = new TextField(30);

		labelpanel.add(txt_Email);

		lbl_Password = new Label("Password :");
		labelpanel.add(lbl_Password);
		txt_Password = new TextField(20);
		labelpanel.add(txt_Password);

		this.add(labelpanel);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int nScreendWidth = screenSize.width;
		int nScreendHeight = screenSize.height;

		this.add(btn_Start);

		setTitle("EBayCapture - 2018.7.20");
		setSize(nScreendWidth, 200); // frame size, width x height

		this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we) {
            	try {
            		driver.close();
            	}
            	catch(Exception e)
            	{
            		e.printStackTrace();
            	}
            	System.exit(0);
            }
        });

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        this.setVisible(true);
	}
	
	private void _InitControlsEvent() {
		
		btn_Start.addActionListener(this);
		btn_Run.addActionListener(this);

	}
}
