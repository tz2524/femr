## Group 1

`zxq150130` Shane Qi  
`txz150730` Tianxiang Zhang

## Automated Refactoring

### 1. Internal duplication in `ResearchService.java`

This class is flagged with internal duplicate smell because these 3 methods have duplication code with each other:

`buildVitalResultSet(List encounters, ResearchFilterItem filters): ResearchResultSetItem`  
`buildHeightResultSet(List encounters, ResearchFilterItem filters): ResearchResultSetItem`  
`buildAgeResultSet(List encounters, ResearchFilterItem filters): ResearchResultSetItem`  

1. We used a diff inspecting tool 'FileMerge' to find out the duplicated code.  
![](./internal-duplicate.png)

2. We selected those duplicated code and tried 'extract method' operation of the IDE, but it failed. The error message was: 'Cannot perform refactoring. Selected block should present a set of statements or an expression'. So that we decided to do the refactoring manually.

3. We created a private method `private void getSecondaryData()`, copied the duplicated code and pasted code into the new-created method.

4. There were many 'unresolved symbol' errors because many variables were local variables.

5. We manually extracted those unresolved symbol variables to the method's parameter.

6. Finally the method turned out to be `private void getSecondaryData(ResearchResultSetItem resultSet, ResearchFilterItem filters, ResearchResultItem resultItem, IResearchEncounter encounter)`.

7. We replaced duplicated code with the new extracted method.

8. We ran test cases, and all test cases passed.

9. We run Incode again, one of the three bad smell went away. We inspected the rest two methods, their duplicated code is variable declaring which is unavoidable.

10. We asserted that the refactoring succeed.

## Manual Refactoring

### 1. Feature envy in `LocaleUnitConverter.java`

1. We inspected the method `public static PatientItem toMetric(PatientItem patient)`. The problem was that the method accessed the data of a `PatientItem` object a lot.

2. We decided move the operations of the method into `PatientItem.java`.

3. We cut the whole method and pasted it into `PatientItem.java`.

4. We removed `static` symbol, changed the method return type to `void`, removed `return` statement and delete the parameter. Because all the operations in the method would be performed on the object itself.

5. There appeared many 'unresolved symbol' errors because we removed parameter, and there were many external invocations on the parameter variable. We changed all the external invocations to internal invocations, eg: changing `patient.getHeightFeet()` to `getHeightFeet()`.

6. We delete the `null` handling code since the object can't be `null` anymore.

7. We found out usages of the methods and replaced them all with the new extracted method.

8. We ran test cases, and all test cases passed.

9. We run Incode again, the bad smell went away so that we asserted that the refactoring succeed.
