## Part 2

### FEMR-208: encounter PDF not displaying amount of prescription dispensed

#### change1

```diff
diff -r -u 0_before/femr/app/femr/ui/controllers/PDFController.java 1_after/femr/app/femr/ui/controllers/PDFController.java
--- 0_before/femr/app/femr/ui/controllers/PDFController.java	2016-11-07 16:00:59.000000000 -0600
+++ 1_after/femr/app/femr/ui/controllers/PDFController.java	2016-11-05 12:18:49.000000000 -0500
@@ -439,6 +439,7 @@
 
             for (PrescriptionItem prescription : prescriptionItems) {
 
+                String medicationName =  prescription.getAmount() + " " + prescription.getName();
                 if (prescription.getOriginalMedicationName() != null) {
 
                     //jank way to strikethrough
@@ -449,11 +450,11 @@
 
                     table.addCell(cell);
 
-                    Paragraph replacedMedName = new Paragraph(prescription.getName(), getValueFont());
+                    Paragraph replacedMedName = new Paragraph(medicationName, getValueFont());
                     cell = new PdfPCell(replacedMedName);
                     table.addCell(cell);
                 } else {
-                    Paragraph medName = new Paragraph(prescription.getName(), getValueFont());
+                    Paragraph medName = new Paragraph(medicationName, getValueFont());
                     cell = new PdfPCell(medName);
                     table.addCell(cell);

```

#### change2

```diff
diff -r -u 0_before/femr/app/femr/ui/controllers/PDFController.java 1_after/femr/app/femr/ui/controllers/PDFController.java
--- 0_before/femr/app/femr/ui/controllers/PDFController.java	2016-11-07 18:25:34.000000000 -0600
+++ 1_after/femr/app/femr/ui/controllers/PDFController.java	2016-11-05 11:47:40.000000000 -0500
@@ -438,9 +438,7 @@
             table.completeRow();
 
             for (PrescriptionItem prescription : prescriptionItems) {
-
-                if (prescription.getOriginalMedicationName() != null) {
-
+                    if (prescription.getOriginalMedicationName() != null) {
                     //jank way to strikethrough
                     Chunk strikeThrough = new Chunk(prescription.getOriginalMedicationName(), getValueFont());
                     strikeThrough.setUnderline(0.1f, 3f);   // Thickness, the y axis location of
@@ -448,12 +446,13 @@
                     cell = new PdfPCell(originalMedName);
 
                     table.addCell(cell);
-
-                    Paragraph replacedMedName = new Paragraph(prescription.getName(), getValueFont());
+                    String pdfRMedName =     prescription.getAmount().toString() +"  "+prescription.getName();
+                    Paragraph replacedMedName = new Paragraph(pdfRMedName, getValueFont());
                     cell = new PdfPCell(replacedMedName);
                     table.addCell(cell);
                 } else {
-                    Paragraph medName = new Paragraph(prescription.getName(), getValueFont());
+                    String pdfNmedName =     prescription.getAmount().toString() +"  "+prescription.getName();
+                    Paragraph medName = new Paragraph(pdfNmedName, getValueFont());
                     cell = new PdfPCell(medName);
                     table.addCell(cell);
```

```diff
diff -r -u 0_before/femr/app/femr/ui/models/admin/users/EditViewModel.java 1_after/femr/app/femr/ui/models/admin/users/EditViewModel.java
--- 0_before/femr/app/femr/ui/models/admin/users/EditViewModel.java	2016-11-07 18:25:34.000000000 -0600
+++ 1_after/femr/app/femr/ui/models/admin/users/EditViewModel.java	2016-11-07 18:26:14.000000000 -0600
@@ -48,17 +48,13 @@
             errors.add(new ValidationError("email", "email is a required field"));
         if (!newPassword.equals(newPasswordVerify))
             errors.add(new ValidationError("newPassword", "passwords do not match"));
-        else if(newPassword.isEmpty() || newPasswordVerify.isEmpty())
-            errors.add(new ValidationError("newPassword", "password field is empty"));
-        else {
-            if(newPassword.length() < 6 || !hasUppercase.matcher(newPassword).find()
-                    || !hasNumber.matcher(newPassword).find())      //AJ Saclayan Password Constraints
+        else { if( !newPassword.isEmpty() && (newPassword.length() < 6 || !hasUppercase.matcher(newPassword).find()
+                    || !hasNumber.matcher(newPassword).find()))      //AJ Saclayan Password Constraints
                 errors.add(new ValidationError("newPassword",
                         "password must have at least 6 characters with at least one upper case letter and number"));
         }
         if (roles == null || roles.size() < 1)
             errors.add(new ValidationError("roles", "a user needs at least one role"));
-
         return errors.isEmpty() ? null : errors;
     }
```

#### change3

```diff
diff -r -u 0_before/femr/app/femr/data/daos/system/MedicationRepository.java 1_after/femr/app/femr/data/daos/system/MedicationRepository.java
--- 0_before/femr/app/femr/data/daos/system/MedicationRepository.java	2016-11-07 16:06:46.000000000 -0600
+++ 1_after/femr/app/femr/data/daos/system/MedicationRepository.java	2016-11-07 16:09:16.000000000 -0600
@@ -146,7 +146,9 @@
     public IMedication createNewMedication (String medicationName, List<IMedicationGenericStrength> medicationGenericStrengths, IConceptMedicationForm conceptMedicationForm){
         IMedication medication = null;
         try {
-        if (medicationName == null || medicationGenericStrengths == null || conceptMedicationForm == null) {
+//        if (medicationName == null || medicationGenericStrengths == null || conceptMedicationForm == null) {
+          if (medicationName == null) {
+
             return null;
         }
```
 
