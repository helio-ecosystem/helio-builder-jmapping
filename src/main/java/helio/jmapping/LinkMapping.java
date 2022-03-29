package helio.jmapping;

import java.util.Set;

public class LinkMapping extends TripleMapping{
	
	private Datasource datasourceTarget;


	public LinkMapping(Datasource datasource, String template, Set<String> dataReferences, Datasource datasourceTarget) {
		super();
		this.datasourceTarget = datasourceTarget;
	}


	public Datasource getDatasourceTarget() {
		return datasourceTarget;
	}


	public void setDatasourceTarget(Datasource datasourceTarget) {
		this.datasourceTarget = datasourceTarget;
	}
	
	
	

}
