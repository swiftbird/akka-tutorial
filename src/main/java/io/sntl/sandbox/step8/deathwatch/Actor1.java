package io.sntl.sandbox.step8.deathwatch;

import akka.actor.ActorRef;
import akka.actor.Props;
import static akka.pattern.Patterns.ask;

public class Actor1 extends DynamicActor {

	public Actor1() {
		System.out.println("I am in the Actor1 constructor");
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String m = (String) message;
//			for (int x = 0; x < 50; x++) {
//				ActorRef destructive = context().system().actorOf(Props
//						.create(DestructiveActor.class));
//				destructive.tell(x, self());
//				Thread.sleep(500);
//				System.out.println(m + " " + x);
//				
//			}
			ActorRef destructive = context().system().actorOf(Props
					.create(DestructiveActor.class));
//			destructive.tell(m, self());	
			ask(destructive, m, 100000);
//			context().stop(self());
			Thread.sleep(30000);
			System.out.println("Actor " + m + " has completed his task");
			
			
		}

	}

}
