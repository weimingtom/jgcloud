# Interpolation (or how to smooooth things out) #
## Introduction ##
This Wiki is a quick overview of how I implemented interpolation in to my project. For those that don't know what I'm talking about, interpolation is the process of working out where an object would be at a given point in time given a start point and an end point and a duration (at least this is my interpretation for my needs).

In my [DarkstarJMEIntegration](DarkstarJMEIntegration.md), clients are sending their location every 100 milliseconds. If you have watched the tanks moving around the arena, you'll see that this looks very clunky. We could increase the frequency (in fact, for a LAN game you probably would), but when we're sending messages over greater distances via the internet, we may need to be more realistic about how often messages can be sent/received.

So, how do we sort this clunky movement out? Well, you've probably guessed. A technique called interpolation. If you know where you want the tank to be in (say) 100 milliseconds, and you know where it is now, then you can divide that distance up in to smaller points along the way. The same principal applies accross all three x, y and z axes. You just need to divide them up by time.

Please be aware that there is a distinction (again, in my implementation) between interpolation and "Dead Reckoning". Dead Reckoning is the process of "guessing" where the node will be given their current direction, speed, acceleration etc. This means that the player sees where remote players are likely to be right now. The implementation described below is just taking the new "current" location sent by the client and creating a smooth path towards it until the next location is received again. Some of you may already be noticing that this means we are only ever seeing where they were, not where they are now. Indeed. A decent dead-reckoner is on my to-do list ;)

---

## Grabbing the Source Code ##
Please check (using an svn client) the latest release from my project:
```
  svn checkout http://jgcloud.googlecode.com/svn/trunk/DarkstarJME
  svn checkout http://jgcloud.googlecode.com/svn/trunk/TestDarkstarServer
```

---

## Code Overview ##
### Interpolator.java ###
The first class to look at is the aptly named "Interpolator". This class has a start and finish translation, rotation and time. The intention is that when a new remote player (client) appears, we create a new Interpolator instance for them. The next time they send us their location we will know:

  * Their current location
  * Their current rotation
  * The new location
  * The new rotation
  * The current time (system clock time)
  * How long (roughly) we need to wait before their next location message will arrive.

Right then, so what we will do is update the start translation and rotation with their current translation and rotation. We'll set the new translation and rotation to the details that have just arrived. We'll set the start time to _now_ (on the system clock), and we'll set the finish time to _now+update\_interval_ (eg 100ms). This needs to be done for every remote client, every time they send their new location.

Given this information, the Interpolator has a couple of key methods that we can now use:

  * `getCurrentTranslation()`
  * `getCurrentRotation()`

Starting with `getCurrentTranslation()`. You'll notice it's pretty small. This is because I have some good news for you. JME has an `interpolate(...)` method for a `Vector3f` ! All we need to do is tell it the `startTranslation`, `finishTranslation` and how near we are (as a percent) to the finish time (where 1 means we're at the finish time). It will then return a new `Vector3f` of where we should be.

So when the main GPL threads `update(...)` method loops through the remote players, it can call the `getCurrentTranslation()` and put the remote player there.

The same principal applies to `getCurrentRotation(...)` except the JME method is now called `slerp(...)` ?? Must have been thirsty when they wrote that method ;)


### TankGameState.updateRemotePlayerLocations() ###
So how do we implement this? well, it's pretty straightforward actually. Those that have gone through the [DarkstarJMEIntegration](DarkstarJMEIntegration.md) wiki will see there's not much difference.

`updateRemotePlayerLocations()` is called by the `TankGameState.update()` method every frame. The first thing it does is poll our playerDetails queue which gets updated via the `DarkstarUpdater` thread as and when new client locations arrive. This will contain an update of an existing players position, or potentially, a new players position. If it's a new player, then we update their Interpolator instance, setting their `startLocation/Rotation` to what we just received and time=_now_, also setting their `finishLocation/Rotation/Time` to the same thing (we're hoping a new update will arrive soon). If it's an existing player then we update their `startLocation/Rotation` to where they currently are on our arena, and the time=_now_. Their `finishLocation/Rotation` is set to the new value that's just arrived and the finishTime is set to _now+update\_interval_ (eg 100 for 100ms). We also allow a bit more time in case theirs any latency.

The final step is to loop through our registered remotePlayers Set and set their position and rotation to the interpolate values that the Interpolator instance gives us. This means every frame their position is getting estimated and appears much more smooth.

If you can now imagine that a client is getting a regular update of a remote players location. When it does, it updates their interpolator to the new position. While it's waiting for the next one to arrive, it moves along each frame towards the last stated location. This is happening very quickly, and with any luck, very smoothly ;)

---

## Summary ##
I hope this has made some sense and will be useful to you. Please be sure to give me any feedback on something that's not clear.

Cheers
Richard