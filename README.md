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


## Python Lambda


