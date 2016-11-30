package io.sntl.sandbox.step4.composingfutures;

/*
 * A common use case within Akka is to have some computation performed 
 * concurrently without needing the extra utility of an UntypedActor. 
 * If you find yourself creating a pool of UntypedActors for the sole 
 * reason of performing a calculation in parallel, there is an easier (and faster) way:
 * 
 * This is NOT using Actors, but Callable classes for the small unit of computation to save on overhead
 * as suggested in: http://doc.akka.io/docs/akka/snapshot/java/futures.html
 * 
 * 
 * The traverse method is similar to sequence, but it takes a sequence of A and applies a function from 
 * A to Future<B> and returns a Future<Iterable<B>>, enabling parallel map over the sequence, if you use 
 * Futures.future to create the Future.
 * 
 */

import java.util.Arrays;
import java.util.concurrent.Callable;

import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import akka.actor.ActorSystem;
import akka.japi.Function;
import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.traverse;

public class TraverseFuturesMain {

	private ActorSystem system;

	public TraverseFuturesMain() {
		// Create the ActorSystem
		system = ActorSystem.create("DirectFuture");
	}

	public void doIt() {

		final ExecutionContext ec = system.dispatcher();

		// Just a sequence of Strings
		Iterable<String> listStrings = Arrays.asList("a", "b", "c");

		Future<Iterable<String>> futureResult = traverse(listStrings,
				new Function<String, Future<String>>() {
					public Future<String> apply(final String r) {
						return future(new Callable<String>() {
							public String call() {
								return r.toUpperCase();
							}
						}, ec);
					}
				}, ec);

		// Returns the sequence of strings as upper case
		futureResult.onSuccess(new PrintResult<Iterable<String>>(),
				system.dispatcher());

		system.terminate();

		System.out.println("Akka System has shutdown");

		/*
		 * To better explain what happened in the example, Future.sequence is
		 * taking the Iterable<Future<Long>> and turning it into a
		 * Future<Iterable<Long>>. We can then use map to work with the
		 * Iterable<Long> directly, and we aggregate the sum of the Iterable.
		 */
	}

	public static void main(String[] args) {
		TraverseFuturesMain dfm = new TraverseFuturesMain();
		dfm.doIt();

	}

}
