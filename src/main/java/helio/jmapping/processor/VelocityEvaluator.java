package helio.jmapping.processor;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

import helio.blueprints.components.Components;
import helio.jmapping.functions.HF;



public class VelocityEvaluator {

	// -- Attributes
	protected static final org.apache.velocity.app.VelocityEngine velocityEngine;
	protected static final StringResourceRepository templateRepository;

	static {
		velocityEngine = new org.apache.velocity.app.VelocityEngine();
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "string");
		velocityEngine.addProperty("string.resource.loader.class", StringResourceLoader.class.getName());
		velocityEngine.addProperty("string.resource.loader.repository.static", "false");
		try {
			velocityEngine.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		templateRepository = (StringResourceRepository) velocityEngine.getApplicationAttribute(StringResourceLoader.REPOSITORY_NAME_DEFAULT);

	}


	// -- Constructor

	private VelocityEvaluator() {
		super();
	}

	// -- CRUD over templates

	public static void registerVelocityTemplate(String templateId, String velocityTemplate) {
		templateRepository.putStringResource(templateId, velocityTemplate);
	}

	public static void removeVelocityTemplate(String templateId) {
		templateRepository.removeStringResource(templateId);
	}

	public static boolean existsVelocityTemplate(String templateId) {
		return getVelocityTemplate(templateId)!=null;
	}

	public static Template getVelocityTemplate(String templateId) {
		try {
			return velocityEngine.getTemplate(templateId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// -- Evaluation over templates

	public static StringWriter evaluateTemplate(String templateId, Map<String, List<String>> dataReferences)  {
		VelocityContext context = new VelocityContext();
		context.put("HF", new HF());
		context.put("ref", context); // 
		// Load functions
		Components.getMappingFunctions().entrySet().parallelStream().filter(f -> !f.getClass().equals(HF.class)).forEach(entry -> context.put(entry.getKey(), entry.getValue()));
		// Load references
		dataReferences.entrySet().parallelStream().forEach(entry -> context.put(entry.getKey(), entry.getValue()));
		// Solve template
		StringWriter writer = new StringWriter();
		Template template = getVelocityTemplate(templateId);
		try {
			template.merge( context, writer );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer;
	}


}
