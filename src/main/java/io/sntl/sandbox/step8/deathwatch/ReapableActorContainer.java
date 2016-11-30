package io.sntl.sandbox.step8.deathwatch;

import java.util.Date;

import akka.actor.ActorRef;

public class ReapableActorContainer {
	private Date createTime;
	private ActorRef actor;

	public ReapableActorContainer(ActorRef a) {
		actor = a;
		createTime = new Date();
		System.out.println("ReapableActorContainer created at " + createTime);
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public ActorRef getActor() {
		return actor;
	}

	public void setActor(ActorRef actor) {
		this.actor = actor;
	}

}
