package start;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
//import junit.framework.Assert;


@Listeners({ ScreenshotUtility.class })
public class Testscript extends androidInterface{
	
	//Java client 3.1
	//private AndroidDriver<WebElement> driver;
	
	//public
	//AndroidDriver driver;
	AppiumDriver driverWeb;
	//WebDriverWait wait;
	TouchAction touch;
	WebDriverWait waitShort;
	WebDriverWait waitLong;
	Boolean uploadSuccess;
	Boolean signedIn;
	//FirefoxDriver web;
	private SoftAssert softAssert = new SoftAssert();
	
	//device screen
	int screenHeight;
	int screenWidth;
	
	@SuppressWarnings("rawtypes")

	@BeforeClass
	@Parameters("port")
	//Setup
	public void setup(String port) throws MalformedURLException, InterruptedException {
		
		File appDir = new File("src");
		File app = new File(appDir, "ll-1.5.0.166-staging.apk");
		
		//appium specific configuration
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		cap.setCapability(MobileCapabilityType.DEVICE_NAME,"Android device");
		cap.setCapability(MobileCapabilityType.APP,app.getAbsolutePath());
		
		//create objects
		driver = new AndroidDriver(new URL("http://127.0.0.1:"+port+"/wd/hub"),cap);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver,60);
		waitShort = new WebDriverWait(driver,10);
		waitLong = new WebDriverWait(driver,240);
		touch = new TouchAction(driver);
		//web = new FirefoxDriver();
		//web.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		uploadSuccess = true;
		signedIn = false;
		
		screenHeight = driver.manage().window().getSize().getHeight();
		screenWidth = driver.manage().window().getSize().getWidth();
		
		try{
			driver.findElementById("com.sphero.sprk:id/negative_action").click();
		}
		catch(Exception e){
			System.out.println("Bluetooth dialog did not appearo on splash");
		}
		
		//check landing page is home - feed
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/bottom_nav")));
		
		//Verify starting screen is Home
		String selectedNavBar = driver.findElementById("com.sphero.sprk:id/bottom_navigation_small_item_title").getText();
		Assert.assertEquals(selectedNavBar, "Home");
		//Verify selected tab is Feed
		Boolean selectedTab = driver.findElementByXPath("//android.support.v7.a.d[@index='0']/android.widget.TextView[@text='Feed']").isSelected();
		Assert.assertTrue(selectedTab);
		
		System.out.println(driver.currentActivity());
		
