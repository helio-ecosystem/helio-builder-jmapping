package helio.jmapping;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This object represents a rule to link two RDF resources using a fuzzy similarity expression
 * @author Andrea Cimmino
 *
 */
public class LinkRule {

	@Expose
	@SerializedName(value = "source")
	private String sourceNamedGraph;
	@Expose
	@SerializedName(value = "target")
	private String targetNamedGraph;
	@Expose
	@SerializedName(value = "condition")
	private String expression;
	@Expose
	@SerializedName(value = "predicate")
	private String predicate;
	@Expose
	@SerializedName(value = "inverse")
	private String inversePredicate;

	/**
	 * Default constructor
	 */
	public LinkRule() {
		super();
	}

	// Getters & Setters
	/**
	 *
	 * @return
	 */
	public String getSourceNamedGraph() {
		return sourceNamedGraph;
	}
	/**
	 *
	 * @param sourceNamedGraph
	 */
	public void setSourceNamedGraph(String sourceNamedGraph) {
		this.sourceNamedGraph = sourceNamedGraph;
	}
	/**
	 *
	 * @return
	 */
	public String getTargetNamedGraph() {
		return targetNamedGraph;
	}
	/**
	 *
	 * @param targetNamedGraph
	 */
	public void setTargetNamedGraph(String targetNamedGraph) {
		this.targetNamedGraph = targetNamedGraph;
	}
	/**
	 *
	 * @return
	 */
	public String getExpression() {
		return expression;
	}
	/**
	 *
	 * @param expression
	 */
	public void setExpression(String expression) {
		this.expression = expression;
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
	public String getInversePredicate() {
		return inversePredicate;
	}
	/**
	 *
	 * @param inversePredicate
	 */
	public void setInversePredicate(String inversePredicate) {
		this.inversePredicate = inversePredicate;
	}

	// Ancillary methods

	@Override
	public String toString() {
		return "LinkRule [sourceNamedGraph=" + sourceNamedGraph + ", targetNamedGraph=" + targetNamedGraph
				+ ", expression=" + expression + ", predicate=" + predicate + ", inversePredicate=" + inversePredicate
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
		result = prime * result + ((inversePredicate == null) ? 0 : inversePredicate.hashCode());
		result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
		result = prime * result + ((sourceNamedGraph == null) ? 0 : sourceNamedGraph.hashCode());
		result = prime * result + ((targetNamedGraph == null) ? 0 : targetNamedGraph.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		LinkRule other = (LinkRule) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		if (inversePredicate == null) {
			if (other.inversePredicate != null)
				return false;
		} else if (!inversePredicate.equals(other.inversePredicate))
			return false;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		if (sourceNamedGraph == null) {
			if (other.sourceNamedGraph != null)
				return false;
		} else if (!sourceNamedGraph.equals(other.sourceNamedGraph))
			return false;
		if (targetNamedGraph == null) {
			if (other.targetNamedGraph != null)
				return false;
		} else if (!targetNamedGraph.equals(other.targetNamedGraph))
			return false;
		return true;
	}







}