```diff
diff -r -u 0_before/femr/app/femr/ui/controllers/PDFController.java 1_after/femr/app/femr/ui/controllers/PDFController.java
--- 0_before/femr/app/femr/ui/controllers/PDFController.java	2016-11-07 16:06:46.000000000 -0600
+++ 1_after/femr/app/femr/ui/controllers/PDFController.java	2016-11-07 16:05:34.000000000 -0600
@@ -438,22 +438,30 @@
             table.completeRow();
 
             for (PrescriptionItem prescription : prescriptionItems) {
+                String medicationForm = prescription.getMedicationForm();
+
+                if (medicationForm == null || medicationForm.equals("")) {
+                    medicationForm = "N/A";
+                } else {
+                    medicationForm = medicationForm.trim();
+                }
+
 
                 if (prescription.getOriginalMedicationName() != null) {
 
                     //jank way to strikethrough
-                    Chunk strikeThrough = new Chunk(prescription.getOriginalMedicationName(), getValueFont());
+                    Chunk strikeThrough = new Chunk(prescription.getAmount() + " " + prescription.getOriginalMedicationName(), getValueFont());
                     strikeThrough.setUnderline(0.1f, 3f);   // Thickness, the y axis location of
                     Paragraph originalMedName = new Paragraph(strikeThrough);
                     cell = new PdfPCell(originalMedName);
 
                     table.addCell(cell);
 
-                    Paragraph replacedMedName = new Paragraph(prescription.getName(), getValueFont());
+                    Paragraph replacedMedName = new Paragraph(prescription.getAmount() + " " + prescription.getName() + " (" + medicationForm + ")", getValueFont());
                     cell = new PdfPCell(replacedMedName);
                     table.addCell(cell);
                 } else {
-                    Paragraph medName = new Paragraph(prescription.getName(), getValueFont());
+                    Paragraph medName = new Paragraph(prescription.getAmount() + " " + prescription.getName() + " (" + medicationForm + ")", getValueFont());
                     cell = new PdfPCell(medName);
                     table.addCell(cell);
```

```diff
diff -r -u 0_before/femr/app/femr/ui/views/history/indexEncounter.scala.html 1_after/femr/app/femr/ui/views/history/indexEncounter.scala.html
--- 0_before/femr/app/femr/ui/views/history/indexEncounter.scala.html	2016-11-07 16:06:46.000000000 -0600
+++ 1_after/femr/app/femr/ui/views/history/indexEncounter.scala.html	2016-11-07 16:05:34.000000000 -0600
@@ -200,7 +200,7 @@
                             @defining(viewModelPharmacy.getPrescriptions.get(x - 1)) { prescription =>
                               <tr>
                                   @if(prescription.getOriginalMedicationName != null) {
-                                      <th><p><del><span>@prescription.getOriginalMedicationName</span></del></p></th>
+                                      <th><p><del><span>@prescription.getAmount @prescription.getOriginalMedicationName</span></del></p></th>
                                       <th> @prescription.getAmount @outputStringOrNA(prescription.getName)
                                           (@outputStringOrNA(prescription.getMedicationForm).toString().trim)
                                           @for(activeDrug <- prescription.getMedicationActiveDrugs) {
```

#### Assessing

Change1 and change2 are pretty similar and straight-forward. These two changes both replace prescription name with a local variable which is a combination of prescription name and prescription amount. Well, they are slightly different because change1 used only one local variable before `if ... else ...` while change2 had two local variables with same value in the `if` and `else` block.

Change3 is different from the other two because it did some more extra changes. Change3 not only added prescription amount, but also added medication form into pdf output. Besides pdf, change3 added prescriptino amount into encounter history web page. And change3 also modified the control flow in `MedicationRepository.java`.

We assumed that these three changes all fixed the issue in the change request, and we have these criteria about the change quality: 

1. How much time the change costs to actualize.
  - Change1 is straight-forward and only has 3 lines of code changed;
  - Change2 also costs little and has 4 lines of code changed;
  - Change3 changed much more lines of code than the other two changes, it definitely costs more time.
  
2. The risks of injecting new bugs as a side-effect of the change.
  - Change1 and change2 is not possible to introduce bug since they didn't make a huge change on the system.
  - Change2 put more risks on the system because it has more lines of code changed and also changed some control flow.
  
3. Whether the change intruduces code bad-smell.
  - Change1 and change3 are okay, not any bad-smell was introduced.
  - Change2 has a small problem since it added two same local virable which can be extracted to a single one. This is kind of a code duplication.
  
In summary, change1 has the highest quality while change3 has the lowest.

### FEMR-137: flag birthdays as being accurate or a guess

#### change1

```diff
diff -r -u 0_before/femr/app/femr/ui/views/triage/index.scala.html 1_after/femr/app/femr/ui/views/triage/index.scala.html
--- 0_before/femr/app/femr/ui/views/triage/index.scala.html	2016-11-07 17:01:11.000000000 -0600
+++ 1_after/femr/app/femr/ui/views/triage/index.scala.html	2016-11-07 16:57:35.000000000 -0600
@@ -60,7 +60,12 @@
                 <div id="ageClassificationWrap">
                     <label>Age<span class="red bold">*</span></label>
                     @inputAge("Age", "Years", "years", "Months", "months", if(viewModel != null) viewModel.getPatient else null)
-                    <input type="hidden" name="isAgeReal" value="" />
+                    @if(viewModel == null) {
+                        <input type="hidden" name="isAgeReal" value="" />
+                    } else {
+                        <input type="hidden" name="isAgeReal" value="@viewModel.getPatient.getIsAgeReal" />
+                    }
+
                     <span class="orSpan">OR</span>
                     @inputDate("Birth Date", "age", if(viewModel != null) viewModel.getPatient.getBirth else null)
                     <span class="orSpan">OR</span>
```

