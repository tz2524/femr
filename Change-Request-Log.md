# Change Request Log

## Team: Group 1

txz150730 - Tianxiang Zhang  
zxq150130 - Zengtai Qi (Shane)

## Change Request: FEMR-158

Description:  
If an administrator tries to edit a user, the "Change User Password" input fields are required and a notification gets sent back that the "password field is empty".
Action should only be taken if these fields are filled out - they shouldn't be required to be filled out.

## Concept Location


\# | Description | Rationale
---|---|---
1 | We ran the system. | 
2 | We interacted with the system: after logging in we entered the 'admin' view, and then 'users' tab. Then we entered 'edit' page of one of users. | In order to locate the page of the problem that the change request talks about.
3 | We edited the user’s first name and without enter any character in the two 'Change User Password' text fields. Then we click 'save' button. | In order to reproduce the problem.
4 | After we clicked 'save' button, the save didn’t success, and we got 'password field is empty' alert. | Successfully reproduced the problem.
5 | We switched to IDE then navigated to file: `./app/femr/ui/views/admin/users/edit.scala.html` with IDE project tree view window. | According to common sense of web framework, there should a template file of the edit page. And it turned out there does is a template html file.
6 | We searched keyword 'Change User Password' with IDE 'Find' tool. And located the form code. | If the project doesn’t have internationalization and localization, the form fields labels are possibly hard-coded. And it turned out that form fields labels are hard-coded.
7 | According to the form code we found in template file, we navigated to file `./app/femr/ui/controllers/admin/UsersController.java` method `editPost`.  | The template code indicated that the form submission would be routed to UsersController and editPost method
8 | We tried to find `if` statement on text fields’ text or keywords 'password field is empty' in `editPost` method. | There must be a `if` statement to return some error on the condition password fields are empty.
9 | We use 'Find' tool search keywords 'password field is empty' in the whole fEMR module, there was only one result which points to `validate()` method of class `EditViewModel`. We went to the file via double-clicking find result, and there are several `if` statements on form fields content which is what we had been finding. | Luckily, the ‘password field is empty’ string is also hard-coded.
10 | We made a breakpoint in the first line in `validate()` method of class `EditViewModel`. Then re-ran the project in debug mode. | In order to make sure the `validate()` method of class `EditViewModel` is invoked in edit form submission.
11 | Again we edited the user’s first name and without enter any character in the two 'Change User Password' text fields. Then we click 'save' button.|
12 | The project paused at the breakpoint. Which means `validate()` method of class `EditViewModel` is possibly where we should make modification.
13 | Besides, in the debug view, we track the invoking stack, the invoking chain only contains three internal invocations: the first was Routes (we don’t care), the second was in UsersController (we’d been here), it was the invocation of method `bindFromRequest()` of class `Form<>`, the third was `validate()` method of class `EditViewModel`.
14 | According to what we found out above, we marked these two classes: `UsersController` and `EditViewModel`. | Directly modify `validate()` method of class `EditViewModel` might not be a good decision, because of possible hierarchy and polymorphism. So we also marked class `UsersController`. 

__Time Spent: 120 mins__
__Recorder: Shane Qi__
 
