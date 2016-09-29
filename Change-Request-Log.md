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

__Time Spent: 40 mins__
__Recorder: Shane Qi__
 
