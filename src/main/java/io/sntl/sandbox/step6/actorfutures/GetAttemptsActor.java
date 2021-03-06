package io.sntl.sandbox.step6.actorfutures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;

import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.util.Timeout;

public class GetAttemptsActor extends AbstractActor {

	public GetAttemptsActor() {
		receive(
		// do something
		ReceiveBuilder
				.match(AttemptScoreMessage.class, attempt -> {

					AttemptList results = processAttemptScore(attempt);
					sender().tell(results, self());
					context().stop(self());

				})
				.match(AttemptScoreMessageAsync.class, attempt -> {
					processAttemptScoreAsync(attempt);

				})
				.matchAny(
						unknown -> {
							System.out
									.println("GetAttemptsForActor: Unknown Message "
											+ unknown);
						}).build());
	}

	private AttemptList processAttemptScore(AttemptScoreMessage attempt) {
		// create a new actor
		// return a list of attempts

		// System.out.println("Called GetAttemptsActor "
		// + attempt.getAssignment_id());
		// new PauseALittle().call();

		System.out.println("GetAttemptsActor invoked");
		AttemptScoreCalculatedMessage result = new AttemptScoreCalculatedMessage();
		result.setAssignment_id("newID");

		// sender.tell(result, self);
		// do the Parallel Fetch of Attempts

		AttemptList results = new AttemptList();
		for (int x = 0; x < 5; x++) {
			Attempt a = new Attempt();
			a.setAttempt_number(x);
			a.setAssignment_id("Att" + x);
			results.add(a);
		}
		
		return results;

		
	}

	private void processAttemptScoreAsync(AttemptScoreMessageAsync attempt) {
		System.out.println("I have an attemptscoreasync! "
				+ attempt.getAssignment_id());

		final Timeout t = new Timeout(Duration.create(5, TimeUnit.SECONDS));

		ActorRef getOneAttempt = context().actorOf(
				Props.create(GetOneAttemptActor.class));
		final Future<Object> result = ask(getOneAttempt,
				attempt.getAssignment_id(), t);
		System.out.println("GetAttemptsActor self: " + self().toString());

		// Both of these work
		// pipe(result,
		// context().system().dispatcher()).to(self());
		pipe(result, context().dispatcher()).to(self());

		// ------------- This is doing a bundle of things
		// and then sequencing the result -------------

		System.out.println("I have piped so I am doing more stuff");
		// The bundle of futures
		final ArrayList<Future<Object>> futures = new ArrayList<Future<Object>>();
		for (int i = 0; i < 5; i++) {
			// create a new "getOneAttemptActor"
			ActorRef getAttempt = context().actorOf(
					Props.create(GetOneAttemptActor.class));
			// Doing an ask to make sure we can sync the
			// results
			// back
			futures.add(ask(getAttempt, "GeneralMessage" + i, t));

		}
		// Defining the future that returns when all sub
		// futures are done

		final Future<Iterable<Object>> aggregate = Futures.sequence(futures,
				context().dispatcher());

		final Future<AttemptList> completed = aggregate.map(
				new Mapper<Iterable<Object>, AttemptList>() {
					AttemptList result = new AttemptList();
					int attemptsFetched = 0;

					public AttemptList apply(Iterable<Object> coll) {
						System.out.println("I am aggregating!");
						final Iterator<Object> it = coll.iterator();
						while (it.hasNext()) {
							it.next();
							attemptsFetched++;
							Attempt a = new Attempt();
							a.setAssignment_id("Assignment" + attemptsFetched);
							a.setAttempt_number(attemptsFetched);

							result.add(a);
						}

						return result;
					}
				}, context().dispatcher());

		// pipe creates an onComplete handler
		pipe(completed, context().dispatcher()).to(sender());
		System.out.println("Rinku says do something");
	}

}
