All work should be performed on branches corresponding to issue IDs in Jira.
main is the current production version.
dev is the development branch which is most up to date. To work on a feature, create a branch off dev named with the ID
of the issue youre working on in Jira. e.g. Issue SCC363-10 Create login UI should be on a branch called SCC363-10. Once
the issue is completed (issues should take no longer than a day to complete), merge the branch back into dev.

If an issue is too complex to complete in a short time frame, create new issues on the board and assign them to the current
sprint. These can then be implemented on their own branches.

To start the spring localhost on your machine, run (Linux/OSX) -> './mvnw spring-boot:run' (Windows) -> 'mvnw spring-boot:run'

Steps to run the spring server on your local machine for testing and marking purposes - 
1. extract the folder from the zip file.
2. open the project folder in Intellij
3. in the top right hand corner click on 'Add Configuration...'
4. There is a + sign that you should click and Application should appear in the list 
5. Add the Application 
6. Add the main file (Controller)
7. Click apply
8. Click ok
