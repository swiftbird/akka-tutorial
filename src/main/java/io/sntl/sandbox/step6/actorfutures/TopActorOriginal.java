package io.sntl.sandbox.step6.actorfutures;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class TopActorOriginal extends AbstractActor {
	public TopActorOriginal() {
		receive(
		// do something
		ReceiveBuilder
				.match(AttemptScoreMessage.class, attempt -> {
					// create a new actor
					// return a list of attempts
						System.out.println("TopActor: AttemptScore received! "
								+ attempt.getAssignment_id());
						// ActorRef getAttempts = context().actorOf(
						// Props.create(GetAttemptsActor.class),
						// "getAttempts");
						ActorRef getAttempts = context().actorOf(
								Props.create(ReferenceActor.class),
								"getAttempts");
						// Invoke the getAttempts actor
						System.out
								.println("TopActor: sending message to getAttempts");
						getAttempts.tell(attempt, self());

					})
				.match(AttemptScoreMessageAsync.class, attempt -> {
					// create a new actor
					// return a list of attempts
						System.out
								.println("TopActor: AttemptScoreAsync received! "
										+ attempt.getAssignment_id());
						ActorRef getAttempts = context().actorOf(
								Props.create(ReferenceActor.class),
								"getAttempts");
						// Invoke the getAttempts actor
						System.out
								.println("TopActor: sending message to getAttempts");
						getAttempts.tell(attempt, self());

					})
				.match(AttemptScoreCalculatedMessage.class,
						calculated -> {
							System.out.println("TopActor: Got the response: "
									+ calculated.getAssignment_id());

						})
				.match(AttemptList.class, attempts -> {
					System.out.println("TopActor: Received an AttemptList");
					for (Attempt att : attempts)
						System.out.println("I got " + att.getAssignment_id());
				})
				.matchAny(
						unknown -> {
							String messageType = unknown.getClass().getName();
							System.out.println("TopActor: Unknown Message: "
									+ messageType);
						}).build());
	}
}
