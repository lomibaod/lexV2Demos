# lexV2Demos
Amazon Lex V2 Demos
This is a sample Lex V2 project that shows off a number of capabilities of Amazon Lex for creating conversational AI
If you don't have an aws account sign up here:
[AWS Free Tier](https://aws.amazon.com/free/)
[AWS Educate if you don't have a credit card](https://www.awseducate.com/Registration)

Once you have an account go here to see the Amazon Lex console
[Amazon Lex Console for managing your chat bots](https://console.aws.amazon.com/lexv2/home?region=us-east-1#bots)

## Sample Bot
A sample lex bot has been provided
```SampleLexV2BotExportJson.zip```
to import the bot into your own account:
1. go to the [Amazon Lex Console](https://console.aws.amazon.com/lexv2/home?region=us-east-1#bots)
2. go to the section that says "Bots"
3. click on Action
4. select Import
5. Under Bot name type "SampleLexV2Bot" (or use whatever name you want)
6. Under Imput file select "Browse file" and select the ```SampleLexV2BotExportJson.zip``` file in this repo
7. there is password on the input file
8. for IAM permissions select "Create a role with basic Amazon Lex permissions"
9. for COPPA section, select "No" (this bot is not subject to the Children's Online Privacy Protection Act)
10. Leave other settings as default and then select "Import"

Once the import is finished, you will see a new bot in the Bots list called "SampleLexV2Bot" (or whatever you called it)

## Java Lambda
### to build from command line/terminal
to build, navigate to the LexV2LambdaDemoJava directory and then run the following command:
  - windows: ```gradlew.bat build```
  - linux/wsl/mac/unix: ``` ./gradlew build```
after "BUILD SUCCESSFUL" the file to upload to AWS Lambda is ```LexV2LambdaDemo-0.1-all.jar```
  - windows: located at ```{repo directory}\LexV2LambdaDemoJava\build\libs\LexV2LambdaDemo-0.1-all.jar```
  - linux/wsl/mac/unix: located at ```{repo directory}/LexV2LambdaDemoJava/build/libs/LexV2LambdaDemo-0.1-all.jar```

### to import into intellij
0. make sure to have [lombok for intellij](https://projectlombok.org/setup/intellij) installed, and annotation processing is enabled (File ->Settings -> search for annotation processors-> check Enable annotation processing)

1. in intelij, select File>New> Project from existing sources...
2. select ``{repo directory}/LexV2LambdaDemoJava/build.gradle```
### to import into eclipse
0. make sure to have [lombok for eclipse](https://projectlombok.org/setup/eclipse) installed, and annotation proccessing is enabled (project properties -> Annotation Processing -> check Enable annotation processing)

1. select File>Import...
2. select Gradle from the list
3. select Existing Gradle project
4. select Next
6. select project root directory by clicking the Browse... button and select the directory LexV2LambdaDemoJava
7. select Gradle Tasks tab, and double click the build task



## Python Lambda


