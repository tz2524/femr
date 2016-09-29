# Change Request Log

## Team: Group 1

txz150730 - Tianxiang Zhang  
zxq150130 - Zengtai Qi (Shane)

## Change Request: FEMR-208

Description:  
The generated PDF is not displaying the amount of a prescription that was dispensed.  

1. Create a new patient in Triage
2. Prescribe them some medications in Medical. Make sure the amount is > 0
3. Dispense the prescriptions in Pharmacy (click Submit)
4. View the patients encounter summary by pulling their ID up in Triage and clicking "Patient History" on the bottom.
5. Select the encounter where the prescription was dispensed on the right. The dispensed prescription is displayed here with the amount.
6. Click the red "Generate PDF" button. The dispensed prescription is displayed here, but without the amount.

## Concept Location


\# | Description | Rationale
---|---|---
1 | We ran the system. | 
2 | According to steps in the issue description (also refered to TA's email), we reproduced the problem in the localhost website. | In order to locate the page of the problem that the change request talks about.
3 | According to the page uri (`history/encounter/####`), we located the 'Generate PDF' button in file: `./app/femr/ui/views/history/indexEncounter.scala` | In order the find out which class in responsive for generating pdf.
4 | We looked into the html, find out this button goes into method `Result index(int encounterId)` of class `PDFController`. | In order to find out code about generating PDF.
5 | Method `Result index(int encounterId)` does an internal call to method `byte[] buildPDF(int encounterId)`, we went to this private method via shorcut 'Command + Left Click' | We wanted to track back to those code that generates directly.
6 | Method `byte[] buildPDF(int encounterId)` makes a lot of internal calls, we decided to use 'Find' tool with keywords 'Origin' (table head of generated pdf). | According to experience of change request 1, keywords in pdf are possibly hard-coded.
7 | We successfully find out the code of generating pdf in the method `PdfPTable getAssessments(TabFieldMultiMap tabFieldMultiMap, List<PrescriptionItem> prescriptionItems , List<ProblemItem> problemItems)` of class `PDFController`. And we marked this class and method. |
8 | We looked into this method and found out that this method is only responsible for generating part of the pdf. We recorded this information for future reference. | 

__Time Spent: 40 mins__
__Recorder: Shane Qi__
 
## Impact Analysis

Use the table below to describe each step you followed when performing impact analysis for this change request. Include as many details as possible, including why classes are visited or why they are discarded from the ones that have to change.
Do not take the impact analysis of your changes lightly. Remember that any small change in the code could lead to large changes in the behavior of the system. Follow the process on impact analysis covered in the class. Describe in details how you followed this process in the change request log. Provide details on how and why you finished the impact analysis process.
 
\# | Description | Rationale
---|---|---
1 | We made a breakpoint around code of generating prescription items table and generated a pdf again. | In order to know how the button click event goes all the way to the code, and how many classed/methods are involved.
2 | According to debug tool, we found out there are four methods and three classes in the chain. (we ignored those classes that is called before Routes class) | In order to have a list of what could be impacted by the change. And classes called before Routes class are about the framework which we don't have to pay attention.
3 | |

 Time spent (in minutes): 33
