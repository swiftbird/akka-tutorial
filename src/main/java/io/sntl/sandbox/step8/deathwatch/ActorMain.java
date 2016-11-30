package io.sntl.sandbox.step8.deathwatch;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.Props;

public class ActorMain {
	private ActorSystem system;
	@SuppressWarnings("unused")
	private final ActorRef topLevelService;

	public ActorMain() {

		system = ActorSystem.create("TopService");

		topLevelService = createTopService();
	}

	public String doIt(String input) throws InterruptedException {
		String result = "doIt Completed";
		ActorRef reaper = system.actorOf(
				Props.create(DynamicActorManager.class), "DynamicActorManager");
		System.out.println("The Reaper is started as: " + reaper.path());
		// Create the dynamic Actor to watch
		Cancellable reapTimer = system.scheduler().schedule(Duration.Zero(),
				Duration.create(5000, TimeUnit.MILLISECONDS), reaper,
				new DynamicActorManager.Reap(5000), system.dispatcher(),
				ActorRef.noSender());

//		ActorRef destructive = system.actorOf(Props
//				.create(DestructiveActor.class));
//		destructive.tell("do your thing", ActorRef.noSender());
		
		for (int x = 0; x < 10; x++) {
			ActorRef actor1 = system
					.actorOf(Props.create(Actor1.class));
			actor1.tell("Actor " +  x, ActorRef.noSender());
		}
//		try {
//			for (int y = 0; y < 100; y++) {
//				for (int x = 0; x < 100; x++) {
//					// Thread.sleep(1000);
//					ActorRef actor1 = system
//							.actorOf(Props.create(Actor1.class));
//
//					actor1.tell("sleep", ActorRef.noSender());
//				}
//
//
//				for (int x = 0; x < 100; x++) {
//					// Thread.sleep(1000);
//					ActorRef actor1 = system
//							.actorOf(Props.create(Actor1.class));
//
//					actor1.tell("sleep", ActorRef.noSender());
//				}
//			}
//			Thread.sleep(1000);
//			System.out.println("================");
//			reaper.tell(new DynamicActorManager.Status(), null);
//			Thread.sleep(10000);
//
//			
//
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println("done with the actors");
		Thread.sleep(10000);
		reaper.tell(new DynamicActorManager.Status(), null);
		Thread.sleep(10000);
//		system.shutdown();
		system.terminate();
		return result;
	}

	protected ActorRef createTopService() {
		return system.deadLetters();
	}

	public static void main(String[] args) throws InterruptedException {
		ActorMain m = new ActorMain();
		System.out.println(m.doIt(null));

	}

}
