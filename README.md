# Balance the Loan Books

## Build from Source Code
Make sure you have installed [Gradle](https://gradle.org/install/) first, and JDK 1.8 or higher.   

Then run  
`$ ./gradlew build`  

- Folder *small* are localed in *src/test/resources*,
which are used test resource to valid the code.
- You can view the test report by opening the HTML output file, located at build/reports/tests/test/index.html.

## Run the Application.
`$ ./gradlew run`

- Folder *large* are localed in *src/main/resources*,
which are used as application input.
- Output files will be written the root directory, 
with names *assigments.csv* and *yields.csv*. 

## Write-Up Questions
1. How long did you spend working on the problem? What did you find to be the most difficult part?  
3h for the basic coding. Details are hard to make decisions, without being able to talk to others. 
Like how and when to round decimals, how to persist data in real life.

2. How would you modify your data model or code to account for an eventual introduction of new, 
as-of-yet unknown types of covenants, beyond just maximum default likelihood and state restrictions?  
More covenants can be added to the `Covenants.java` and change the condition in `isSatisified()`.
These are the only two places that needs to be changed, unless the input data format changed, 
or there is a needs to optimize the time efficiency of the new filter logic.

3. How would you architect your solution as a production service wherein new facilities can be introduced at arbitrary points in time. 
Assume these facilities become available by the finance team emailing your team and describing the addition with a new set of CSVs.  
Both new `facilities.csv` files and new `covenants.csv` files can be added by 
`FacilityRepository.addFacilities(File facilityFile)` and `FacilityRepository.addCovenants(File covenantFile)` in time.
There is no other dependency between two of them.

4. Your solution most likely simulates the streaming process by directly calling a method in your code 
to process the loans inside of a for loop. What would a REST API look like for this same service? 
Stakeholders using the API will need, at a minimum, to be able to request a loan be assigned to a facility, 
and read the funding status of a loan, as well as query the capacities remaining in facilities.  
Add the following APIs, details in code `StakeholderApi.java`
- `assignLoan` to request a loan be assigned to a facility.
- `getFundingStatus` to read the funding status of a loan.
- `getCapacity` to query the capacities remaining in facilities.

