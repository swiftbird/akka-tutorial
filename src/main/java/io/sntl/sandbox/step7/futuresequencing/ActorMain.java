package io.sntl.sandbox.step7.futuresequencing;

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

	public String doIt(String input) {
		String result = "doIt Completed";
		ActorRef orchActor = system.actorOf(Props.create(OrchActor.class),
				"orchActor");

		orchActor.tell("Start", ActorRef.noSender());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		system.shutdown();
		system.terminate();
		return result;
	}

	protected ActorRef createTopService() {
		return system.deadLetters();
	}

	public static void main(String[] args) {
		ActorMain m = new ActorMain();
		System.out.println(m.doIt(null));

	}

}
