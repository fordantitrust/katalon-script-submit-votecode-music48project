import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WSBuiltInKeywords
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUiBuiltInKeywords
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.thoughtworks.selenium.Selenium as Selenium
import com.thoughtworks.selenium.DefaultSelenium as DefaultSelenium
import org.openqa.selenium.firefox.FirefoxDriver as FirefoxDriver
import org.openqa.selenium.WebDriver as WebDriver
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium as WebDriverBackedSelenium
import static org.junit.Assert.*
import java.util.regex.Pattern as Pattern
import static org.apache.commons.lang3.StringUtils.join
import com.kms.katalon.core.testdata.CSVData
import com.kms.katalon.core.testdata.InternalData
import java.util.concurrent.TimeUnit;

String resultDir = "C:\\result-vote"

CSVData codeVotes = findTestData('Code')

WebUI.openBrowser('https://akb48-sousenkyo.jp/akb/')

def driver = DriverFactory.getWebDriver()

driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)

String baseUrl = 'https://akb48-sousenkyo.jp/akb/'

selenium = new WebDriverBackedSelenium(driver, baseUrl)

result_vote_ok = new File(resultDir + '\\result-vote-ok.log')
result_vote_already = new File(resultDir + '\\result-vote-already.log')
result_vote_error = new File(resultDir + '\\result-vote-error.log')
result_vote_failed = new File(resultDir + '\\result-vote-failed.log')

for (def index : (0..codeVotes.getRowNumbers() - 1)) {
	
    selenium.click('//li[2]/a/p')
	
    selenium.click('link=チームBIII')
		
    selenium.click('link=MUSIC／ミュージック')

    selenium.click('id=serial1')

    selenium.type('id=serial1', codeVotes.internallyGetValue('s1', index))

    selenium.click('id=serial2')

    selenium.type('id=serial2', codeVotes.internallyGetValue('s2', index))
	
    selenium.click('//div[@id=\'vote\']/a/p')
		
	String pageSource = driver.getPageSource()
	
	String codeVoteGroup = codeVotes.internallyGetValue('s1', index) + codeVotes.internallyGetValue('s2', index)
	
	if(pageSource.contains("投票完了")){ // Completed	
		
		WebUI.takeScreenshot('\\\\' + resultDir + '\\Screenshot\\OK\\' + codeVoteGroup + '.jpg')
		
		def file = new File('\\\\' + resultDir + '\\Text\\OK\\' + codeVoteGroup + '.html')
		
		file.text = pageSource
		
		result_vote_ok.append('\n'+codeVotes.internallyGetValue('s1', index) + "," + codeVotes.internallyGetValue('s2', index))

		
	} else if(pageSource.contains("エラーが発生しました。")){ // An error occurred.	
		
		WebUI.takeScreenshot('\\\\' + resultDir + '\\Screenshot\\Error\\' + codeVoteGroup + '.jpg')
		
		def file = new File('\\\\' + resultDir + '\\Text\\Error\\' + codeVoteGroup + '.html')
		
		file.text = pageSource
		
		result_vote_error.append('\n'+codeVotes.internallyGetValue('s1', index) + "," + codeVotes.internallyGetValue('s2', index))
		
	} else if(pageSource.contains("入力したシリアルナンバーは既に投票済みです。")){ // Already vote
		
		WebUI.takeScreenshot('\\\\' + resultDir + '\\Screenshot\\Already\\' + codeVoteGroup + '.jpg')
		
		def file = new File('\\\\' + resultDir + '\\Text\\Already\\' + codeVoteGroup + '.html')
		
		file.text = pageSource
		
		result_vote_already.append('\n'+codeVotes.internallyGetValue('s1', index) + "," + codeVotes.internallyGetValue('s2', index))
		
	} else { // Other Error
	
		WebUI.takeScreenshot('\\\\' + resultDir + '\\Screenshot\\Failed\\' + codeVoteGroup + '.jpg')
		
		def file = new File('\\\\' + resultDir + '\\Text\\Failed\\' + codeVoteGroup + '.html')
		
		file.text = pageSource

		result_vote_failed.append('\n'+codeVotes.internallyGetValue('s1', index) + "," + codeVotes.internallyGetValue('s2', index))
		
	}	
	
	// Back
	selenium.open("https://akb48-sousenkyo.jp/akb/")
	
}