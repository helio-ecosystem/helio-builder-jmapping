package helio.jmapping;

import java.util.Objects;
import java.util.Set;

public class TripleMapping {
	
	protected Datasource datasource;
	protected String template;
	protected Set<String> dataReferences; // me las dan a mano, se extraen automaticamente de la plantilla, o en la plantilla se llama al datasource en cuestion
	
	
	public TripleMapping() {
		
	}

	public TripleMapping(Datasource datasource2, String template, Set<String> dataReferences) {
		super();
		this.datasource = datasource2;
		this.template = template;
		this.dataReferences = dataReferences;
	}


	

	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Set<String> getDataReferences() {
		return dataReferences;
	}

	public void setDataReferences(Set<String> dataReferences) {
		this.dataReferences = dataReferences;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataReferences, datasource, template);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TripleMapping other = (TripleMapping) obj;
		return Objects.equals(dataReferences, other.dataReferences) && Objects.equals(datasource, other.datasource)
				&& Objects.equals(template, other.template);
	}
	

	
}
