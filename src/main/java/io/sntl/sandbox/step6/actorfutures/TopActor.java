package io.sntl.sandbox.step6.actorfutures;

import scala.PartialFunction;
import scala.runtime.BoxedUnit;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class TopActor extends AbstractActor {
	public TopActor() {

		// We are overriding from the method, not inline in the constructor

	}

	@Override
	public void preStart() throws Exception {
		// do some stuff as the actor starts up
		System.out.println("ReferenceActor preStart");

		super.preStart();
	}

	@Override
	public void postStop() throws Exception {
		// do some stuff to clean up
		System.out.println("ReferenceActor postStop");
		super.postStop();
	}

	// This is the main message handler -- implement all of the actor stuff here

	@Override
	public PartialFunction<Object, BoxedUnit> receive() {

		// Since this is a top level actor, we are just routing message to the
		// worker actors

		return ReceiveBuilder.match(AttemptScoreMessage.class, attempt -> {

			procesAttemptScore(attempt);

		}).match(AttemptScoreMessageAsync.class, attempt -> {

			processAttemptScoreAsync(attempt);

		}).match(AttemptScoreCalculatedMessage.class, calculated -> {

			this.processAttemptScoreCalculated(calculated);

		}).match(AttemptList.class, attempts -> {

			processAttemptList(attempts);

		}).matchAny(unknown -> {
			String messageType = unknown.getClass().getName();
			System.out.println("TopActor: Unknown Message: " + messageType);
		}).build();

	}

	private void procesAttemptScore(AttemptScoreMessage message) {
		// There are no additional steps in this one so just
		ActorRef getAttempts = context().actorOf(
				Props.create(ReferenceActor.class), "getAttempts");
		// Invoke the getAttempts actor
		System.out.println("TopActor: sending message to getAttempts");
		getAttempts.tell(message, self());

	}

	private void processAttemptScoreAsync(AttemptScoreMessageAsync message) {
		System.out.println("TopActor: AttemptScoreAsync received! "
				+ message.getAssignment_id());
		ActorRef getAttempts = context().actorOf(
				Props.create(ReferenceActor.class), "getAttempts");
		// Invoke the getAttempts actor
		System.out.println("TopActor: sending message to getAttempts");
		getAttempts.tell(message, self());
	}

	private void processAttemptScoreCalculated(
			AttemptScoreCalculatedMessage calculated) {
		System.out.println("TopActor: Got the response: "
				+ calculated.getAssignment_id());
	}

	private void processAttemptList(AttemptList attempts) {
		System.out.println("TopActor: Received an AttemptList");
		for (Attempt att : attempts)
			System.out.println("I got " + att.getAssignment_id());
	}
	
}
