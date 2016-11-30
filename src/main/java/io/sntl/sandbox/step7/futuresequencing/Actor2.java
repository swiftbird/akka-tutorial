package io.sntl.sandbox.step7.futuresequencing;

import akka.actor.UntypedActor;

public class Actor2 extends UntypedActor {

	public void preStart() throws Exception {
		System.out.println("Actor2: preStart");

		super.preStart();
	}

	public void postStop() throws Exception {
		System.out.println("Actor2: postStop");
		super.postStop();
	}

	public void onReceive(Object message) throws Exception {
		// Recieve either a student or a collection of student grades in
		// category bucket list
		System.out.println("Actor2 received a message. "
				+ message.getClass().getName());

		// I have a grade that changed so let's update the GTD
		if (message instanceof String) {
			System.out.println("Actor2 sleeping for 1");
			Thread.sleep(1000);
			sender().tell(new Actor2Response("Actor2 Completed"), self());

		}
	}

	public static class Actor2Response {

		public Actor2Response(String m) {
			message = m;
		}

		public String message;
	}
}