```diff
diff -r -u 0_before/femr/public/js/triage/triage.js 1_after/femr/public/js/triage/triage.js
--- 0_before/femr/public/js/triage/triage.js	2016-11-07 17:01:11.000000000 -0600
+++ 1_after/femr/public/js/triage/triage.js	2016-11-07 16:57:35.000000000 -0600
@@ -293,6 +293,7 @@
         triageFields.patientInformation.firstName.prop('readonly', true);
         triageFields.patientInformation.lastName.prop('readonly', true);
         triageFields.patientInformation.age.prop('readonly', true);
+        triageFields.patientInformation.isAgeReal.prop('readonly', true);
         triageFields.patientInformation.years.prop('readonly', true);
         triageFields.patientInformation.months.prop('readonly', true);
         triageFields.patientInformation.city.prop('readonly', true);
@@ -372,6 +373,7 @@
         firstName: $('#firstName'),
         lastName: $('#lastName'),
         age: $('#age'),//doesn't work for an existing patient
+        isAgeReal: $('#isAgeReal'),
         years: $('#years'),
         months: $('#months'),
         ageClassification: $('[name=ageClassification]'),
@@ -527,8 +529,6 @@
     });
     //birthday shit
     $('#age').change(function () {
-        //set age is guess = 0
-        $('#isAgeReal').val(true);
         var inputYear = $('#age').val().split('-')[0];
         var inputMonth = $('#age').val().split('-')[1] - 1;
         var inputDay = $('#age').val().split('-')[2];
@@ -546,6 +546,7 @@
                 if (diffDay < 0) {
                     ageMonths--;
                 }
+                $('input[name=isAgeReal]').val("true");
                 $('#years').val(Math.floor(ageMonths / 12));
                 $('#months').val(ageMonths % 12);
                 $('#years').css('border', '');
@@ -571,8 +572,7 @@
             var nan = randomString(birthDate);
             if (nan === false) {
                 $('#age').val(birthString);
-                //add is age guess = 1
-                $('#isAgeReal').val(false);
+                $('input[name=isAgeReal]').val("false");
                 $('#years').css('border', '');
                 $('#months').css('border', '');
                 $('#age').css('border', '');
@@ -586,8 +586,7 @@
             var nan = randomString(birthDate);
             if (nan === false) {
                 $('#age').val(birthString);
-                //add is age guess = 1
-                $('#isAgeReal').val(false);
+                $('input[name=isAgeReal]').val("false");
                 $('#years').css('border', '');
                 $('#months').css('border', '');
                 $('#age').css('border', '');
```

#### change2

```diff
diff -r -u 0_before/femr/app/femr/business/services/system/PatientService.java 1_after/femr/app/femr/business/services/system/PatientService.java
--- 0_before/femr/app/femr/business/services/system/PatientService.java	2016-11-07 17:05:30.000000000 -0600
+++ 1_after/femr/app/femr/business/services/system/PatientService.java	2016-11-07 17:03:35.000000000 -0600
@@ -133,6 +133,7 @@
                     savedPatient.getAddress(),
                     savedPatient.getUserId(),
                     savedPatient.getAge(),
+					savedPatient.getFakeBDFlag(),
                     savedPatient.getSex(),
                     null,
                     null,
@@ -162,7 +163,15 @@
         }
 
         try {
-            IPatient newPatient = dataModelMapper.createPatient(patient.getUserId(), patient.getFirstName(), patient.getLastName(), patient.getBirth(), patient.getSex(), patient.getAddress(), patient.getCity(), patient.getPhotoId());
+            IPatient newPatient = dataModelMapper.createPatient(patient.getUserId(), 
+					patient.getFirstName(),
+                    patient.getLastName(),
+                    patient.getBirth(),
+                    patient.getFakeBDFlag(),
+                    patient.getSex(),
+                    patient.getAddress(),
+                    patient.getCity(),
+                    patient.getPhotoId());
             newPatient = patientRepository.create(newPatient);
             String photoPath = null;
             Integer photoId = null;
@@ -178,6 +187,7 @@
                             newPatient.getAddress(),
                             newPatient.getUserId(),
                             newPatient.getAge(),
+							newPatient.getFakeBDFlag(),
                             newPatient.getSex(),
                             null,
                             null,                          
```

```diff
diff -r -u 0_before/femr/app/femr/business/services/system/SearchService.java 1_after/femr/app/femr/business/services/system/SearchService.java
--- 0_before/femr/app/femr/business/services/system/SearchService.java	2016-11-07 17:05:30.000000000 -0600
+++ 1_after/femr/app/femr/business/services/system/SearchService.java	2016-11-07 17:03:35.000000000 -0600
@@ -125,6 +125,7 @@
                     savedPatient.getAddress(),
                     savedPatient.getUserId(),
                     savedPatient.getAge(),
+                    savedPatient.getFakeBDFlag(),
                     savedPatient.getSex(),
                     weeksPregnant,
                     patientHeightFeet,
@@ -198,6 +199,7 @@
                     patient.getAddress(),
                     patient.getUserId(),
                     patient.getAge(),
+                    patient.getFakeBDFlag(),
                     patient.getSex(),
                     weeksPregnant,
                     patientHeightFeet,
@@ -532,6 +534,7 @@
                         patient.getAddress(),
                         patient.getUserId(),
                         patient.getAge(),
+                        patient.getFakeBDFlag(),
                         patient.getSex(),
                         null,
                         null,
@@ -639,6 +642,7 @@
                         patient.getAddress(),
                         patient.getUserId(),
                         patient.getAge(),
+                        patient.getFakeBDFlag(),
                         patient.getSex(),
                         null,
                         null,
```

