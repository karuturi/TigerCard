# TigerCard Application

## Assumptions
- Tickets are in the sorted order of date time (just like how one uses a card)
- Application processes all tickets of one user per run
- time interval for peak times is exclusive. Meaning peak interval 10:00 to 11:00 does not include 10:00:00 and 11:00:00

## testing
* Test case be run either from mvn command line or intellij 

```mvn test``` and ```mvn jacoco:report``` will give the test result
![jacoco coverge](images/jacoco.jpg)


* com.rajani.tigercard.TigerCardApplication.main can be used to run from command line

  sample input to process two tickets:
  
  ```
  2
  2007-12-01T10:15:30, ONE, ONE
  2007-12-01T04:15:30, ONE, ONE
  ```
