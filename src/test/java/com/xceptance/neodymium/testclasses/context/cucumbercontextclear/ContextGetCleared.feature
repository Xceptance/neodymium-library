Feature: Context data get cleared

  Scenario: Change timeout
    Given Change timeout to 4000

  Scenario: Assert resettet timeout
    Given Assert timeout of 3000

  Scenario: Change collection timeout
    Given Change default collection timeout to 1234

  Scenario: Assert resettet collection timeout
    Given Assert collection timeout of 6000