```diff
diff -r -u 0_before/femr/app/femr/common/IItemModelMapper.java 1_after/femr/app/femr/common/IItemModelMapper.java
--- 0_before/femr/app/femr/common/IItemModelMapper.java	2016-11-07 17:05:30.000000000 -0600
+++ 1_after/femr/app/femr/common/IItemModelMapper.java	2016-11-07 17:03:35.000000000 -0600
@@ -83,6 +83,7 @@
      * @param address            address of the patient, may be null
      * @param userId             id of the user that checked in the patient in triage, not null
      * @param age                age of the patient, may be null
+	 * @param fakeBDFlag         Falg indicating if this is a fake genterated or real birthday
      * @param sex                sex of the patient, may be null
      * @param weeksPregnant      how many weeks pregnant the patient is, may be null
      * @param heightFeet         how tall the patient is, may be null
@@ -100,6 +101,7 @@
                                   String address,
                                   int userId,
                                   Date age,
+                                  Integer fakeBDFlag,
                                   String sex,
                                   Integer weeksPregnant,
                                   Integer heightFeet,
```

```diff
diff -r -u 0_before/femr/app/femr/common/ItemModelMapper.java 1_after/femr/app/femr/common/ItemModelMapper.java
--- 0_before/femr/app/femr/common/ItemModelMapper.java	2016-11-07 17:05:30.000000000 -0600
+++ 1_after/femr/app/femr/common/ItemModelMapper.java	2016-11-07 17:03:35.000000000 -0600
@@ -165,6 +165,7 @@
                                                 String address,
                                                 int userId,
                                                 Date age,
+												Integer fakeBDFlag,
                                                 String sex,
                                                 Integer weeksPregnant,
                                                 Integer heightFeet,
```

```diff
diff -r -u 0_before/femr/app/femr/data/DataModelMapper.java 1_after/femr/app/femr/data/DataModelMapper.java
--- 0_before/femr/app/femr/data/DataModelMapper.java	2016-11-07 17:05:30.000000000 -0600
+++ 1_after/femr/app/femr/data/DataModelMapper.java	2016-11-07 17:03:35.000000000 -0600
@@ -351,7 +351,7 @@
      * {@inheritDoc}
      */
     @Override
-    public IPatient createPatient(int userID, String firstName, String lastName, Date birthday, String sex, String address, String city, Integer photoID) {
+    public IPatient createPatient(int userID, String firstName, String lastName, Date birthday,Integer fakeBDFlag, String sex, String address, String city, Integer photoID) {
 
         if (userID < 0 || StringUtils.isNullOrWhiteSpace(firstName) || StringUtils.isNullOrWhiteSpace(lastName)) {
 
@@ -365,6 +365,8 @@
         patient.setLastName(lastName);
         if (birthday != null)
             patient.setAge(birthday);
+		patient.setFakeBDFlag(fakeBDFlag);
+		
         patient.setSex(sex);
         patient.setAddress(address);
         patient.setCity(city);
```

```diff
diff -r -u 0_before/femr/app/femr/data/IDataModelMapper.java 1_after/femr/app/femr/data/IDataModelMapper.java
--- 0_before/femr/app/femr/data/IDataModelMapper.java	2016-11-07 17:05:30.000000000 -0600
+++ 1_after/femr/app/femr/data/IDataModelMapper.java	2016-11-07 17:03:35.000000000 -0600
@@ -139,13 +139,14 @@
      * @param firstName first name of the patient, not null
      * @param lastName  last name of the patient, not null
      * @param birthday  the patients birthday, may be null
+     * @param fakeBDflag fake generated indicator
      * @param sex       the sex of the patient, may be null
      * @param address   the address of the patients residence, may be null
      * @param city      the city of the patient, may be null
      * @param photoID   the id of a photo of the patient, may be null
      * @return an implementation of IPatient or null if processing fails
      */
-    IPatient createPatient(int userID, String firstName, String lastName, Date birthday, String sex, String address, String city, Integer photoID);
+    IPatient createPatient(int userID, String firstName, String lastName, Date birthday,Integer fakeBDFlag, String sex, String address, String city, Integer photoID);
 
     /**
      * Generate and provide an implementation of IPatientEncounter.
```

```diff
diff -r -u 0_before/femr/app/femr/data/models/core/IPatient.java 1_after/femr/app/femr/data/models/core/IPatient.java
--- 0_before/femr/app/femr/data/models/core/IPatient.java	2016-11-07 17:05:30.000000000 -0600
+++ 1_after/femr/app/femr/data/models/core/IPatient.java	2016-11-07 17:03:35.000000000 -0600
@@ -44,6 +44,8 @@
 
     void setAge(Date age);
 
+ 	Integer getFakeBDFlag();
+    void setFakeBDFlag(Integer fakeBDFlag);    
     String getSex();
 
     void setSex(String sex);
```

```diff
diff -r -u 0_before/femr/app/femr/data/models/mysql/Patient.java 1_after/femr/app/femr/data/models/mysql/Patient.java
--- 0_before/femr/app/femr/data/models/mysql/Patient.java	2016-11-07 17:05:30.000000000 -0600
+++ 1_after/femr/app/femr/data/models/mysql/Patient.java	2016-11-07 17:03:35.000000000 -0600
@@ -40,6 +40,8 @@
     private String lastName;
     @Column(name = "age")
     private Date age;
+    @Column(name = "fakebdflag", nullable = true)
+    private Integer fakeBDFlag;
     @Column(name = "sex", nullable = true)
     private String sex;
     @Column(name = "address", nullable = true)
@@ -60,6 +62,16 @@
 
 
     @Override
+    public Integer getFakeBDFlag() {
+        return fakeBDFlag;
+    }
+
+    @Override
+    public void setFakeBDFlag(Integer fakeBDFlag) {
+        this.fakeBDFlag = fakeBDFlag;
+    }
+
+    @Override
     public int getId() {
         return id;
     }
@@ -104,6 +116,8 @@
         this.age = age;
     }
 
+
+
     @Override
     public String getSex() {
         return sex;
```

