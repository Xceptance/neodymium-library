Feature: Context data get cleared

  Scenario: Change timeout
    Given Change timeout to 4000

  Scenario: Assert resettet timeout
    Given Assert timeout of 3000
