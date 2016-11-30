package io.sntl.sandbox.step8.deathwatch;

public class DestructiveActor extends DynamicActor {

	public DestructiveActor() {
		System.out.println("I am in the sub constructor");

	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String m = (String) message;
			System.out
					.println("Destructive Actor " + m + " self destructing in 50000 ms ");
			Thread.sleep(50000);
			// context().stop(self());
			System.out.println("Destructive Actor " + m + " has finished the long wait.");
		}

	}

}
