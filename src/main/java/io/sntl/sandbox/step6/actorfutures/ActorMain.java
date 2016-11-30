package io.sntl.sandbox.step6.actorfutures;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class ActorMain {

	private ActorSystem system;
	@SuppressWarnings("unused")
	private final ActorRef topLevelService;

	public ActorMain() {
		// TODO Change this to start with a defined configuration so we can
		// control
		// Thread pools and the like
		system = ActorSystem.create("TopService");
		// System.out.println("Akka Settings: \n" + system.settings());

		topLevelService = createTopService();
	}

	protected ActorRef createTopService() {
		return system.deadLetters();
	}

	public String complexOperation(String input) {
		String result = "Generic Operation: Completed";
		ActorRef genericActor = system.actorOf(Props.create(TopActorOriginal.class),
				"topActor");
		
		System.out.println("Main: Now doing the async message");
		AttemptScoreMessageAsync asm =  new AttemptScoreMessageAsync();
		asm.setAssignment_id(input);
		genericActor.tell(asm, ActorRef.noSender());

		return result;
	}
	
	public String simpleOperation(String input) {
		String result = "Simple Operation: Completed";
		ActorRef genericActor = system.actorOf(Props.create(TopActorOriginal.class),
				"topActor2");

		// Send the starting message -- in this case it represents an update
		AttemptScoreMessage message = new AttemptScoreMessage();
		message.setAssignment_id(input);
		genericActor.tell(message, ActorRef.noSender());
				
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ActorMain am = new ActorMain();
		am.simpleOperation("Test1");
		am.complexOperation("Test2");
//		am.system.terminate();
		

	}

}
