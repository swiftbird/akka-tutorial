package io.sntl.sandbox.step6.actorfutures;

/*
 * This represents the initial score message coming into the system from the scoring engine
 */

public class AttemptScoreMessage {

	private String source_id;
	private String learning_context_id;
	private String assignment_id;
	private String user_id;
	private int points_earned;
	private int extra_credit_points_earned;
	private String comments;
	private int attempt_number;

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public String getLearning_context_id() {
		return learning_context_id;
	}

	public void setLearning_context_id(String learning_context_id) {
		this.learning_context_id = learning_context_id;
	}

	public String getAssignment_id() {
		return assignment_id;
	}

	public void setAssignment_id(String assignment_id) {
		this.assignment_id = assignment_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getPoints_earned() {
		return points_earned;
	}

	public void setPoints_earned(int points_earned) {
		this.points_earned = points_earned;
	}

	public int getExtra_credit_points_earned() {
		return extra_credit_points_earned;
	}

	public void setExtra_credit_points_earned(int extra_credit_points_earned) {
		this.extra_credit_points_earned = extra_credit_points_earned;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getAttempt_number() {
		return attempt_number;
	}

	public void setAttempt_number(int attempt_number) {
		this.attempt_number = attempt_number;
	}

}
