package io.sntl.sandbox.step6.actorfutures;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

public class GetOneAttemptActor extends AbstractActor {

	
	// now make this return a list
	
	GetOneAttemptActor() {
		
		receive(
				ReceiveBuilder.matchAny(s -> {
					System.out.println("GetOneAttemptActor: Just Pausing");
					
					new PauseALittle().call();
					System.out.println("GetOneAttemptActor: Going to do the tell back " + sender().toString());
					sender().tell("response: " + s, self());
					
				}).build());
		
	}
}
