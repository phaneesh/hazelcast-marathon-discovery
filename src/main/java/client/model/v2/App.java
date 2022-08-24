package client.model.v2;


import client.utils.ModelUtils;

import java.util.*;

public class App {
	public static class Deployment {
		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return ModelUtils.toString(this);
		}
	}

	private String id;
	private String cmd;
	private List<String> args;
	private Integer instances;
	private Double cpus;
	private Double mem;
	private Collection<String> uris;
	private List<List<String>> constraints;
	private Collection<String> acceptedResourceRoles;
	private Container container;
	private Map<String, String> env;
	private Map<String, String> labels;
	private String executor;
	private List<Integer> ports;
	private Boolean requirePorts;
	private Collection<String> dependencies;
	private Integer backoffSeconds;
	private Double backoffFactor;
	private Integer maxLaunchDelaySeconds;
	private Collection<Task> tasks;
	private Integer tasksStaged;
	private Integer tasksRunning;
	private Integer tasksHealthy;
	private Integer tasksUnhealthy;
	private List<HealthCheck> healthChecks;
	private UpgradeStrategy upgradeStrategy;

	private List<Deployment> deployments;
	private TaskFailure lastTaskFailure;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public List<String> getArgs() {
		return args;
	}

	public void setArgs(final List<String> args) {
		this.args = args;
	}

	public Integer getInstances() {
		return instances;
	}

	public void setInstances(Integer instances) {
		this.instances = instances;
	}

	public Double getCpus() {
		return cpus;
	}

	public void setCpus(Double cpus) {
		this.cpus = cpus;
	}

	public Double getMem() {
		return mem;
	}

	public void setMem(Double mem) {
		this.mem = mem;
	}

	public Collection<String> getUris() {
		return uris;
	}

	public void setUris(Collection<String> uris) {
		this.uris = uris;
	}

	public List<List<String>> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<List<String>> constraints) {
		this.constraints = constraints;
	}

	public void addConstraint(String attribute, String operator, String value) {
		if (this.constraints == null) {
			this.constraints = new ArrayList<List<String>>();
		}
		List<String> constraint = new ArrayList<String>(3);
		constraint.add(attribute == null ? "" : attribute);
		constraint.add(operator == null ? "" : operator);
		constraint.add(value == null ? "" : value);
		this.constraints.add(constraint);
	}

	public Collection<String> getAcceptedResourceRoles() {
		return acceptedResourceRoles;
	}

	public void setAcceptedResourceRoles(Collection<String> acceptedResourceRoles) {
		this.acceptedResourceRoles = acceptedResourceRoles;
	}

	public void addAcceptedResourceRole(String role) {
		if (this.acceptedResourceRoles == null) {
			this.acceptedResourceRoles = new HashSet<String>();
		}

		this.acceptedResourceRoles.add(role);
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public Map<String, String> getEnv() {
		return env;
	}

	public void setEnv(Map<String, String> env) {
		this.env = env;
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public void addLabel(final String key, final String value) {
		if (key != null && key.trim().length() > 0) {
			if (this.labels == null) {
				this.labels = new HashMap<String, String>();
			}
			this.labels.put(key, value == null ? "" : value);
		}
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public List<Integer> getPorts() {
		return ports;
	}

	public void setPorts(List<Integer> ports) {
		this.ports = ports;
	}

	public Boolean getRequirePorts() {
		return requirePorts;
	}

	public void setRequirePorts(final Boolean requirePorts) {
		this.requirePorts = requirePorts;
	}

	public Collection<String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(final List<String> dependencies) {
		this.dependencies = dependencies;
	}

	public void addDependency(final String dependency) {
		if (dependency != null && dependency.trim().length() > 0) {
			if (this.dependencies == null) {
				this.dependencies = new HashSet<String>();
			}
			this.dependencies.add(dependency);
		}
	}

	public Integer getBackoffSeconds() {
		return backoffSeconds;
	}

	public void setBackoffSeconds(final Integer backoffSeconds) {
		this.backoffSeconds = backoffSeconds;
	}

	public Double getBackoffFactor() {
		return backoffFactor;
	}

	public void setBackoffFactor(final Double backoffFactor) {
		this.backoffFactor = backoffFactor;
	}

	public Integer getMaxLaunchDelaySeconds() {
		return maxLaunchDelaySeconds;
	}

	public void setMaxLaunchDelaySeconds(final Integer maxLaunchDelaySeconds) {
		this.maxLaunchDelaySeconds = maxLaunchDelaySeconds;
	}

	public void addUri(String uri) {
		if (this.uris == null) {
			this.uris = new ArrayList<String>();
		}
		this.uris.add(uri);
	}

	public void addPort(int port) {
		if (this.ports == null) {
			this.ports = new ArrayList<Integer>();
		}
		this.ports.add(port);
	}

	public Collection<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Collection<Task> tasks) {
		this.tasks = tasks;
	}

	public Integer getTasksStaged() {
		return tasksStaged;
	}

	public void setTasksStaged(Integer tasksStaged) {
		this.tasksStaged = tasksStaged;
	}

	public Integer getTasksRunning() {
		return tasksRunning;
	}

	public void setTasksRunning(Integer tasksRunning) {
		this.tasksRunning = tasksRunning;
	}

	public Integer getTasksHealthy() {
		return tasksHealthy;
	}

	public void setTasksHealthy(Integer tasksHealthy) {
		this.tasksHealthy = tasksHealthy;
	}

	public Integer getTasksUnhealthy() {
		return tasksUnhealthy;
	}

	public void setTasksUnhealthy(Integer tasksUnhealthy) {
		this.tasksUnhealthy = tasksUnhealthy;
	}

	public List<HealthCheck> getHealthChecks() {
		return healthChecks;
	}

	public void setHealthChecks(List<HealthCheck> healthChecks) {
		this.healthChecks = healthChecks;
	}

	public UpgradeStrategy getUpgradeStrategy() {
		return upgradeStrategy;
	}

	public List<Deployment> getDeployments() {
		return deployments;
	}

	public void setDeployments(List<Deployment> deployments) {
		this.deployments = deployments;
	}

	public void setUpgradeStrategy(final UpgradeStrategy upgradeStrategy) {
		this.upgradeStrategy = upgradeStrategy;
	}

	public TaskFailure getLastTaskFailure() {
		return lastTaskFailure;
	}

	public void setLastTaskFailure(TaskFailure lastTaskFailure) {
		this.lastTaskFailure = lastTaskFailure;
	}

	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}

}
