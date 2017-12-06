Feature: try  some cucumber on final project
  As a developer 
  I want to see examples of test cases in project
  
Scenario: set username and pass for an Account 
  Given I have an Account
  And I set user name as zhiyuan
  And I set password as li
  Then the account should have the user Name as zhiyuan and password as li
  
  
  
  