		//sign out if signed in
		if(driver.findElements(By.id("com.sphero.sprk:id/sign_in_button")).size()==0){
			System.out.println("Sign in button not found, signed in");
			//Sign out process
			clickTab("Profile");
			clickButton("Sign out");
			clickButton("positive");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("android:id/progress")));
			driver.findElements(By.id("com.sphero.sprk:id/sign_in_button"));
			driver.findElements(By.id("com.sphero.sprk:id/sign_in_text"));
			clickTab("Feed");
		}
	
	}
	
	@BeforeMethod
	//Verify screen is build before each case
	public void startPoint() throws Exception{
		
		//Check if app crashed
		try{
			driver.findElement(By.id("android:id/message"));
			driver.closeApp();
			driver.launchApp();
		}
		catch(Exception crash){
			System.out.println("Crash not detected");
		}
		
		//back out of edit screen 
		if(uploadSuccess==false){
			System.out.println("Uploading failed, backing out to nav bar");
			while(driver.findElements(By.id("com.sphero.sprk:id/bottom_nav")).size()==0){
				driver.pressKeyCode(AndroidKeyCode.BACK);
				Thread.sleep(1500);
				try{
					clickButton("yes");
				}
				catch(Exception e){
					System.out.println("No dialog was found blocking exit");
				}
			}
		}
		
		uploadSuccess = true; //reset back to original state
		
		driver.findElementById("com.sphero.sprk:id/bottom_nav");
		
	}
	
	
	
	@DataProvider(name = "signInInfo")
    public Object[][] dataProviderMethod1() {
		//String username,String password, String type, Boolean valid, Boolean Clever, Boolean signOut
        return new Object[][] { 
            {"ffsqat+s4@gmail.com","yyyyyy","student",true,false,true}, 
            {"ffsqat@gmail.com","1asdf","instructor",false,false,true}, 
            {"307201","307201","student",true,true,true}, 
            {"307201","307201342","student",false,true,true}
            };
    }
    
    @Test(dataProvider = "signInInfo",invocationCount=10)
    public void signIn(String username,String password, String type, Boolean valid, Boolean Clever, Boolean signOut){
        testLogTitle("Sign in");
        System.out.println("Signing in with username:" + username + "| password:" + password);
        System.out.println("Account type:" + type +" | Clever:" + Clever);
        System.out.println("Valid credentials: " + valid);
        System.out.println("Sign out after : " + signOut);
        
        //check account type before running
        if("instructor"!=type.intern() && "student"!=type.intern()){
            Assert.fail("Wrong expected account types, test case not run");
        }
        
        clickNavBar("Home");
        clickTab("Profile");
        clickButton("Sign in");
        //Click the sign in button within the get started fragment
        driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/fragment_container']//*[@resource-id='com.sphero.sprk:id/sign_in_button']").click();
        System.out.println("Clicked Sign in button on Get Started");
        
        //Regular sign in
        if(Clever==false){
        	driver.findElementById("com.sphero.sprk:id/username_et").click();
            driver.findElementById("com.sphero.sprk:id/username_et").sendKeys(username);
            System.out.println("Entered username");
            driver.hideKeyboard();
            driver.findElementById("com.sphero.sprk:id/password_et").click();
            driver.findElementById("com.sphero.sprk:id/password_et").sendKeys(password);
            System.out.println("Entered password");
            driver.hideKeyboard();
            driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/fragment_container']//*[@resource-id='com.sphero.sprk:id/sign_in_button']").click();
            System.out.println("Clicked Sign In button on Sign In");
            
        }
        //Clever sign in
        else{
            driver.findElementById("com.sphero.sprk:id/clever_sign_in_button").click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.EditText")));
            List <WebElement> fields = driver.findElementsByClassName("android.widget.EditText");
            for(WebElement i : fields){
                if(i.getAttribute("name").equals("Username")){
                    i.sendKeys(username);
                    System.out.println("Entered username");
                }
                if(i.getAttribute("name").equals("Password")){
                    i.sendKeys(password);
                    System.out.println("Entered password");
                }
            }
            driver.hideKeyboard();
            driver.findElementByXPath("//android.webkit.WebView//android.widget.Button").click(); //login to clever button
            System.out.println("Clicked Sign in with clever button");
           // wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.sphero.sprk:id/nav_close")));
        }
        
        //Expected conditions for valid sign in
        if(valid == true){
        	wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("android:id/progress")));
        	driver.findElementById("com.sphero.sprk:id/contact_card");
        	driver.findElementById("com.sphero.sprk:id/edit_profile_button");
        	driver.findElementById("com.sphero.sprk:id/profile_text_field");
        	driver.findElementById("com.sphero.sprk:id/sign_out_button");
        	
        	//check for email id for instructor
        	if(type.toLowerCase().equals("instructor")){
        		driver.findElementById("com.sphero.sprk:id/email");
        	}
        	//check for grade for student
        	else{
        		driver.findElementById("com.sphero.sprk:id/grade_container");
        	}
        	
        	System.out.println("Sign in success");
            signedIn=true;
        }
        else if(valid == false && Clever==false){
        	wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("android:id/progress")));
            clickButton("ok"); //close invalid credentials dialog
            clickButton("back"); //close sign in
            clickButton("x"); //close get started

        }
        else if(valid == false  && Clever==true){
        	clickButton("back"); //close clever
        	clickButton("back"); //close sign in
        	clickButton("x"); //close get started
        }
        
        if(signOut==true){
        	if(valid==true){
        		signOut();
        	}
        }
        
    }
	
	@Test
	public void signOutDelete(){
		
		testLogTitle("Delete programs and Sign Out");
		
		//Delete all programs before signing out
		
		//deleteAllPrograms();
		
		signOut();
		
		signedIn=false;
	}
	
	@Test
	public void signOut(){
		
		testLogTitle("Sign Out");
		
		clickNavBar("home");
		clickTab("Profile");
		clickButton("Sign out");
		clickButton("positive");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("android:id/progress")));
		driver.findElements(By.id("com.sphero.sprk:id/sign_in_button"));
		driver.findElements(By.id("com.sphero.sprk:id/sign_in_text"));
		
		System.out.println("Sign out success");
		signedIn=false;
	}
	
	@Test
	public void signUp12YearsUnder(){
		clickMenu();
		clickPocketNavSignIn();
		driver.findElementById("com.sphero.sprk:id/sign_up_button").click();
		driver.findElementById("com.sphero.sprk:id/select_student").click();
		driver.findElementById("com.sphero.sprk:id/continue_button").click();
		driver.findElementById("com.sphero.sprk:id/initials_et").sendKeys("abc");
		//driver.hideKeyboard();
		driver.findElementById("com.sphero.sprk:id/guardian_email_et").sendKeys("asd23fk31efvm@sadlkfjkasdfjesac.com");
		driver.hideKeyboard();
		driver.findElementById("com.sphero.sprk:id/request_access_button").click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/continue_button")));
		driver.findElementById("com.sphero.sprk:id/continue_button").click();
		checkToolBarTitle("Build");
	}
	
	@Parameters({"student_username","student_email","password"})
	@Test
	public void signUp13Years(String student_username,String student_email,String password) throws Exception{
		clickMenu();
		clickPocketNavSignIn();
		driver.findElementById("com.sphero.sprk:id/sign_up_button").click();
		driver.findElementById("com.sphero.sprk:id/select_student").click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("android:id/pickers")));
		driver.findElementByXPath("//android.widget.NumberPicker[@index='2']/android.widget.EditText").click();
		driver.findElementByXPath("//android.widget.NumberPicker[@index='2']/android.widget.EditText").clear();
		driver.findElementByXPath("//android.widget.NumberPicker[@index='2']/android.widget.EditText").sendKeys("2000");
		
		
		driver.findElementByXPath("//android.widget.NumberPicker[@index='0']/android.widget.EditText").clear();
		driver.findElementByXPath("//android.widget.NumberPicker[@index='0']/android.widget.EditText").sendKeys("A");
		
		//driver.findElementByXPath("//android.widget.NumberPicker[@index='1']/android.widget.EditText").clear();
		//driver.findElementByXPath("//android.widget.NumberPicker[@index='1']/android.widget.EditText").sendKeys("11");
		
		
		driver.hideKeyboard();
		driver.findElementById("com.sphero.sprk:id/continue_button").click();
		
		driver.findElementById("com.sphero.sprk:id/class_name_helper").click();
		driver.findElementById("com.sphero.sprk:id/buttonDefaultNeutral").click();
		//driver.findElementById("com.sphero.sprk:id/class_name_et").clear();
		//driver.findElementById("com.sphero.sprk:id/class_name_et").sendKeys("classname");
		driver.findElementById("com.sphero.sprk:id/reenter_password_et").sendKeys(password);
		driver.findElementById("com.sphero.sprk:id/password_et").sendKeys(password);
		driver.findElementById("com.sphero.sprk:id/email_et").sendKeys(student_email);
		driver.findElementById("com.sphero.sprk:id/username_et").sendKeys(student_username);
		driver.pressKeyCode(AndroidKeyCode.BACK);
		Thread.sleep(2000); //sign up button to shift
		driver.findElementById("com.sphero.sprk:id/create_account_button").click();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/verify_account_button")));
		Thread.sleep(10000); //wait for email to be sent
		//Firefox();
		
		Thread.sleep(30000);
		driver.findElementById("com.sphero.sprk:id/verify_account_button").click();
		checkToolBarTitle("Build");
		dismissHint();
		signOut();
		//do automated gmail account verification
		
		//com.sphero.sprk:id/verify_account_button
		//com.sphero.sprk:id/do_this_later_button
	}
	@Parameters({"instructor_username","instructor_email","password"})
	@Test
	public void signUpInstructor(String instructor_username,String instructor_email,String password) throws Exception{
		clickMenu();
		clickPocketNavSignIn();
		driver.findElementById("com.sphero.sprk:id/sign_up_button").click();
		driver.findElementById("com.sphero.sprk:id/select_instructor").click();
		driver.findElementById("com.sphero.sprk:id/reenter_password_et").click();
		driver.findElementById("com.sphero.sprk:id/reenter_password_et").sendKeys(password);
		driver.findElementById("com.sphero.sprk:id/password_et").click();
		driver.findElementById("com.sphero.sprk:id/password_et").sendKeys(password);
		driver.findElementById("com.sphero.sprk:id/email_et").click();
		driver.findElementById("com.sphero.sprk:id/email_et").sendKeys(instructor_email);
		driver.findElementById("com.sphero.sprk:id/username_et").click();
		driver.findElementById("com.sphero.sprk:id/username_et").sendKeys(instructor_username);
		driver.pressKeyCode(AndroidKeyCode.BACK);
		Thread.sleep(2000); //sign up button to shift
		driver.findElementById("com.sphero.sprk:id/continue_signup_button").click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/verify_account_button")));
		Thread.sleep(10000); //wait for email to be sent
		//Firefox();
		
		Thread.sleep(30000);
		driver.findElementById("com.sphero.sprk:id/verify_account_button").click();
		checkToolBarTitle("Build");
		dismissHint();
		signOut();
		
	
	}

	@Test //open a sample program and copy
	public void copySampleProgram() throws Exception{
		testLogTitle("View and Copy a Sample Program");
		int numOfProg = countProgram();
		clickSampleProgramsTab();
		clickProgram();
		clickCopy();
		clickButton("yes"); //confirm
		clickButton("yes"); //ok
		checkToolBarTitle("Build");
		clickMyProgramsTab();
		int numOfProgPost = countProgram();
		Assert.assertEquals(numOfProgPost, numOfProg+1);
		System.out.println("A copy of the sample program has been made");
	}
	
	@Test //open a sample program and copy
	public void copySampleProgramRepeatedly() throws Exception{
		testLogTitle("View and Copy a Sample Program");
		int numOfProg = 0;
		
		clickSampleProgramsTab();
		//driver.findElementByXPath("//*[@class='android.support.v7.app.ActionBar$Tab' and @index='1']").click();
		//driver.findElementsByXPath("//android.support.v7.app.ActionBar$Tab[@index='1']");
		while(numOfProg<=500){
			clickProgram();
			clickCopy();
			clickButton("yes"); //confirm
			clickButton("yes"); //ok
			System.out.println("Program # " + numOfProg +"has been made");
			numOfProg++;
		}
	}
	
	@Test
	public void viewSampleProgram() throws Exception{
		testLogTitle("View a Sample Program");
		int numOfProg = countProgram();
		//click sample programs tab
		clickSampleProgramsTab();
		//click the first sample program
		clickProgram();
		//click view
		clickView();
		leaveCanvas();
		checkToolBarTitle("Build");
		clickMyProgramsTab();
		int numOfProgPost = countProgram();
		Assert.assertEquals(numOfProgPost, numOfProg);
		System.out.println("No copy is made viewing a program");
	}
	

	
	//Unit test for counting programs
	public int countProgram(){
		String xpath = "//android.support.v7.widget.RecyclerView/descendant::android.widget.FrameLayout";
		List <WebElement> ListByXpath = driver.findElements(By.xpath(xpath));
		return ListByXpath.size();   
	}
	
	//@Test
	public void countPrograms(){
		String xpath = "//android.support.v7.widget.RecyclerView/descendant::android.widget.FrameLayout";
		List <WebElement> ListByXpath = driver.findElements(By.xpath(xpath));
		System.out.println(ListByXpath.size());
	}
	
	
	public void scrollVertical(String s,int duration){

		int startx = (int) (screenWidth * 0.5); 
		int starty = 0;
		int endy = 0;
		
		if(s.equals("up")){
			starty = (int) (screenHeight * 0.30); //upper half of screen
			endy = (int)(screenHeight-1);
			System.out.println("Scrolling up");
			
		}
		else if (s.equals("down")){
			starty = (int) (screenHeight * 0.75); //lower half of screen
			endy = 5;
			System.out.println("Scrolling down");
			
		}
		//System.out.println(startx);
		//System.out.println(starty);
		driver.swipe(startx, starty, startx, endy, duration);
	}
		
	@Test
	public void scrollToBottomToTop() throws Exception{
		
		testLogTitle("Scroll to bottom and top of my programs");
		
		int scrollLimit ;
		
		
		if(screenWidth>screenHeight){
			scrollLimit = 5;
		}
		else{
			scrollLimit = 10;
		}
		
		//signIn("s2","yyyyyy","valid",false,false);
		
		checkToolBarTitle("Build");

		dismissHint();
		
		int posFirst;
		int posNext;
		
		int scrollCount = 0;
		while(true){
			posFirst = driver.findElementByXPath("//android.support.v7.widget.RecyclerView/descendant::android.widget.FrameLayout[@index='0']").getLocation().y;
			scrollVertical("down",200);
			posNext = driver.findElementByXPath("//android.support.v7.widget.RecyclerView/descendant::android.widget.FrameLayout[@index='0']").getLocation().y;
			System.out.println(posFirst);
			System.out.println(posNext);
			scrollCount++;
			try{
				Boolean footer = driver.findElementsById("com.sphero.sprk:id/progress_bar").size()>0;
				if (footer == true){
					System.out.println("Spinner found, waiting until spinner is gone before scrolling again");
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.sphero.sprk:id/progress_bar")));
					//Thread.sleep(5000);
					footer = false;
					scrollCount = 0;
				}
				if (footer == false && scrollCount>scrollLimit){
					System.out.println("End of my programs");
					break;
				}
			}
			catch(Exception e){
				System.out.println("Footer refresh not found");
			}
		}
		
		//Assert that programming tutorial is found
		driver.findElementByName("Programming Tutorial");
		
		while (true){
			scrollVertical("up",200);
			try{
			Boolean header = driver.findElementsByXPath("//android.view.View[@resource-id='com.sphero.sprk:id/swipe_refresh']/android.widget.ImageView").size() > 0 ;
				if (header == true){
					System.out.println("Found refresh icon");
					break;
				}
			}
			catch(Exception e){
				System.out.println("Something went wrong scrolling up");
			}
		}
		//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//android.view.View[@resource-id='com.sphero.sprk:id/swipe_refresh']/android.widget.ImageView")));
		//System.out.println("Refresh icon not visible, at top");
		
		signOut();
		checkToolBarTitle("Build");
	}
