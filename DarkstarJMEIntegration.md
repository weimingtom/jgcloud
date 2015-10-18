# Introduction #
As part of my gaming project, I wanted to get JME interacting with a Darkstar server for a multiplayer game. Now that I've finished this part, I thought I'd share with you how I did it... Hope it helps !

This wiki is intended as a step-by-step runthrough of what you need, the code itself, and how to test it. I appreciate it's pretty large and will take you a few hours (probably) to complete. I would suggest taking your time and understand each piece before proceeding. Netbeans knowledge is a big advantage here.

The intended audience is those reasonably proficient in JME, but with little or no Darkstar experience.

I myself am a newbie(ish) in both technologies, so I'd appreciate any feedback on the code (my e-mail's at the top).

---

# Getting Started #

## Required Software ##
| Java 1.6 | http://java.sun.com/javase/downloads/index.jsp |
|:---------|:-----------------------------------------------|
| JMonkeyEngine | http://code.google.com/p/jmonkeyengine/downloads/list |
| Darkstar Server (binary) | http://projectdarkstar.com/current-distribution.html |
| Darkstar Client (binary) | http://projectdarkstar.com/current-distribution.html |
| Netbeans 6.7.1 (Java SE) | http://netbeans.org/downloads/                 |
| SVN Client | eg http://www.open.collab.net/downloads/subversion.html |
| Ant (optional) | http://ant.apache.org/bindownload.cgi          |
| Project Source Code | Check out via SVN (see below)                  |

This is what I have, and I'm going to assume that you have the same. It's definitely possible to get buy with just Java, JME and Darkstar server/client, but I would have to write a lot more to explain the process!

I'm guessing you already have Java 1.6 and JME on your PC ;)

## Darkstar Installation ##
The Darkstar binaries are just zip files. Could you please extract them to C:\Darkstar. So you should have something like this:

  * c:\Darkstar\sgs-server-dist-0.9.11
  * c:\Darkstar\sgs-client-dist-0.9.11

## Netbeans Installation ##
This took a while for me because it wants to make a few additional updates afterwards. It's an exe, so follow it through and it'll get there eventually! It asks you at the end if you want to provide feedback. That's a bit more registration I think, and it's not mandatory.

One thing I should mention is that you will run in to trouble if you're on a 64-bit machine. You're going to need a 32-bit Java 1.6 JDK installation (but I bet you knew this if you're using JME). Once you've checked out the source code (see below), you will have to use this compiler instead.

## SVN Client Installation ##
I'm using the Collabnet client, but I think there are others out there. It's just the client (not the server) that you'll need to check out my code from Google. Again, it's just a standard exe installation. It does ask you to try a few other apps at the end. You don't need to bother (for now anyway).

## Ant Installation ##
Not essential (yet), as Netbeans has its own distribution, but will be useful if/when you want to start building the project outside of an IDE.

## Source Code ##
Provided you have downloaded the SVN client successfully, you should be able to check out the code. Make a _root_ directory to put the code (eg c:\svn-source), load up a command-prompt, `cd` in to that directory, and then type:
```
svn checkout http://jgcloud.googlecode.com/svn/tags/release-0.07 darkstar-jme-example
```
With a bit of luck, you should start seeing files go in to the `darkstar-jme-example` folder. These are the two Netbeans projects, and you should be able to load them up in Netbeans now. Note that they are two separate Netbeans projects.

Depending on where you stored your Darkstar and JME libraries, you will need to go in to each project and clean up the links. Do this by clicking on the project, and selecting "Properties". Now click on the "Libraries" link. You will need to remap each JME and Darkstar library to their home on your computer. Do this for both projects.

Also, for the DarkstarJME project, go in to the "Run" box and change the VM options as needed.

**NOTE** If your version of Darkstar **is not** 0.9.11, edit `TestDarkstarServer/nbproject/project.properties` file and edit the `darkstar.home.dir` property.
## We're Ready to Go! ##
Hopefully that wasn't too painful. Now let's look at the code...

---

# Code Review #
## The Darkstar Server ##

Firstly, let's talk about the what the server is going to do. Please note I use client and player interchangably here:

  * Server starts up.
  * Create a channel to handle client locations.
  * Accept logins from remote clients.
  * After client login, join them to the channel for notification of remote client locations.
  * Listen for clients sending their location information to the server.
  * Send this information back out on the channel, effectively letting everyone know this clients location.

A channel in Darkstar is a convenient way of grouping up clients to make it easy to send messages in one go. I have created a channel called PLAYER\_LOCATIONS, which is always referenced via the `PLAYER_LOCATIONS_CHANNEL` static String.

On startup of the server, it's the `TestDarkstarAppListener` that starts us off. Here is a rough guide to what is going on:

  * `initialize(...)` is the first thing the Darkstar server calls when it starts up. Note that it only runs this the first time it starts this server instance up. Subsequent runs use the persistence model to restore any objects. In our `initialize`, we create a channel (the `PLAYER_LOCATIONS_CHANNEL`) and create a reference to it. In Darkstar, you never (nearly never?) pass actual objects around. You pass a `ManagedReference` to that object. My basic understanding of this is that it makes it easier to share among multiple threads, and doesn't tinker with the persistence model.

  * `loggedIn(ClientSession session)` as the name suggests is what happens after the client has logged in (don't worry, we will get to the client soon!). In our implementation, it adds our newly logged-in client to the `PLAYER_LOCATIONS_CHANNEL`. Each client has it's own `ClientSessionListener`, which performs a few standard tasks. I have used the `DarkstarClientSessionListener` class for this, but it doesn't do much, other than listen for non-channel messages (which we don't send) and disconnects... So please ignore for now. To be honest, I should have got `TestDarkstarAppListener` to implement the `ClientSessionListener` interface as well. I will do next time !

  * `receivedMessage(Channel channel, ClientSession clientSession, ByteBuffer message)` is important, so pay attention ;) This is what gets called when our client sends us a message on the `PLAYER_LOCATIONS_CHANNEL`. This method needs to handle all channels, but we only have one right now. All it does is take the message sent from the client, and send it back to all parties on the same channel. This includes the client that sent the message in the first place. That means we'll need to think about that later, as the receiving client will want to ignore messages sent from itself (in this case anyway).

  * `decodeMessage(...)` A convenience method that allows us to view the message sent from the client if we want (The message comes down as a `ByteBuffer`). In our example, we're just bouncing the message untouched back to the clients on the channel, so this isn't used (unless we turn the logging level up).

