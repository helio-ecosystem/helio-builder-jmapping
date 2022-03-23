package helio.jmapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import helio.blueprints.exceptions.IncorrectMappingException;


/**
 * This object represents a group of {@link TranslationRule} to generate a piece of RDF
 * @author Andrea Cimmino
 *
 */
public class TranslationRules {

	// -- Attributes
	@Expose
	private String id; // An Id that identifies this object
	@Expose
	private String subject; // if contains '{...}' then is a template otherwise is constant
	@Expose
	private List<TranslationRule> properties;
	@Expose
	@SerializedName(value = "datasource", alternate = "datasource_id")
	private String datasource;


	// -- Constructor
	/**
	 *
	 */
	public TranslationRules() {
		super();
		properties = new ArrayList<>();
	}

	// -- Getters & Setters

	/**
	 * This method confirms if a {@link Datasource} is binded to this {@link TranslationRules}
	 * @param dataSource the if of the {@link Datasource}
	 * @return true if the {@link Datasource} is binded to the {@link TranslationRules}
	 * @throws IncorrectMappingException 
	 */
	public Boolean hasDataSource(String dataSource) throws IncorrectMappingException {
		if(this.datasource==null)
			throw new IncorrectMappingException("Translation rules must have associated a datasource using the key 'datasource'");
		return this.datasource.equals(dataSource);
	}

	/**
	 * Gets the id of this {@link TranslationRules}
	 * @return the id of this {@link TranslationRules}
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id for this {@link TranslationRules}
	 * @param resourceRuleId the new id
	 */
	public void setId(String resourceRuleId) {
		this.id = resourceRuleId;
	}

	/**
	 * Gets the subject template
	 * @return the subject template
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets a new subject template
	 * @param subjectTemplate the new subject template
	 */
	public void setSubject(String subjectTemplate) {
		this.subject = subjectTemplate;
	}

	/**
	 * Gets the list of {@link TranslationRule}
	 * @return the list of {@link TranslationRule}
	 */
	public List<TranslationRule> getProperties() {
		return properties;
	}

	/**
	 * Sets a new list of {@link TranslationRule}
	 * @param properties the new list of {@link TranslationRule}
	 */
	public void setProperties(List<TranslationRule> properties) {
		this.properties = properties;
	}

	/**
	 * Gets the {@link Datasource} id binded to this {@link TranslationRule} 
	 * @return a {@link Datasource} id
	 */
	public String getDatasourceId() {
		return datasource;
	}

	/**
	 * Sets a new id for a {@link Datasource}
	 * @param datasourceId an id of a new {@link Datasource}
	 */
	public void setDatasourceId(String datasourceId) {
		this.datasource = datasourceId;
	}

	// -- Other methods
	/**
	 * This method returns all the data references held by the elements of this {@link TranslationRules}
	 * @return a list of data references
	 */
	public Set<String> getDataReferences(){
		Set<String> dataReferences = this.properties.stream().flatMap(elem -> elem.fetchDataReferences().stream()).collect(Collectors.toSet());
		dataReferences.addAll(Expresions.extractDataReferences(this.subject));
		return dataReferences;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		TranslationRules other = (TranslationRules) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}







}