@Test(invocationCount=20)
public void scrollToBottom() throws Exception{
		
		testLogTitle("Scroll to bottom and top of my programs");
		
		int scrollLimit ;
		
		if(screenWidth>screenHeight){
			scrollLimit = 5;
		}
		else{
			scrollLimit = 10;
		}
		
		//signIn("i500","yyyyyy","valid",false,false);
		
		checkToolBarTitle("Build");

		dismissHint();
		
		
		int scrollCount = 0;
		while(true){
			scrollVertical("down",200);
			scrollCount++;
			try{
				Boolean footer = driver.findElementsById("com.sphero.sprk:id/progress_bar").size()>0;
				if (footer == true){
					System.out.println("Spinner found, waiting until spinner is gone before scrolling again");
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.sphero.sprk:id/progress_bar")));
					//Thread.sleep(5000);
					footer = false;
					scrollCount = 0;
				}
				if (footer == false && scrollCount>scrollLimit){
					System.out.println("End of my programs");
					break;
				}
			}
			catch(Exception e){
				System.out.println("Footer refresh not found");
			}
		}
		
		//Assert that programming tutorial is found
		driver.findElementByName("Programming Tutorial");
		
		signOut();
		checkToolBarTitle("Build");
	}
	

	
	
	
	
	
	@DataProvider(name = "data-provider")
    public Object[][] dataProviderMethod() {
        return new Object[][] { { "exploreprograms" }, { "media" } };
	}
	
	
	
	@Test
	@Parameters("date")
	public void scrollFeed(String date){
		testLogTitle("Scroll through feed");

		clickNavBar("Home");
		
		int scrollCount = 0;
		Boolean found = false;
		
		while(scrollCount<30){
			scrollVertical("down",400);
			scrollCount++;
			try{
				found = driver.findElementById("com.sphero.sprk:id/tw__tweet_timestamp").getText().toLowerCase().contains(date.toLowerCase());
				if(found==true){
					System.out.println("Twttier post date found");
					break;
				}
			}
			catch(Exception e){
				driver.pressKeyCode(AndroidKeyCode.BACK);
				System.out.println("Date not found");
			}
		}

		//Go to Build
		clickMenu();
		clickMenuItem("build");
		if(found==false){
			Assert.fail("Twitter post date not found");
		}
	}
	
	@Test
	public void Settings(){
		clickNavBar("Home");
		clickTab("Settings");
		
		//check version presence
		driver.findElementById("com.sphero.sprk:id/app_version_text");
		
		List <WebElement> SettingsList = driver.findElementsByXPath("//*[@resource-id='com.sphero.sprk:id/settings_card_view']//android.widget.TextView");
		
		Assert.assertEquals( SettingsList.get(0).getText(), "About");
		Assert.assertEquals( SettingsList.get(1).getText(), "Support");
		Assert.assertEquals( SettingsList.get(2).getText(), "Contact Sphero");
		Assert.assertEquals( SettingsList.get(3).getText(), "Force Firmware Update");
		
		
		//check sleep switch text
		String sleep = driver.findElementById("com.sphero.sprk:id/stay_awake_switch").getAttribute("checked");
		if(sleep.equals("true")){
			Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/stay_awake_switch").getText(), "Sleep While Charging ON");
			driver.findElementById("com.sphero.sprk:id/stay_awake_switch").click();
			Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/stay_awake_switch").getText(), "Sleep While Charging OFF");
		}
		else{
			Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/stay_awake_switch").getText(), "Sleep While Charging OFF");
			driver.findElementById("com.sphero.sprk:id/stay_awake_switch").click();
			Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/stay_awake_switch").getText(), "Sleep While Charging ON");
		}
		
		//driver.findElementById("com.sphero.sprk:id/about_button").click();
		
		//About button
		SettingsList.get(0).click();
		try{
			driver.findElementById("android:id/button_once").click();
		}
		catch(Exception e){
			System.out.println("Just once/Always not found");
		}
		String site = driver.findElementById("com.android.chrome:id/url_bar").getText();
		Assert.assertEquals(site,"https://sphero-staging.cwist.com/about");
		driver.pressKeyCode(AndroidKeyCode.BACK);
		
		//Contact Support
		SettingsList.get(1).click();
		try{
			driver.findElementById("android:id/button_once").click();
		}
		catch(Exception e){
			System.out.println("Just once/Always not found");
		}
		site = driver.findElementById("com.android.chrome:id/url_bar").getText();
		Assert.assertEquals(site,"https://sphero.zendesk.com/hc/en-us");
		driver.pressKeyCode(AndroidKeyCode.BACK);
		
		//Contact Sphero button
		SettingsList.get(2).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/dialog_title").getText(), "Contact Sphero");
		
		List <WebElement> contactList = driver.findElementsById("com.sphero.sprk:id/spinner_text");
		
		Assert.assertEquals( contactList.get(0).getText(), "Orders");
		Assert.assertEquals( contactList.get(1).getText(), "Tech Support");
		Assert.assertEquals( contactList.get(2).getText(), "Education Team Questions");
		clickButton("yes");
	
	}
	
	//@Test(dependsOnMethods={"signIn"})
	public void pullToRefresh(){
		testLogTitle("Refresh gesture");
		/*
		Dimension size = driver.manage().window().getSize(); 
		int startx = (int) (size.width * 0.5); 
		int starty = (int)(size.height * 0.5); 
		int endy = (int) (size.height-1); 
		driver.swipe(startx, starty, startx, endy, 3000);
		driver.swipe(startx, starty, startx, endy, 3000);
		WebElement refreshIcon = driver.findElementByXPath("//android.view.View[@resource-id='com.sphero.sprk:id/swipe_refresh']/android.widget.ImageView");
		System.out.println("Found refresh icon");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//android.view.View[@resource-id='com.sphero.sprk:id/swipe_refresh']/android.widget.ImageView")));
		System.out.println("Refresh icon gone");
		*/
		
		scrollVertical("up",500);
		try{
		Boolean header = driver.findElementsByXPath("//android.view.View[@resource-id='com.sphero.sprk:id/swipe_refresh']/android.widget.ImageView").size() > 0 ;
		if (header == true){
			System.out.println("Found refresh icon");
			}
		}
		catch(Exception e){
				System.out.println("Something went wrong scrolling up");
			}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//android.view.View[@resource-id='com.sphero.sprk:id/swipe_refresh']/android.widget.ImageView")));
		System.out.println("Refresh icon not visible, at top");
	}


	
	public void verifyToolbar(String s){
		String screen = driver.findElementById("com.sphero.sprk:id/toolbar_title").getText().toLowerCase();
		Assert.assertEquals(screen,s.toLowerCase());
		System.out.println(s + " title found");
	}
	
	public void testLogTitle(String s){
		System.out.println("===================START================");
		System.out.println("Test: " + s);
		System.out.println("========================================");
	}
	
	
	