```diff
diff -r -u 0_before/femr/app/femr/ui/controllers/PDFController.java 1_after/femr/app/femr/ui/controllers/PDFController.java
--- 0_before/femr/app/femr/ui/controllers/PDFController.java	2016-11-07 17:05:30.000000000 -0600
+++ 1_after/femr/app/femr/ui/controllers/PDFController.java	2016-11-05 11:47:40.000000000 -0500
@@ -438,9 +438,7 @@
             table.completeRow();
 
             for (PrescriptionItem prescription : prescriptionItems) {
-
-                if (prescription.getOriginalMedicationName() != null) {
-
+                    if (prescription.getOriginalMedicationName() != null) {
                     //jank way to strikethrough
                     Chunk strikeThrough = new Chunk(prescription.getOriginalMedicationName(), getValueFont());
                     strikeThrough.setUnderline(0.1f, 3f);   // Thickness, the y axis location of
@@ -448,12 +446,13 @@
                     cell = new PdfPCell(originalMedName);
 
                     table.addCell(cell);
-
-                    Paragraph replacedMedName = new Paragraph(prescription.getName(), getValueFont());
+                    String pdfRMedName =     prescription.getAmount().toString() +"  "+prescription.getName();
+                    Paragraph replacedMedName = new Paragraph(pdfRMedName, getValueFont());
                     cell = new PdfPCell(replacedMedName);
                     table.addCell(cell);
                 } else {
-                    Paragraph medName = new Paragraph(prescription.getName(), getValueFont());
+                    String pdfNmedName =     prescription.getAmount().toString() +"  "+prescription.getName();
+                    Paragraph medName = new Paragraph(pdfNmedName, getValueFont());
                     cell = new PdfPCell(medName);
                     table.addCell(cell); 
```

```diff
diff -r -u 0_before/femr/app/femr/ui/controllers/TriageController.java 1_after/femr/app/femr/ui/controllers/TriageController.java
--- 0_before/femr/app/femr/ui/controllers/TriageController.java	2016-11-07 17:05:30.000000000 -0600
+++ 1_after/femr/app/femr/ui/controllers/TriageController.java	2016-11-07 17:03:35.000000000 -0600
@@ -290,7 +290,8 @@
         if (viewModelPost.getAge() != null) {
             patient.setBirth(viewModelPost.getAge());
         }
-        patient.setSex(viewModelPost.getSex());
+	    patient.setSex(viewModelPost.getSex());
+		patient.setFakeBDFlag(viewModelPost.getFakeBDFlag());
         patient.setAddress(viewModelPost.getAddress());
         patient.setCity(viewModelPost.getCity());
```

```diff
diff -r -u 0_before/femr/app/femr/ui/models/admin/users/EditViewModel.java 1_after/femr/app/femr/ui/models/admin/users/EditViewModel.java
--- 0_before/femr/app/femr/ui/models/admin/users/EditViewModel.java	2016-11-07 17:05:30.000000000 -0600
+++ 1_after/femr/app/femr/ui/models/admin/users/EditViewModel.java	2016-11-07 17:12:58.000000000 -0600
@@ -48,17 +48,13 @@
             errors.add(new ValidationError("email", "email is a required field"));
         if (!newPassword.equals(newPasswordVerify))
             errors.add(new ValidationError("newPassword", "passwords do not match"));
-        else if(newPassword.isEmpty() || newPasswordVerify.isEmpty())
-            errors.add(new ValidationError("newPassword", "password field is empty"));
-        else {
-            if(newPassword.length() < 6 || !hasUppercase.matcher(newPassword).find()
-                    || !hasNumber.matcher(newPassword).find())      //AJ Saclayan Password Constraints
+        else { if( !newPassword.isEmpty() && (newPassword.length() < 6 || !hasUppercase.matcher(newPassword).find()
+                    || !hasNumber.matcher(newPassword).find()))      //AJ Saclayan Password Constraints
                 errors.add(new ValidationError("newPassword",
                         "password must have at least 6 characters with at least one upper case letter and number"));
         }
         if (roles == null || roles.size() < 1)
             errors.add(new ValidationError("roles", "a user needs at least one role"));
-
         return errors.isEmpty() ? null : errors;
     }
``` 

```diff
diff -r -u 0_before/femr/app/femr/ui/models/triage/IndexViewModelPost.java 1_after/femr/app/femr/ui/models/triage/IndexViewModelPost.java
--- 0_before/femr/app/femr/ui/models/triage/IndexViewModelPost.java	2016-11-07 17:05:30.000000000 -0600
+++ 1_after/femr/app/femr/ui/models/triage/IndexViewModelPost.java	2016-11-07 17:03:35.000000000 -0600
@@ -31,6 +31,7 @@
     private String city;
     private Date age;
     private String ageClassification;
+     private Integer fakeBDFlag;
     private String sex;
     public Boolean deletePhoto; //flag to determine if user would like to delete image file
     //begin vitals
@@ -56,6 +57,13 @@
 
 
     private String patientPhotoCropped;
+    public Integer getFakeBDFlag() {
+        return fakeBDFlag;
+    }
+
+    public void setFakeBDFlag(Integer fakeBDFlag) {
+        this.fakeBDFlag = fakeBDFlag;
+    }
 
     public String getPatientPhotoCropped() {
         return patientPhotoCropped;
```

