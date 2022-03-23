package helio.jmapping;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * This object represents a rule that generates from heterogeneous data an RDF triple
 * @author Andrea Cimmino
 */
public class TranslationRule {

	// -- Attributes
	@Expose
	private String object; // mandatory
	@Expose
	private String predicate; // mandatory
	@Expose
	@SerializedName(value = "literal", alternate = "is_literal")
	private Boolean isLiteral; // mandatory
	@Expose
	@SerializedName(value = "datatype")
	private String dataType; // optional; disjoint with language
	@Expose
	@SerializedName(value = "lang")
	private String language; // optional; disjoint with language

	// -- Constructor

	/**
	 *
	 */
	public TranslationRule() {
		super();
	}

	// -- Getters and Setters

	/**
	 *
	 * @return
	 */
	public String getObject() {
		return object;
	}

	/**
	 *
	 * @param expression
	 */
	public void setObject(String expression) {
		this.object = expression;
	}

	/**
	 *
	 * @return
	 */
	public String getPredicate() {
		return predicate;
	}

	/**
	 *
	 * @param predicate
	 */
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	/**
	 *
	 * @return
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 *
	 * @param dataType
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 *
	 * @return
	 */
	public Boolean getIsLiteral() {
		return isLiteral;
	}

	/**
	 *
	 * @param isLiteral
	 */
	public void setIsLiteral(Boolean isLiteral) {
		this.isLiteral = isLiteral;
	}

	/**
	 *
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 *
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}


	// Other methods
	/**
	 *
	 * @return
	 */
	public List<String> fetchDataReferences(){
		List<String> dataReferences = new ArrayList<>();
		dataReferences.addAll(Expresions.extractDataReferences(predicate));
		dataReferences.addAll(Expresions.extractDataReferences(object));
		if(dataType!=null && !dataType.isEmpty())
			dataReferences.addAll(Expresions.extractDataReferences(dataType));
		if(language!=null && !language.isEmpty())
			dataReferences.addAll(Expresions.extractDataReferences(language));
		return dataReferences;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((isLiteral == null) ? 0 : isLiteral.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		TranslationRule other = (TranslationRule) obj;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		if (isLiteral == null) {
			if (other.isLiteral != null)
				return false;
		} else if (!isLiteral.equals(other.isLiteral))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		return true;
	}












}
