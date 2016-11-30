package io.sntl.sandbox.step2.directakkafutures;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import akka.actor.ActorSystem;
import akka.dispatch.*;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.Await;
import scala.concurrent.Promise;
import akka.util.Timeout;
import scala.concurrent.ExecutionContext;
import scala.concurrent.ExecutionContext$;

public class AkkaFutures {

	private static ActorSystem system;
	
	public static void main(String[] args) {
		system = ActorSystem.create("CalcService");
		ExecutorService executor = Executors.newFixedThreadPool(4);
		ExecutionContext ec = ExecutionContexts.fromExecutorService(executor);
		
				 
				//Use ec with your Futures
				Future<String> f1 = Futures.successful("foo");
				 
				// Then you shut down the ExecutorService at the end of your application.
				executor.shutdown();

	}

}
