# Spring Simulator

Have you ever wished you could simulate what happened on that fateful day when your app behaved unexpectedly. Or would you like a nice way of smoke testing with a previously recorded data set? Or do you just want to add some new annotations to your codebase?

If you answered yes to any of these questions then Spring simulator is for you.

## What is it?

Spring Simulator allows you to mark a method for simulation. It runs in two modes which must be specified at start time using specific Spring profiles. The first mode (record) records the data for simulation and the second mode (simulate) plays back or simulates with the recorded data.

## How do I use it?
There are two types of simulation, specified using @SimulateResult and @SimulateCall.

### @SimulateResult
@SimulateResult simulates the result of a method call similar to Springs @Cacheable annotation in that it records the result of the method call for the arguments given and when in simulation mode returns the recorded results.

### @SimulateCall
@SimulateCall simulates the method invocation rather than the result. ie. It records the time of the invocation, along with the arguments passed. In 'simulate' mode the method is invoked at the recorded time with the recorded arguments asynchronously.

###Constraints

- Method arguments of @SimulateResult must implement equals()