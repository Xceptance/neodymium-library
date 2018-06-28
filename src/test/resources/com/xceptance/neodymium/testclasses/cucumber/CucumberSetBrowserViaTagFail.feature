@SetUpWithBrowserTag
Feature: Set browser via tag

	@Chrome_1024x768 @FF_1024x768
  Scenario: Set browsers
    Given the browser "Chrome_1024x768" is setup

