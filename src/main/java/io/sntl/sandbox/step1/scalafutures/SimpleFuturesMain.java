package io.sntl.sandbox.step1.scalafutures;

import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Mapper;

/* 
 * This class demonstrates the use of Scala futures without the Akka system.
 * It shows both sequencing of multiple threads (asynchronous composition) as well as
 * Callback and blocking calls.
 * 
 * Using Java 8 ExecutorService thread pool, not akka.
 * 
 */

public class SimpleFuturesMain {
	public static void main(String[] args) throws InterruptedException {

		ExecutorService executor = Executors.newFixedThreadPool(4);
		ExecutionContext ec = ExecutionContexts.fromExecutorService(executor);

		List<Future<Long>> futures = new ArrayList<Future<Long>>();

		System.out
				.println("Akka Futures says: Adding futures for random length pauses");

		futures.add(future(new RandomPause(), ec));
		futures.add(future(new PauseALot(), ec));
		futures.add(future(new PauseALittle(), ec));

		System.out.println("Akka Futures says: There are " + futures.size()
				+ " Pause's currently running");

		// compose a sequence of the futures
		Future<Iterable<Long>> futuresSequence = sequence(futures, ec);

		// Find the sum of the odd numbers
		// This takes the result from the pause (a Long in this case) and let's
		// you iterate
		// Through the results as they come in.
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

		futureSum.onSuccess(new SuccessfulCompletion<Long>(), ec);

		// wait until the result all comes back (synchronize everything back)
		try {
			System.out.println("I am doing more stuff...");
			// block until the futures come back
			System.out.println("Result :"
					+ Await.result(futureSum,
							Duration.apply(5, TimeUnit.SECONDS)));
			System.out.println("Is this at the end...");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Thread.sleep(10000);
		executor.shutdown();
		System.out.println("Java Executor System has shutdown");
	}

}
