package client.model.v2;

import client.utils.ModelUtils;

import java.util.List;

public class GetDeploymentsResponse {
	private List<Deployment> deployments;

	public List<Deployment> getDeployments() {
		return deployments;
	}

	public void setDeployments(List<Deployment> deployments) {
		this.deployments = deployments;
	}
	
	@Override
	public String toString() {
		return ModelUtils.toString(this);
	}
}
