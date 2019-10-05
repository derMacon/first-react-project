# Anki-Pdf-Editor
Commandline tool to create anki flash cards via the vim editor. Once started the programm will display a selected pdf document in which the user can navigate throughout vim itself. If a anki-card should contain a specific pdf page of the displayed document on either the front- or the backside of a note it can be passed in a simplyfied version where the pagenumber is written between tags. 

All features can also be used via shortcuts. To use this programm the user has to paste the configuration of the given [.vimrc](./config/vim-shortcuts.txt) into his own and start the prebuild `.jar` file which is available in the release tab of this repository.

### Resources
* anki api documentattion: [anki-connect](https://foosoft.net/projects/anki-connect/)
* [Blocking queue example](https://www.mkyong.com/java/java-blockingqueue-examples/)
* record screen - `peek`
* stop screenkey - `pkill -f screenkey`

### Todo
* `.projHistory` file overwritten instead of appended
* Test initialization of project structure
* dissable log4j debug messages
* checkstyle

