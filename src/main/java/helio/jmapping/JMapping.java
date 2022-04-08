package helio.jmapping;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.jmapping.processor.JMappingProcessor;

/**
 * This class codifies a set of objects used to to generate RDF.<p>
 * The {@link JMapping} consist on a list of {@link Datasource}, a list of {@link TranslationRules}, and a list of {@link LinkRule}.
 * @author Andrea Cimmino
 *
 */
public class JMapping  {

	@Expose
	@SerializedName(value = "datasources")
	private List<Datasource> datasources;
	@Expose
	@SerializedName(value = "rules", alternate = "resource_rules")
	private List<TranslationRules> translationRules;
	@Expose
	@SerializedName(value = "links", alternate = "link_rules")
	private List<LinkRule> linkRules;

	/**
	 * Initializes an empty mapping
	 */
	public JMapping() {
		datasources = new ArrayList<>();
		translationRules = new ArrayList<>();
		linkRules = new ArrayList<>();
	}

	/**
	 * This method merges the provided mapping with the one codified by this object
	 * @param mapping a {@link JMapping} to be merged with this
	 */
	public void merge(JMapping mapping) {
		datasources.addAll(mapping.getDatasources());
		translationRules.addAll(mapping.getTranslationRules());
		linkRules.addAll(mapping.getLinkRules());
	}

	/**
	 * Gets the {@link Datasource} of this {@link JMapping}
	 * @return a list of {@link Datasource}
	 */
	public List<Datasource> getDatasources() {
		return datasources;
	}

	/**
	 * Sets the {@link Datasource} of this {@link JMapping}
	 * @param datasources a new list of {@link Datasource}
	 */
	public void setDatasources(List<Datasource> datasources) {
		this.datasources = datasources;
	}

	/**
	 * Gets the {@link TranslationRules} of this {@link JMapping}
	 * @return a list of {@link TranslationRules}
	 */
	public List<TranslationRules> getTranslationRules() {
		return translationRules;
	}

	/**
	 * Sets the {@link TranslationRules} of this {@link JMapping}
	 * @param translationRules a new list of {@link TranslationRules}
	 */
	public void setTranslationRules(List<TranslationRules> translationRules) {
		this.translationRules = translationRules;
	}

	/**
	 * Gets the {@link LinkRule} of this {@link JMapping}
	 * @return a list of {@link LinkRule}
	 */
	public List<LinkRule> getLinkRules() {
		return linkRules;
	}

	/**
	 * Sets the {@link LinkRule} of this {@link JMapping}
	 * @param linkRules a new list of {@link LinkRule}
	 */
	public void setLinkRules(List<LinkRule> linkRules) {
		this.linkRules = linkRules;
	}

	/**
	 * This method checks that the mapping is correct and valid
	 * @throws IncorrectMappingException
	 * @throws ExtensionNotFoundException
	 */
	public void checkMapping() throws IncorrectMappingException, ExtensionNotFoundException {
		if(this.datasources.isEmpty() && this.translationRules.isEmpty() && this.linkRules.isEmpty())
			throw new IncorrectMappingException("The mapping is empty");
		int size = this.datasources.size();
		for(int index=0; index < size; index++) {
			Datasource ds = this.datasources.get(index);
			ds.getDataHandler();
			ds.getDataProvider();
		}
	}

	// -- Ancillary


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datasources == null) ? 0 : datasources.hashCode());
		result = prime * result + ((linkRules == null) ? 0 : linkRules.hashCode());
		result = prime * result + ((translationRules == null) ? 0 : translationRules.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		JMapping other = (JMapping) obj;
		if (datasources == null) {
			if (other.datasources != null)
				return false;
		} else if (!datasources.equals(other.datasources))
			return false;
		if (linkRules == null) {
			if (other.linkRules != null)
				return false;
		} else if (!linkRules.equals(other.linkRules))
			return false;
		if (translationRules == null) {
			if (other.translationRules != null)
				return false;
		} else if (!translationRules.equals(other.translationRules))
			return false;
		return true;
	}



	public String toJson() {
		return JMappingProcessor.GSON.toJson(this);
	}



}


