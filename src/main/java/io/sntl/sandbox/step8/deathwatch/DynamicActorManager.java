package io.sntl.sandbox.step8.deathwatch;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;

public class DynamicActorManager extends UntypedActor {
	
	private HashMap<String, ReapableActorContainer> actors;
	
	private int totalActorsManaged;
	private int totalActorsReaped;

	public DynamicActorManager() {
		actors = new HashMap<String, ReapableActorContainer>();
	}

	@Override
	public void preStart() throws Exception {
		// do some stuff as the actor starts up
		System.out.println("DynamicActorMangaer: preStart");

		super.preStart();
	}

	@Override
	public void postStop() throws Exception {
		// do some stuff to clean up
		System.out.println("DynamicActorMangaer: postStop");
		super.postStop();
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Register) {
			System.out
					.println("DynamicActorMangaer: Received a Register message ");
			// I am registering an actor to be watched
			Register reg = (Register) message;

			actors.put(reg.getName(),
					new ReapableActorContainer(reg.getActor()));
			getContext().watch(reg.getActor());
			System.out.println("DynamicActorMangaer: Reaper now watching "
					+ actors.size() + " Actors.");
			totalActorsManaged++;

		} else if (message instanceof Reap) {
			System.out
					.println("DynamicActorMangaer: Reaper received a Reap message ");
			Reap reap = (Reap) message;
			System.out
					.println("DynamicActorMangaer: Going to Reap anything older than "
							+ reap.getOlderThan() + " milliseconds.");
			performReaping(reap.getOlderThan());

		} else if (message instanceof Terminated) {

			final Terminated t = (Terminated) message;
			String name = t.getActor().path().name();
			System.out
					.println("DynamicActorMangaer: I got a terminated message from: "
							+ name);
			actors.remove(name);
			System.out.println("DynamicActorMangaer: Reaper now watching "
					+ actors.size() + " Actors.");
		} else if (message instanceof Status) {
			StatusResponse response = new StatusResponse(totalActorsManaged,
					totalActorsReaped, actors.size());
			System.out.println("DynamicActorManager: " + response);
			sender().tell(response, self());
		} else {
			unhandled(message);
		}

	}

	private void performReaping(long olderThan) {
		System.out.println("DynamicActorMangaer: Performing Reaping!");
		int reapCount = 0;
		Iterator<ReapableActorContainer> i = actors.values().iterator();
		while (i.hasNext()) {
			ReapableActorContainer r = i.next();

			Instant actorTime = r.getCreateTime().toInstant();
			Instant now = new Date().toInstant();
			Instant reapTime = now.minusMillis(olderThan);

			if (actorTime.isBefore(reapTime)) {
				System.out.println("DynamicActorMangaer: You are toast dude! "
						+ r.getActor().path().name());
				context().stop(r.getActor());
				reapCount++;
			} else {
				System.out
						.println("DynamicActorMangaer: You live to fight another day! "
								+ r.getActor().path().name());
			}

		}
		System.out.println("DynamicActorMangaer: Reaped a total of "
				+ reapCount + " Actors");
		totalActorsReaped += reapCount;

	}

	// Messages
	public static class Reap {
		private long olderThan;

		public Reap(long olderThan) {
			this.setOlderThan(olderThan);
		}

		public long getOlderThan() {
			return olderThan;
		}

		public void setOlderThan(long olderThan) {
			this.olderThan = olderThan;
		}

	}

	public static class Register {

		public ActorRef getActor() {
			return actor;
		}

		public void setActor(ActorRef actor) {
			this.actor = actor;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private ActorRef actor;
		private String name;

		public Register(ActorRef a) {
			actor = a;
			name = actor.path().name();
		}

	}

	public static class Status {
		// Just wants status. No fields
	}

	public static class StatusResponse {

		@Override
		public String toString() {
			return "StatusResponse [totalActorsManaged=" + totalActorsManaged
					+ ", totalActorsReaped=" + totalActorsReaped
					+ ", currentActorsWatched=" + currentActorsWatched + "]";
		}

		private int totalActorsManaged;
		private int totalActorsReaped;
		private int currentActorsWatched;

		public StatusResponse(int totalManaged, int totalReaped, int current) {
			totalActorsManaged = totalManaged;
			totalActorsReaped = totalReaped;
			currentActorsWatched = current;
		}

		public int getTotalActorsManaged() {
			return totalActorsManaged;
		}

		public void setTotalActorsManaged(int totalActorsManaged) {
			this.totalActorsManaged = totalActorsManaged;
		}

		public int getTotalActorsReaped() {
			return totalActorsReaped;
		}

		public void setTotalActorsReaped(int totalActorsReaped) {
			this.totalActorsReaped = totalActorsReaped;
		}

		public int getCurrentActorsWatched() {
			return currentActorsWatched;
		}

		public void setCurrentActorsWatched(int currentActorsWatched) {
			this.currentActorsWatched = currentActorsWatched;
		}

	}
}