```diff
diff -r -u 0_before/femr/app/femr/ui/views/triage/index.scala.html 1_after/femr/app/femr/ui/views/triage/index.scala.html
--- 0_before/femr/app/femr/ui/views/triage/index.scala.html	2016-11-07 17:05:30.000000000 -0600
+++ 1_after/femr/app/femr/ui/views/triage/index.scala.html	2016-11-07 17:03:35.000000000 -0600
@@ -57,6 +57,28 @@
                 </div>
 
 
+<div class="generalInfoInput">
+
+                            <label>The patient's Real birthday is unknown creating Fake Birthday</label>
+                        @if(viewModel.getPatient.getFakeBDFlag == null) {
+
+                            <input type="radio" name="fakeBDFlag" value="0" /> Real
+                            <input type="radio" name="fakeBDFlag" value="1" checked="true"/> Fake
+
+
+                        } else{
+                            @if(viewModel.getPatient.getFakeBDFlag == 0) {
+                                <input type="radio" name="fakeBDFlag" value="0" checked="true"/> Real
+                                <input type="radio" name="fakeBDFlag" value="1" /> Fake
+                            }  else {''
+
+                            <input type="radio" name="fakeBDFlag" value="0" /> Real
+                            <input type="radio" name="fakeBDFlag" value="1" checked="true"/> Fake
+                            }
+
+                        }
+                     </div>
+
                 <div id="ageClassificationWrap">
                     <label>Age<span class="red bold">*</span></label>
                     @inputAge("Age", "Years", "years", "Months", "months", if(viewModel != null) viewModel.getPatient else null)
@@ -146,7 +168,7 @@
                             </div>
                         </div>
                         <div class="vitalWrap">
-                            <label for="heartRate">Heart Rate</label>
+                            <kelabel for="heartRate">Heart Rate</kelabel>
                             <input type="number" step="number" min="0" class="fInput" id="@viewModel.getVitalNames.get(1).getName" name="@viewModel.getVitalNames.get(1).getName" placeholder="bpm"/>
                         </div>
```

#### change3

```diff
diff -r -u 0_before/femr/app/femr/business/services/system/PatientService.java 1_after/femr/app/femr/business/services/system/PatientService.java
--- 0_before/femr/app/femr/business/services/system/PatientService.java	2016-11-07 17:16:32.000000000 -0600
+++ 1_after/femr/app/femr/business/services/system/PatientService.java	2016-11-07 17:14:20.000000000 -0600
@@ -140,7 +140,8 @@
                     null,
                     photoPath,
                     photoId,
-                    null);
+                    null,
+                    savedPatient.getAgeCalculated());
             response.setResponseObject(patientItem);
 
         } catch (Exception ex) {
@@ -162,7 +163,7 @@
         }
 
         try {
-            IPatient newPatient = dataModelMapper.createPatient(patient.getUserId(), patient.getFirstName(), patient.getLastName(), patient.getBirth(), patient.getSex(), patient.getAddress(), patient.getCity(), patient.getPhotoId());
+            IPatient newPatient = dataModelMapper.createPatient(patient.getUserId(), patient.getFirstName(), patient.getLastName(), patient.getBirth(), patient.getSex(), patient.getAddress(), patient.getCity(), patient.getPhotoId(), patient.getAgeCalculated());
             newPatient = patientRepository.create(newPatient);
             String photoPath = null;
             Integer photoId = null;
@@ -185,7 +186,8 @@
                             null,
                             photoPath,
                             photoId,
-                            null)
+                            null,
+                            newPatient.getAgeCalculated())
             );
         } catch (Exception ex) {
             response.addError("exception", ex.getMessage());
```

```diff
diff -r -u 0_before/femr/app/femr/business/services/system/SearchService.java 1_after/femr/app/femr/business/services/system/SearchService.java
--- 0_before/femr/app/femr/business/services/system/SearchService.java	2016-11-07 17:16:32.000000000 -0600
+++ 1_after/femr/app/femr/business/services/system/SearchService.java	2016-11-07 17:14:20.000000000 -0600
@@ -132,7 +132,8 @@
                     patientWeight,
                     pathToPhoto,
                     photoId,
-                    ageClassification
+                    ageClassification,
+                    savedPatient.getAgeCalculated()
             );
 
             //TODO: why is this being repeated?
@@ -205,7 +206,8 @@
                     patientWeight,
                     pathToPhoto,
                     photoId,
-                    ageClassification
+                    ageClassification,
+                    patient.getAgeCalculated()
             );
 
             // If metric setting enabled convert response patientItem to metric
@@ -539,7 +541,8 @@
                         null,
                         pathToPhoto,
                         photoId,
-                        null
+                        null,
+                        patient.getAgeCalculated()
                 ));
             }
             response.setResponseObject(patientItems);
@@ -646,7 +649,8 @@
                         null,
                         pathToPhoto,
                         photoId,
-                        null
+                        null,
+                        patient.getAgeCalculated()
                 );
 
                 if (patient.getPhoto() != null) {
```

```diff
diff -r -u 0_before/femr/app/femr/common/IItemModelMapper.java 1_after/femr/app/femr/common/IItemModelMapper.java
--- 0_before/femr/app/femr/common/IItemModelMapper.java	2016-11-07 17:16:32.000000000 -0600
+++ 1_after/femr/app/femr/common/IItemModelMapper.java	2016-11-07 17:14:20.000000000 -0600
@@ -91,6 +91,7 @@
      * @param pathToPatientPhoto filepath to the patient photo, may be null
      * @param photoId            id of the patients photo, may be null
      * @param ageClassification  age classification of the patient (adult,child, etc), may be null
+     * @param ageCalculated Indicates that birthday was calculated
      * @return a new PatientItem or null if processing fails, may be null
      */
     PatientItem createPatientItem(int id,
@@ -107,7 +108,8 @@
                                   Float weight,
                                   String pathToPatientPhoto,
                                   Integer photoId,
-                                  String ageClassification);
+                                  String ageClassification,
+                                  String ageCalculated);
 
     /**
      * Generate and provide an instance of PatientEncounterItem
```

```diff
diff -r -u 0_before/femr/app/femr/common/ItemModelMapper.java 1_after/femr/app/femr/common/ItemModelMapper.java
--- 0_before/femr/app/femr/common/ItemModelMapper.java	2016-11-07 17:16:32.000000000 -0600
+++ 1_after/femr/app/femr/common/ItemModelMapper.java	2016-11-07 17:14:20.000000000 -0600
@@ -172,7 +172,8 @@
                                                 Float weight,
                                                 String pathToPatientPhoto,
                                                 Integer photoId,
-                                                String ageClassification) {
+                                                String ageClassification,
+                                                String ageCalculated) {
 
         if (StringUtils.isNullOrWhiteSpace(firstName) ||
                 StringUtils.isNullOrWhiteSpace(lastName) ||
@@ -199,6 +200,7 @@
         if (age != null) {
 
             patientItem.setAge(dateUtils.getAge(age));//age (int)
+            patientItem.setAgeCalculated(ageCalculated);
             patientItem.setBirth(age);//date of birth(date)
             patientItem.setFriendlyDateOfBirth(dateUtils.getFriendlyDate(age));
``` 