So the server will start up, do some initialization, and create the locations channel. It then listens for new clients and add them to the locations channel straight away. Any location updates from any of the clients is thrown back down the same channel to **all** clients (reiterating that this includes the client who sent the message).

This should be all we need. So let's build it. Peform the following actions:

  * Right-click on the "TestDarkstarServer" and click "Clean and Build".
  * Check the output window. As well as compiling, you should see it copying the config files in to the right locations.
  * Now go to your Darkstar home (eg `c:\darkstar`) and double-click the `startDarkstarTestServer.bat` file. It should now be running... Hooray!

You can use Ctrl+C to terminate the job. Clicking the `startDarkstarTestServer.bat` again will restart it, but this time it won't run the `initialize(...)` method, as it has stored all the bits (ie the channel) via persistence. It also throws a nasty exception, which I don't understand (**please let me know if you do!**) but does still load up.


## The JME Client ##

This code is a little more involved, so I'd like to get a few of the basic classes summarised:

  * `TankController` is a standard keyboard/mouse `Controller` class for my tank node. Nothing special here, standard implementation.
  * `PlayerDetails` is a bean that represents the location/rotation of a remote player. The idea is that the `DarkstarUpdater` thread (discussed below) creates an instance of this as each player location arrives and places it on a queue for processing. It will then be the job of the main GPL thread to position our remote players in the scene.
  * `DarkstarConstants` contains some default values and such that the `DarkstarUpdater` will use. One important constant is `DARKSTAR_SERVER_HOST`. You will want to set this to the IP address where you're running the Darkstar server. If you're going to do everything on one machine, then set it to `localhost`. I'm lucky and have a laptop so had the server running on that and a couple of JME clients running on my main PC.

Good, so that only leaves us with `TankGameState` and `DarkstarUpdater` left. Phew!

### DarkstarUpdater ###

I'd like to discuss this one first, because although it gets created and started by the `TankGameState`, it is doing the grunt work of communicating to/from the server.

It was intended to be a singleton class (well, it still is) but I've ended up needing to pass parameters to it first time round. Therefore, it has two `getInstance(...)` methods. One that you call the first time `getInstance(Node myTank)` which does the initialization. Subsequent calls are done via `getInstance()`

There's a bit of bouncing to/from the server on login. Our class calls `simpleClient.login(...)` with the host and port set as properties. Provided we're connected, the server then calls `getPasswordAuthentication()` and we are required to return a `PasswordAuthentication` object, which is basically just a username and password bean. If all has gone well, then the server calls `loggedIn()`. We will also get a message telling us we've joined the `PLAYER_LOCATIONS_CHANNEL`.

If it failed to login, then the server will call `loginFailed(String reason)`. This is also called if the `simpleClient.login(...)` couldn't get a connection to the server in the first place.

So assuming we're connected, I update a boolean to let the thread know it can do its stuff. If you look at the `run()` method, you'll see that I do a check to ensure we're logged in and connected to the `PLAYER_LOCATIONS_CHANNEL`. If we are, then we create a String that is a key/value pair with player-name, location, rotation and client time. And we send that off to the channel via the `send(encodeString(message))` method. We have to encode the message as a byte strem, which is why we call the `encodeString(message)`. This busily chugs along, sending location messages to the server, sleeps and then repeats.

