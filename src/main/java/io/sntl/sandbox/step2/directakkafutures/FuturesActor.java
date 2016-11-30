package io.sntl.sandbox.step2.directakkafutures;

import akka.actor.AbstractActor;

/*
 * To send the result of a Future to an Actor, you can use the pipe construct:

 akka.pattern.Patterns.pipe(future, system.dispatcher()).to(actor);

 Akka's Future has several monadic methods that are very similar to the ones used by Scala's 
 collections. These allow you to create 'pipelines' or 'streams' that the result will travel 
 through. 

 */

public class FuturesActor extends AbstractActor {

}