```diff
diff -r -u 0_before/femr/app/femr/common/models/PatientItem.java 1_after/femr/app/femr/common/models/PatientItem.java
--- 0_before/femr/app/femr/common/models/PatientItem.java	2016-11-07 17:16:32.000000000 -0600
+++ 1_after/femr/app/femr/common/models/PatientItem.java	2016-11-07 17:18:32.000000000 -0600
@@ -27,6 +27,7 @@
     private String address;
     private String city;
     private String age;//this is a string representing an integer and "YO"(adult) or "MO"(infant)
+    private String ageCalculated; 
     private Integer yearsOld;//the age of the patient as an integer. 0 if the patient is less than a year old
     private Integer monthsOld;
     private Date birth;
@@ -220,4 +221,12 @@
     public void setMonthsOld(Integer monthsOld) {
         this.monthsOld = monthsOld;
     }
+
+    public String getAgeCalculated() {
+        return ageCalculated;
+    }
+
+    public void setAgeCalculated(String ageCalculated) {
+        this.ageCalculated = ageCalculated;
+    }
 }
```

```diff
diff -r -u 0_before/femr/app/femr/data/DataModelMapper.java 1_after/femr/app/femr/data/DataModelMapper.java
--- 0_before/femr/app/femr/data/DataModelMapper.java	2016-11-07 17:16:32.000000000 -0600
+++ 1_after/femr/app/femr/data/DataModelMapper.java	2016-11-07 17:14:20.000000000 -0600
@@ -351,7 +351,7 @@
      * {@inheritDoc}
      */
     @Override
-    public IPatient createPatient(int userID, String firstName, String lastName, Date birthday, String sex, String address, String city, Integer photoID) {
+    public IPatient createPatient(int userID, String firstName, String lastName, Date birthday, String sex, String address, String city, Integer photoID, String ageCalculated) {
 
         if (userID < 0 || StringUtils.isNullOrWhiteSpace(firstName) || StringUtils.isNullOrWhiteSpace(lastName)) {
 
@@ -363,8 +363,10 @@
         patient.setUserId(userID);
         patient.setFirstName(firstName);
         patient.setLastName(lastName);
-        if (birthday != null)
+        if (birthday != null) {
             patient.setAge(birthday);
+            patient.setAgeCalculated(ageCalculated);
+        }
         patient.setSex(sex);
         patient.setAddress(address);
         patient.setCity(city);
```

```diff
diff -r -u 0_before/femr/app/femr/data/IDataModelMapper.java 1_after/femr/app/femr/data/IDataModelMapper.java
--- 0_before/femr/app/femr/data/IDataModelMapper.java	2016-11-07 17:16:32.000000000 -0600
+++ 1_after/femr/app/femr/data/IDataModelMapper.java	2016-11-07 17:14:20.000000000 -0600
@@ -143,9 +143,10 @@
      * @param address   the address of the patients residence, may be null
      * @param city      the city of the patient, may be null
      * @param photoID   the id of a photo of the patient, may be null
+     * @param ageCalculated Indicates that birthday was calculated
      * @return an implementation of IPatient or null if processing fails
      */
-    IPatient createPatient(int userID, String firstName, String lastName, Date birthday, String sex, String address, String city, Integer photoID);
+    IPatient createPatient(int userID, String firstName, String lastName, Date birthday, String sex, String address, String city, Integer photoID, String ageCalculated);
 
     /**
      * Generate and provide an implementation of IPatientEncounter.
```

```diff
diff -r -u 0_before/femr/app/femr/data/models/core/IPatient.java 1_after/femr/app/femr/data/models/core/IPatient.java
--- 0_before/femr/app/femr/data/models/core/IPatient.java	2016-11-07 17:16:32.000000000 -0600
+++ 1_after/femr/app/femr/data/models/core/IPatient.java	2016-11-07 17:14:20.000000000 -0600
@@ -44,6 +44,10 @@
 
     void setAge(Date age);
 
+    String getAgeCalculated();
+
+    void setAgeCalculated(String ageCalculated);
+
     String getSex();
 
     void setSex(String sex);
```

```diff
diff -r -u 0_before/femr/app/femr/data/models/my/Patient.java 1_after/femr/app/femr/data/models/mysql/Patient.java
--- 0_before/femr/app/femr/data/models/mysql/Patient.java	2016-11-07 17:16:32.000000000 -0600
+++ 1_after/femr/app/femr/data/models/mysql/Patient.java	2016-11-07 17:14:20.000000000 -0600
@@ -57,6 +57,8 @@
     private Integer deletedByUserId;
     @Column(name = "reason_deleted", nullable = true)
     private String reasonDeleted;
+    @Column(name = "age_calculated", nullable = true)
+    private Integer ageCalculated;
 
 
     @Override
@@ -185,6 +187,26 @@
     @Override
     public void setReasonDeleted(String reason) { this.reasonDeleted = reason; }
     
-    
+    @Override
+    public String getAgeCalculated() {
+        if (this.ageCalculated == null) {
+            return "Unknown";
+        } else if (this.ageCalculated.intValue() == 0) {
+            return "No";
+        } else {
+            return "Yes";
+        }
+    }
+
+    @Override
+    public void setAgeCalculated(String yesNo) {
+        if (yesNo != null && !yesNo.equals("Unknown")) {
+            if (yesNo.equals("Yes")) {
+                this.ageCalculated = 1;
+            } else {
+                this.ageCalculated = 0;
+            }
+        }
+    }
 
 }
```
 
