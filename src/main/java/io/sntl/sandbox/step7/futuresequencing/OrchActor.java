package io.sntl.sandbox.step7.futuresequencing;

import java.util.concurrent.TimeUnit;

//import com.sentinel.client.LogService;


import scala.concurrent.Future;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.OnComplete;
import akka.util.Timeout;
import io.sntl.sandbox.step7.futuresequencing.Actor1.Actor1Response;
import io.sntl.sandbox.step7.futuresequencing.Actor2.Actor2Response;
import scala.concurrent.duration.Duration;
import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;

public class OrchActor extends UntypedActor {
	
//	private LogService log;

	public void preStart() throws Exception {
//		log = new LogService("http://75.145.119.57:8080", OrchActor.class);
		// System.out.println("Actor1: preStart");
//		log = new LogService(OrchActor.class);
		super.preStart();
	}

	public void postStop() throws Exception {
		// System.out.println("Actor1: postStop");
		super.postStop();
	}

	public void onReceive(Object message) throws Exception {
		// Recieve either a student or a collection of student grades in
		// category bucket list
		
//		 System.out.println("OrchActor received a message. "
//				+ message.getClass().getName()); 
		
//		log.debug("Message Received", message.toString(), "123456");
	
		// I have a grade that changed so let's update the GTD
		if (message instanceof String) {
			// System.out.println("OrchActor Starting the process");
			final Timeout t = new Timeout(Duration.create(5, TimeUnit.SECONDS));
 
			ActorRef actor1 = context().actorOf(Props.create(Actor1.class));
			ActorRef actor2 = context().actorOf(Props.create(Actor2.class));

			Future<Object> future1 = ask(actor1, "Do your thing", t);

//			Future<Object> future2 = ask(actor2, "Do your thing", t);

			future1.andThen(new OnComplete<Object>() {
				public void onComplete(Throwable failure, Object result) {
					// I got the response back
					Actor1Response r = (Actor1Response) result;
					// System.out.println("Doing the and then: " + r.message);
					actor2.tell("Do your thing", self());
					if (failure != null) {
						// sendToIssueTracker(failure);
					}
				}

			}, context().dispatcher());

//			pipe(future1, context().dispatcher()).to(self());
//			pipe(future2, context().dispatcher()).to(self());

		} else if (message instanceof Actor1Response) {
			Actor1Response r = (Actor1Response) message;
			// System.out.println("OrchActor: " + r.message);
		} else if (message instanceof Actor2Response) {
			Actor2Response r = (Actor2Response) message;
			// System.out.println("OrchActor: " + r.message);
		}
	}

}
