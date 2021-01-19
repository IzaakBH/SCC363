All work should be performed on branches corresponding to issue IDs in Jira.
main is the current production version.
dev is the development branch which is most up to date. To work on a feature, create a branch off dev named with the ID
of the issue youre working on in Jira. e.g. Issue SCC363-10 Create login UI should be on a branch called SCC363-10. Once
the issue is completed (issues should take no longer than a day to complete), merge the branch back into dev.

If an issue is too complex to complete in a short time frame, create new issues on the board and assign them to the current
sprint. These can then be implemented on their own branches.

To start the spring localhost on your machine, run (Linux/OSX) -> './mvnw spring-boot:run' (Windows) -> 'mvnw spring-boot:run'
