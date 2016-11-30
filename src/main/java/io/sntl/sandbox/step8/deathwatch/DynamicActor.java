package io.sntl.sandbox.step8.deathwatch;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

public abstract class DynamicActor extends UntypedActor {

	// Register with the reaper

	@Override
	public void preStart() throws Exception {
		// Since this is dynamic, Register with a manager so I can be cleaned up
		System.out.println(this.getClass().getSimpleName() + " preStart");
		ActorSelection reaper = context().system().actorSelection(
				"/user/DynamicActorManager");

		System.out.println("Bound to DynamicActorManager: " + reaper.pathString());
		DynamicActorManager.Register registerMessage = new DynamicActorManager.Register(self());
		System.out.println("created a register message to send");
		reaper.tell(registerMessage, ActorRef.noSender());
		super.preStart();
	}

	@Override
	public void postStop() throws Exception {
		System.out.println(this.getClass().getSimpleName() + " postStop");
		super.postStop();
	}

	public DynamicActor() {
		System.out.println("I am in the DynamicActor constructor");

	}

}
