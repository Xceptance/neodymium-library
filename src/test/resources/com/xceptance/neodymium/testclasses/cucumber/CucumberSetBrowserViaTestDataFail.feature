Feature: Set browser via test data fail

  Scenario: Set browsers
    Given "Chrome_Not_Found" is open
     Then the browser "Chrome_Not_Found" is setup
