# Akka Java Tutorial
This 9 step tutorial should help demonstrate the use of the Akka system for Actor based concurrency, synchronization and orchestration.  There are many features in Akka that are not demonstrated here such as Akka Persistance, Akka Streams and Akka HTTP.

Many of the samples shown do not use Lambda expressions in the Actors because it was originally written for Java developers unfamiliar with many Java 8 constructs.  For Lambda examples, see Step 6

## Step 1
**Scala Futures**

This class demonstrates the use of Scala futures without the Akka system. It shows both sequencing of multiple threads (asynchronous composition) as well as Callback and blocking calls.

Using Java 8 ExecutorService thread pool, not akka.

## Step 2
**Direct Akka futures**

A common use case within Akka is to have some computation performed concurrently without needing the extra utility of an UntypedActor. If you find yourself creating a pool of UntypedActors for the sole reason of performing a calculation in parallel, there is an easier (and faster) way:

This is NOT using Actors, but Callable classes for the small unit of computation to save on overhead as suggested in: http://doc.akka.io/docs/akka/snapshot/java/futures.html

## Step 3
**Functional Akka futures**

Scala's Future has several monadic methods that are very similar to the ones used by Scala's collections. These allow you to create 'pipelines' or 'streams' that the result will travel through.

### Future is a Monad
The first method for working with Future functionally is map. This method takes a Mapper which performs some operation on the result of the Future, and returning a new result. The return value of the map method is another Future that will contain the new result

## Step 4
**Composing futures**

It is very often desirable to be able to combine different Futures with each other, these are some examples on how that can be done in a non-blocking fashion.

## Step 5
**Ordered composition / Orchestration**

This example demonstrates a technique to control the order of future completion (callback ordering).  This is useful when you want to make sure one asynchronous task is completed only after other tasks have completed.

## Step 6
**Actor Futures**

Use Akka Actors instead of Callable objects to demonstrate asynchronous processing using the Actor pattern.  Akka actors respond to messages and are also Monads.  This step also details the use of messages and the messaging system.

In Akka there are two ways of triggering an asynchronous task:

**actor.tell(...)**
By using tell(...) you are originating a non-blocking task without any required response or callback.  This is a "throw it over the wall" type of request.

**actor.ask(...)**
An ask(...) call returns a Future<YourType> that will be completed when the target Actor responds with a sender.tell(message).  This can be a blocking call, but in most cases you will _pipe_ the completed result back to a receiving actor in a non-blocking fashion.

## Step 7
**Actor Future Sequencing**

This demonstrates the use of the Future.andThen(...) technique for asynchronously invoking another asynchronous thread only when a previous one has finished.

## Step 8
**Death Watch and dynamic Actors**

An implementation of a Dynamic Actor Manager (Akka does not have the concept of an actor manager natively).  The use of this manager allows for Actor garbage collection, monitoring, logging and other controls.  There is a Reaper actor responsible for garbage collecting any stale actors in the system.  Similar actors can be created for using in monitoring and data collection.
