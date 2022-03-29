package helio.jmapping;

import java.util.Objects;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import helio.blueprints.Components;
import helio.blueprints.components.DataHandler;
import helio.blueprints.components.DataProvider;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncorrectMappingException;

/**
 * This class represents a source of data, which consist in a
 * {@link DataProvider} that retrieves data from the source and a
 * {@link DataHandler} that filters and retrieves only the relevant information
 * from the fetched data.
 * <p>
 * Additionally, a {@link Datasource} can be synchronous (its related RDF will
 * be generated when required by a user), asynchronous, or scheduled (if it has
 * refresh value in that case the RDF is generated periodically as specified in
 * the refresh attribute).
 * <p>
 * The {@link Datasource} also store the provided {@link JsonObject}
 * configuration for the {@link DataProvider} and the {@link DataHandler}.
 *
 * @author Andrea Cimmino Arriaga
 *
 */
public class Datasource {

	private static final String KEY_TYPE = "type";

	@Expose
	private String id;

	private DataHandler handler;
	@Expose
	@SerializedName(value = "handler")
	private JsonObject handlerConfiguration;

	private DataProvider provider;
	@Expose
	@SerializedName(value = "provider")
	private JsonObject providerConfiguration;
	
	@Expose
	private Integer refresh;

	// -- Constructor

	/**
	 * Default constructor
	 */
	public Datasource() {
		// empty
	}

	/**
	 * Quick constructor
	 *
	 * @param id       the id of the {@link Datasource}
	 * @param handler  a {@link DataHandler}
	 * @param provider a {@link DataProvider}
	 */
	public Datasource(String id, DataHandler handler, DataProvider provider) {
		this.id = id;
		this.handler = handler;
		this.provider = provider;
	}

	// Getters & Setters

	/**
	 * Get the id of the {@link Datasource}
	 *
	 * @return an id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id of the {@link Datasource}
	 *
	 * @param id the id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the {@link DataHandler}
	 *
	 * @return the {@link DataHandler}
	 * @throws IncorrectMappingException 
	 * @throws ExtensionNotFoundException 
	 */
	public DataHandler getDataHandler() throws IncorrectMappingException, ExtensionNotFoundException {
		packHandler(handlerConfiguration);
		return handler;
	}

	/**
	 * Sets a new {@link DataHandler}
	 *
	 * @param dataHandler a new {@link DataHandler}
	 */
	public void setDataHandler(DataHandler dataHandler) {
		this.handler = dataHandler;
	}

	/**
	 * Gets the {@link DataProvider}
	 *
	 * @return the {@link DataProvider}
	 * @throws IncorrectMappingException 
	 * @throws ExtensionNotFoundException 
	 */
	public DataProvider getDataProvider() throws IncorrectMappingException, ExtensionNotFoundException {
		packProvider(providerConfiguration);
		return provider;
	}

	/**
	 * Sets a new {@link DataProvider}
	 *
	 * @param dataProvider a new {@link DataProvider}
	 */
	public void setDataProvider(DataProvider dataProvider) {
		this.provider = dataProvider;
	}

	/**
	 * Gets the refresh time, null if the {@link Datasource} is synchronous or
	 * asynchronous
	 *
	 * @return the refresh time
	 */
	public Integer getRefresh() {
		return refresh;
	}

	/**
	 * Sets a refresh time for a scheduled {@link Datasource}
	 *
	 * @param refresh the refresh time
	 */
	public void setRefresh(int refresh) {
		this.refresh = refresh;
	}

	/**
	 * Gets the configuration of the {@link DataHandler}
	 *
	 * @return a {@link JsonObject} configuration
	 */
	public JsonObject getHandlerConfiguration() {
		return handlerConfiguration;
	}

	/**
	 * Sets a new {@link JsonObject} configuration for the {@link DataHandler}
	 *
	 * @param handlerConfiguration a new {@link JsonObject} configuration
	 * @throws IncorrectMappingException 
	 * @throws ExtensionNotFoundException 
	 */
	public void setHandlerConfiguration(JsonObject handlerConfiguration) throws IncorrectMappingException, ExtensionNotFoundException {
		packHandler(handlerConfiguration);
		this.handlerConfiguration = handlerConfiguration;
	}

	private void packHandler(JsonObject json) throws IncorrectMappingException, ExtensionNotFoundException {
			if (!json.has(KEY_TYPE)) {
				throw new IncorrectMappingException(
						"the JSON document for the provider must contain the mandatory key 'type' with a correct value");
			} else {
				String name = json.get(KEY_TYPE).getAsString();
				if (name != null && !name.isEmpty()) {
					if(!Components.getDataHandlers().containsKey(name))
						Components.load(name);
					handler = Components.getDataHandlers().get(name);
					if (handler == null)
						throw new IncorrectMappingException("Data handler specified in the mapping does not exist: "+name);
					handler.configure(json);
				} else {
					throw new IncorrectMappingException("Value of key 'type' can not be null or blank");
				}
			}
	}

	/**
	 * Gets the configuration of the {@link DataProvider}
	 *
	 * @return a {@link JsonObject} configuration
	 */
	public JsonObject getProviderConfiguration() {
		return providerConfiguration;
	}

	/**
	 * Sets a new {@link JsonObject} configuration for the {@link DataProvider}
	 *
	 * @param providerConfiguration a new {@link JsonObject} configuration
	 * @throws IncorrectMappingException 
	 * @throws ExtensionNotFoundException 
	 */
	public void setProviderConfiguration(JsonObject providerConfiguration) throws IncorrectMappingException, ExtensionNotFoundException {
		packProvider(providerConfiguration);
		this.providerConfiguration = providerConfiguration;
	}

	private void packProvider(JsonObject json) throws IncorrectMappingException, ExtensionNotFoundException {
			if (!json.has(KEY_TYPE)) {
				throw new IncorrectMappingException(
						"the JSON document for the provider must contain the mandatory key 'type' with a correct value");
			} else {
				String name = json.get(KEY_TYPE).getAsString();
				if (name != null && !name.isEmpty()) {
					if(!Components.getDataProviders().containsKey(name))
						Components.load(name);
					provider = Components.getDataProviders().get(name);
					if (provider == null)
						throw new IncorrectMappingException("Data provider specified in the mapping does not exist:" +name);
					provider.configure(json);
				} else {
					throw new IncorrectMappingException("Value of key 'type' can not be null or blank");
				}
			}
	}


	// -- Ancillary

	@Override
	public int hashCode() {
		return Objects.hash(handlerConfiguration, id, providerConfiguration);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		Datasource other = (Datasource) obj;
		return Objects.equals(handlerConfiguration, other.handlerConfiguration) && Objects.equals(id, other.id)
				&& Objects.equals(providerConfiguration, other.providerConfiguration);
	}

}
