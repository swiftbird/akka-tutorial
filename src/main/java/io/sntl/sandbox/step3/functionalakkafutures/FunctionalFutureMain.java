package io.sntl.sandbox.step3.functionalakkafutures;

/*
 * A common use case within Akka is to have some computation performed 
 * concurrently without needing the extra utility of an UntypedActor. 
 * If you find yourself creating a pool of UntypedActors for the sole 
 * reason of performing a calculation in parallel, there is an easier (and faster) way:
 * 
 * This is NOT using Actors, but Callable classes for the small unit of computation to save on overhead
 * as suggested in: http://doc.akka.io/docs/akka/snapshot/java/futures.html
 * 
 * Functional Futures
 * Scala's Future has several monadic methods that are very similar to the ones used by Scala's collections. 
 * These allow you to create 'pipelines' or 'streams' that the result will travel through.
 *
 * Future is a Monad
 * The first method for working with Future functionally is map. This method takes a 
 * Mapper which performs some operation on the result of the Future, and returning a new result. 
 * The return value of the map method is another Future that will contain the new result
 * 
 */


import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import akka.actor.ActorSystem;
import akka.actor.Terminated;
import akka.dispatch.Mapper;
import static akka.dispatch.Futures.future;

public class FunctionalFutureMain {

	private ActorSystem system;

	public FunctionalFutureMain() {
		// Create the ActorSystem
		system = ActorSystem.create("DirectFuture");
	}

	public void doIt() {

		final ExecutionContext ec = system.dispatcher();

		Future<Long> f1 = future(new PauseALot(), ec);
		
//		The original Future will take at least 0.1 second to execute now, which means 
//		it is still being processed at the time we call map. The function we provide 
//		gets stored within the Future and later executed automatically by the dispatcher when the result is ready.

		Future<String> f2 = f1.map(new Mapper<Long, String>() {
			// Overriding the default Mapper apply method inline here -- I kinda like creating a class that
			// extends Mapper to feel more OO but this works.
			public String apply(Long s) {
				return "I'm doing a thing with the result of the first future: " + Long.toString(s);
			}
		}, ec);
		
		// Demonstrating coordination.  
		Future<Long> f3 = future(new PauseALittle(), ec);
		f3.onSuccess(new PrintResult<Long>(), system.dispatcher());

		f2.onSuccess(new PrintResult<String>(), system.dispatcher());
	
		Future<Terminated> t = system.terminate();
		
		System.out.println("Akka System has shutdown");
	}

	public static void main(String[] args) {
		FunctionalFutureMain dfm = new FunctionalFutureMain();
		dfm.doIt();

	}

}