```diff
diff -r -u 0_before/femr/app/femr/ui/controllers/TriageController.java 1_after/femr/app/femr/ui/controllers/TriageController.java
--- 0_before/femr/app/femr/ui/controllers/TriageController.java	2016-11-07 17:16:32.000000000 -0600
+++ 1_after/femr/app/femr/ui/controllers/TriageController.java	2016-11-07 17:14:20.000000000 -0600
@@ -289,6 +289,7 @@
         patient.setLastName(viewModelPost.getLastName());
         if (viewModelPost.getAge() != null) {
             patient.setBirth(viewModelPost.getAge());
+            patient.setAgeCalculated(viewModelPost.getAgeCalculated());
         }
         patient.setSex(viewModelPost.getSex());
         patient.setAddress(viewModelPost.getAddress());
```

```diff
diff -r -u 0_before/femr/app/femr/ui/models/triage/IndexViewModelPost.java 1_after/femr/app/femr/ui/models/triage/IndexViewModelPost.java
--- 0_before/femr/app/femr/ui/models/triage/IndexViewModelPost.java	2016-11-07 17:16:32.000000000 -0600
+++ 1_after/femr/app/femr/ui/models/triage/IndexViewModelPost.java	2016-11-07 17:14:20.000000000 -0600
@@ -30,6 +30,7 @@
     private String address;
     private String city;
     private Date age;
+    private String ageCalculated;
     private String ageClassification;
     private String sex;
     public Boolean deletePhoto; //flag to determine if user would like to delete image file
@@ -239,4 +240,12 @@
     public void setIsDiabetesScreenPerformed(String isDiabetesScreenPerformed) {
         this.isDiabetesScreenPerformed = isDiabetesScreenPerformed;
     }
+
+    public String getAgeCalculated() {
+        return ageCalculated;
+    }
+
+    public void setAgeCalculated(String ageCalculated) {
+        this.ageCalculated = ageCalculated;
+    }
 }
```

```diff
diff -r -u 0_before/femr/app/femr/ui/views/triage/index.scala.html 1_after/femr/app/femr/ui/views/triage/index.scala.html
--- 0_before/femr/app/femr/ui/views/triage/index.scala.html	2016-11-07 17:16:32.000000000 -0600
+++ 1_after/femr/app/femr/ui/views/triage/index.scala.html	2016-11-07 17:14:20.000000000 -0600
@@ -59,9 +59,20 @@
 
                 <div id="ageClassificationWrap">
                     <label>Age<span class="red bold">*</span></label>
+
                     @inputAge("Age", "Years", "years", "Months", "months", if(viewModel != null) viewModel.getPatient else null)
                     <span class="orSpan">OR</span>
                     @inputDate("Birth Date", "age", if(viewModel != null) viewModel.getPatient.getBirth else null)
+                    <div class="generalInfoInput">
+
+                        <label>Auto-Calculated</label>
+                        @if(viewModel.getPatient.getAgeCalculated == null) {
+                            <input type = "text" name="ageCalculated" class="fInput" id="ageCalculated" value="Unknown" readonly />
+                        } else {
+                            <input type = "text" name="ageCalculated" class="fInput" id="ageCalculated" value="@viewModel.getPatient.getAgeCalculated" readonly />
+                        }
+                    </div>
+
                     <span class="orSpan">OR</span>
                     <div class="generalInfoInput">
                         <div id="classificationRadioWrap">
```

```diff
Only in 1_after/femr/conf/evolutions/default: 99.sql
+# --- !Ups
+
+
+ALTER TABLE `patients`
+ADD COLUMN `age_calculated` INT(1) NULL DEFAULT NULL AFTER `reason_deleted`;
+
+# --- !Downs
```

```diff
diff -r -u 0_before/femr/public/js/triage/triage.js 1_after/femr/public/js/triage/triage.js
--- 0_before/femr/public/js/triage/triage.js	2016-11-07 17:16:32.000000000 -0600
+++ 1_after/femr/public/js/triage/triage.js	2016-11-07 17:14:20.000000000 -0600
@@ -372,6 +372,7 @@
         firstName: $('#firstName'),
         lastName: $('#lastName'),
         age: $('#age'),//doesn't work for an existing patient
+        ageCalculated: $('#ageCalculated'),
         years: $('#years'),
         months: $('#months'),
         ageClassification: $('[name=ageClassification]'),
@@ -409,6 +410,7 @@
 
         if (!patientInfo.years.val() && !patientInfo.months.val()) {
             patientInfo.age.val(null);
+            patientInfo.ageCalculated.val("Unknown");
             patientInfo.years.css('border', '');
             patientInfo.months.css('border', '');
             return false;
@@ -437,6 +439,7 @@
                 pass = false;
             }
             else {
+
                 patientInfo.months.val(checkMonths);
                 patientInfo.months.css('border', '');
             }
@@ -549,17 +552,21 @@
                 $('#years').css('border', '');
                 $('#months').css('border', '');
                 $('#age').css('border', '');
+                $('#ageCalculated').val("No")
             }
             else {
                 $('#age').css('border-color', 'red');
                 $('#years').val(null);
                 $('#months').val(null);
+                $('#ageCalculated').val("Unknown")
             }
         }
         else {
             $('#age').css('border', '');
             $('#years').val(null);
             $('#months').val(null);
+            $('#ageCalculated').val("Unknown")
+
         }
     });
     $('#years').change(function () {
@@ -572,6 +579,7 @@
                 $('#years').css('border', '');
                 $('#months').css('border', '');
                 $('#age').css('border', '');
+                $('#ageCalculated').val("Yes")
             }
         }
     });
@@ -585,6 +593,7 @@
                 $('#years').css('border', '');
                 $('#months').css('border', '');
                 $('#age').css('border', '');
+                $('#ageCalculated').val("Yes")
             }
         }
     });
```
