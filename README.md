Smash-It
========

Smash It is a simple multiplayer brawl game, from 2 up to 4 players battle on a map. But everything is editable with the included editors : your skin and all its animation, the map
When the pc game will be finished, i'll plan to adapt it on mobile devices.

It's coded in Java without any library on a swing JFrame, with custom drawing with Graphics Object.
Currently not all classes are commented, it's what i'm currently working on.

All non-code resources are under CC BY 3.0.

Download
========

You can always download the latest development build : https://www.dropbox.com/s/k7az01r8h3dec8c/launcher.jar
It's an executable Jar file that will auto-install the game and run it, on 1st launch it may take a few minutes.


Known Issues
========

If the game flashes :
1. In your game folder, open the file graphics.opt with your favorite text editor
2. Try to disable anti-aliasing and a lower rendering level ( from 1 to 3 )

New Issues
========

If you have any issues :
1. Run the runnable  jar file from the command line with : java - jar launcher.jar
2. Take a screenshot of the log if there is something
3. Post the issue on Github and describe how your issue happened

Contribute
========

All commits are accepted since they're commented and improve the game.
Any contribution are accepted since they improve something.

TODO
========

* Comment every class
* Add bonus ( weapons, super power, health regeneration )
* Add a configurable number of death, and say it to the user when he's dead
* Code a separate executable jar for the server
* Add a level system with ranks
* Allow the load of a skin in the skin editor
* Add common painting tools in the skin editor ( suh as fill, erase etc)
