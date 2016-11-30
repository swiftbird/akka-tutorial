package io.sntl.sandbox.step7.futuresequencing;

import akka.actor.UntypedActor;

public class Actor1 extends UntypedActor {

	public void preStart() throws Exception {
		System.out.println("Actor1: preStart");

		super.preStart();
	}

	public void postStop() throws Exception {
		System.out.println("Actor1: postStop");
		super.postStop();
	}

	public void onReceive(Object message) throws Exception {
		// Recieve either a student or a collection of student grades in
		// category bucket list
		System.out.println("Actor1 received a message. "
				+ message.getClass().getName());

		// I have a grade that changed so let's update the GTD
		if (message instanceof String) {
			System.out.println("Actor1 sleeping for 5");
			Thread.sleep(5000);
			sender().tell(new Actor1Response("Actor1 Completed"), self());

		}
	}

	public static class Actor1Response {

		public Actor1Response(String m) {
			message = m;
		}

		public String message;
	}
}
