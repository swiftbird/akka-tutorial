package io.sntl.sandbox.step2.directakkafutures;

/*
 * A common use case within Akka is to have some computation performed 
 * concurrently without needing the extra utility of an UntypedActor. 
 * If you find yourself creating a pool of UntypedActors for the sole 
 * reason of performing a calculation in parallel, there is an easier (and faster) way:
 * 
 * This is NOT using Actors, but Callable classes for the small unit of computation to save on overhead
 * as suggested in: http://doc.akka.io/docs/akka/snapshot/java/futures.html
 * 
 */


import scala.concurrent.Future;
import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import static akka.dispatch.Futures.future;

public class DirectFutureMain {

	private ActorSystem system;

	public DirectFutureMain() {
		// Create the ActorSystem
		system = ActorSystem.create("DirectFuture");
	}

	public void doIt() {
//		 Future<String> f = future(new Callable<String>() {
//		 public String call() {
//		 return "Hello" + "World";
//		 }
//		 }, system.dispatcher());
		
//		List<Future<Long>> futures = new ArrayList<Future<Long>>();
		
		Future<Long> f = future(new PauseALittle(), system.dispatcher());
		
//		futures.add(f);

		System.out
				.println("Doing something while pausing in the background...");
		f.onSuccess(new PrintResult<Long>(), system.dispatcher());
	
		
//		system.shutdown();
		system.terminate();
		System.out.println("Akka System has shutdown");
	}

	public static void main(String[] args) {
		DirectFutureMain dfm = new DirectFutureMain();
		dfm.doIt();

	}

}