/*
	@Test
	public void Firefox(){
		WebDriverWait waitWeb = new WebDriverWait(web,30);
		web.get("https://www.gmail.com"); 
		
		// Store the current window handle
		String winHandleBefore = driver.getWindowHandle();
		
		web.findElementByXPath(".//*[@id='Email']").sendKeys("ffsqatsignup@gmail.com");
		web.findElementByXPath(".//*[@id='next']").click();
		web.findElementByXPath(".//*[@id='Passwd']").sendKeys("middlefinger");
		web.findElementByXPath(".//*[@id='signIn']").click();
		waitWeb.until(ExpectedConditions.urlContains("https://mail.google.com/mail/#inbox"));
		//web.findElementByXPath("//*[contains(text(), 'Activate your SPRK Lightning Lab Account!')]").click();
		
		//web.findElementByXPath(".//span[text()='Activate your SPRK Lightning Lab Account!']").click();
		web.findElementByXPath("//*[text()='Activate your SPRK Lightning Lab Account!']").click();
		web.findElementByXPath("//a[text()='Activate Account']").click();
		
	
		// Switch to new window opened
		for(String winHandle : web.getWindowHandles()){
		    web.switchTo().window(winHandle);
		}

		// Close the new window, if that window no more required
		web.close();

		// Switch back to original browser (first window)
		driver.switchTo().window(winHandleBefore);
		
		web.findElementByXPath("//*[aria-label='Delete']").click();
	}
*/
	
	



	@AfterClass
	public void tearDown() throws InterruptedException{
		System.out.println("Sleep for 3 seconds then quit");
		Thread.sleep(3000);
		driver.quit();
	}

}