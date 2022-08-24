package client.model.v2;



import client.utils.ModelUtils;

import java.util.Collection;
import java.util.List;

public class Deployment {
	private Collection<String> affectedApps;
	private String id;
	private List<Step> steps;
	private Collection<Action> currentActions;
	private String version;
	private Integer currentStep;
	private Integer totalSteps;
	
	public Collection<String> getAffectedApps() {
		return affectedApps;
	}

	public void setAffectedApps(Collection<String> affectedApps) {
		this.affectedApps = affectedApps;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

	public Collection<Action> getCurrentActions() {
		return currentActions;
	}

	public void setCurrentActions(Collection<Action> currentActions) {
		this.currentActions = currentActions;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(Integer currentStep) {
		this.currentStep = currentStep;
	}

	public Integer getTotalSteps() {
		return totalSteps;
	}

	public void setTotalSteps(Integer totalSteps) {
		this.totalSteps = totalSteps;
	}

	public class Step {
		private List<Action> actions;
		
		public List<Action> getActions() {
			return actions;
		}

		public void setActions(List<Action> actions) {
			this.actions = actions;
		}

		@Override
		public String toString() {
			return ModelUtils.toString(this);
		}
	}

	public class Action {
		private String type;
		private String app;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getApp() {
			return app;
		}

		public void setApp(String app) {
			this.app = app;
		}

		@Override
		public String toString() {
			return ModelUtils.toString(this);
		}
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