Great, so we're letting the server know where we are. But... We want to know where other people are too. As explained above, we're definitely going to get messages back on the channel because the server sends back location messsages to ALL clients on the channel INCLUDING the client that sent it (if you think I haven't mentioned this before, then you're not reading hard enough!).

It's the `receivedMessage(ClientChannel clientChannel, ByteBuffer message)` method that handles channel messages back from the server. Because there's only one channel right now, we know it's our `PLAYER_LOCATIONS_CHANNEL`. We also know the format of the message, as it's consistent (and we just sent one!). So I pull that message back again in to a readable key/value pair `Map` via the `parseMessage(...)` method. I then turn this in to a `PlayerDetails` object. This is a nice representation of a location for our purposes. Finally, we simply add this new information to a static queue (`playerDetailsQueue`) that our JME client can retrieve as and when it's ready. Note that we DON'T do this if the location information was from the same client that sent it.

Some astute readers will notice that I'm not using the JME `GameTaskQueueManager`. I am pretty confident this approach reads better and is actually a more loosely-coupled approach. You may disagree... Please let me know!

And that is our `DarkstarUpdater` in a nutshell.

## TankGameState ##
This (you'll be pleased to hear) is not going to take long. I am not going to talk about any of the scene construction here, as I'm assuming you're up to speed on that. Take a look at the `init()` method to see the pieces that make up the JME scene.

I want to talk about the piece that handles placing and moving remote clients in our scene. Check the `TankGameState()` constructor. You'll notice that it starts up our `DarkstarUpdater` thread. Good. So as long as things have gone well, we'll be communicating with the server sending our location. We may also be getting `PlayerDetails` objects placed on the `DarkstarUpdater.playerDetailsQueue`. Therefore, in the `update(...)` method, you'll see a call to `updateRemotePlayerLocations()`. This method polls the queue, once per frame. If it finds a new `PlayerDetails` object (it will only do one per frame) then it's got some work to do. Firstly, it checks to see if that player is already in our `remotePlayersNode` node. If it isn't, then it's new, and we'll create a new tank and place it on the `remotePlayersNode`. Once that's out of the way, we can position the tank as per the information in the `PlayerDetails` object. Nice!

So you see that the `TankGameState` class isn't doing much new other than updating the remote players locations. I hope you'll agree that this is the right approach with most of the grunt work being done by our `DarkstarUpdater` thread.

---

# Testing #
We've come a long way, and we should now be ready to test this all out. Excited? Terrified? Confused? Good. Here's a run-down of what you need to do.

**TestDarkstarServer Project:**
  1. Clean and Build the TestDarkstarServer project (if you haven't already). You should now have the following files in the directories (where PDS\_HOME is your Darkstar server root directory):
    1. PDS\_HOME: startDarkstarTestServer.bat
    1. PDS\_HOME/tutorial: TestDarkstarServer.jar
    1. PDS\_HOME/tutorial/conf: TestDarkstarServer.boot
    1. PDS\_HOME/tutorial/conf: TestDarkstarServer.properties
  1. Run the `startDarkstarTestServer.bat` file which will be in your Darkstar root directory.

**DarkstarJME Project:**
  1. update `DarkstarConstants.DARKSTAR_SERVER_HOST` to point to the IP where Darkstar is running. If it's on the same box, then `localhost` should work for you.
  1. Clean and build the DarkstarJME project.
  1. Right-click the DarkstarJME project and click "Run".
  1. If everything has gone OK, you should see a tank and an arena. Use the arrow keys to move around.
  1. Alt-tab back to Netbeans. Check the logging. You should see a `Login successful` and `Joined Channel PLAYER_LOCATIONS` message saying it connected OK. If not, get this sorted!
  1. If it has worked, do another "Run". Both instances should have two boxes :)
  1. With only one PC, it's going to be hard to see real-time movement, so if you're lucky and have two, I suggest having the faster PC running the Darkstar server and a client, and having another PC connecting to the server remotely.

---

# Conclusion #
First of all, I hope this tutorial was useful. Secondly, I hope it worked for you without too much fuss. Let me know if you have difficulties (e-mail at the top), and I'll try my best to look at it.

Thanks for reading...

---

# Help needed! #
If you found this useful, and are prepared to help me out, I'd like to add the following functionality:

  * **Collision detection:** You'll notice that the walls won't stop you! I'm having a few issues getting this working properly.
  * **Interpolation/Dead reckoning:** Movement looks clunky right now. I would like to implement a smoothing algorithm of some sort. One that is going to handle the firing of bullets later.