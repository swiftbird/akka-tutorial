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
 * Composing Futures
 * It is very often desirable to be able to combine different Futures with each other, 
 * below are some examples on how that can be done in a non-blocking fashion.
 * 
 */

import java.util.ArrayList;
import java.util.List;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import akka.actor.ActorSystem;
import akka.dispatch.Mapper;
import static akka.dispatch.Futures.sequence;
import static akka.dispatch.Futures.future;

public class SequencingFuturesMain {

	private ActorSystem system;

	public SequencingFuturesMain() {
		// Create the ActorSystem
		system = ActorSystem.create("DirectFuture");
	}

	public void doIt() {

		final ExecutionContext ec = system.dispatcher();
		
		List<Future<Long>> futures = new ArrayList<Future<Long>>();

		System.out
				.println("Akka Futures says: Adding futures for random length pauses");

		futures.add(future(new RandomPause(), ec));
		futures.add(future(new PauseALot(), ec));
		futures.add(future(new PauseALittle(), ec));

		System.out.println("Akka Futures says: There are " + futures.size()
				+ " Pause's currently running");
			
		 
		// now we have a Future[Iterable[Integer]]
		Future<Iterable<Long>> futuresSequence = sequence(futures, ec);
		 

		Future<Long> futureSum = futuresSequence.map(
		// This is where we could pipe the results
		new Mapper<Iterable<Long>, Long>() {
			public Long apply(Iterable<Long> ints) {
				long sum = 0;
				for (Long i : ints) {
					sum += i;
					System.out.println("Count " + i);
				}
				return sum;
			}
		}, ec);
		 
		futureSum.onSuccess(new PrintResult<Long>(), system.dispatcher());

		system.terminate();
		
		System.out.println("Akka System has shutdown");
		
		/* 
		 * To better explain what happened in the example, Future.sequence is 
		 * taking the Iterable<Future<Long>> and turning it into a Future<Iterable<Long>>. 
		 * We can then use map to work with the Iterable<Long> directly, and we aggregate the 
		 * sum of the Iterable.
		 */
	}

	public static void main(String[] args) {
		SequencingFuturesMain dfm = new SequencingFuturesMain();
		dfm.doIt();

	}

}
