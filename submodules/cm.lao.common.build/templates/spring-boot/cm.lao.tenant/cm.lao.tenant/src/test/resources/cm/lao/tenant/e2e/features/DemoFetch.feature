Feature: Tenant Fetching

  Scenario: Fetch all Demos
    Given   I am connected as user 'user' with the scope 'demo:read:all'
    And     I assume that the demos with the following data are inside the database
      | id                                   | name   |
      | d91722a8-8e47-43ae-8032-49affbb68065 | demo_1 |
      | 242f22ff-6470-490e-9bb2-d72c6505d093 | demo_2 |
      | 4d1503ba-312b-40c3-aa0e-8f0736b0fc49 | demo_3 |
    When    I fetch all demos using the extraction code 'fieldsToExtract_1'
    Then    I should see that the following data are among the fetched ones
      | id                                   | name   |
      | d91722a8-8e47-43ae-8032-49affbb68065 | demo_1 |
      | 242f22ff-6470-490e-9bb2-d72c6505d093 | demo_2 |
    And     I should see that the following data are not among the fetched ones
      | id                                   | name   |
      | 4d1503ba-312b-40c3-aa0e-8f0736b0fc49 | demo_3 |

  Scenario: Fetch all tenants without the necessary permission
    Given   I am connected as user 'user' with the scopes ''
    And     I assume that the demos with the following data are inside the database
      | id                                   | name   |
      | d91722a8-8e47-43ae-8032-49affbb68065 | demo_1 |
      | 242f22ff-6470-490e-9bb2-d72c6505d093 | demo_2 |
    When    I try to fetch all demos using the extraction code 'fieldsToExtract_1'
    Then    The last request failed with the http status 'FORBIDDEN' and error code ''
    
