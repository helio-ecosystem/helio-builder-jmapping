package helio.tests;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import org.apache.jena.atlas.io.InputStreamBuffered;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.resultset.ResultsFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import helio.blueprints.TranslationUnit;
import helio.blueprints.UnitBuilder;
import helio.blueprints.components.ComponentType;
import helio.blueprints.components.Components;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.blueprints.exceptions.TranslationUnitExecutionException;

public class TestUtils {

	static Logger logger = LoggerFactory.getLogger(TestUtils.class);
	private static ExecutorService scheduledExecutorService =  Executors.newFixedThreadPool(30);

	static {
		try {
			Components.registerAndLoad(
					"/Users/andreacimmino/Desktop/helio-handler-csv-0.1.0.jar",
					"handlers.CsvHandler", 
					ComponentType.HANDLER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Components.registerAndLoad(
					"/Users/andreacimmino/Desktop/helio-handler-jayway-0.1.0.jar",
					"handlers.JsonHandler", 
					ComponentType.HANDLER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad(
					null,
					"helio.jmapping.functions.FileProvider", 
					ComponentType.PROVIDER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad(
					null,
					"helio.jmapping.processor.JMappingProcessor", 
					ComponentType.BUILDER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Model readModel(String file) {
		FileInputStream out;
		Model expected = ModelFactory.createDefaultModel();
		try {
			out = new FileInputStream(file);
			expected = ModelFactory.createDefaultModel();
			expected.read(out, "http://helio.linkeddata.es/resources/", "TURTLE");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return expected;
	}

	public static String readFile(String fileName) {
		StringBuilder data = new StringBuilder();
		// 1. Read the file
		try {
			FileReader file = new FileReader(fileName);
			BufferedReader bf = new BufferedReader(file);
			// 2. Accumulate its lines in the data var
			bf.lines().forEach(line -> data.append(line).append("\n"));
			bf.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data.toString();
	}

	public static Set<TranslationUnit> processJMapping(String mappingFile) {
		try {
		String mappingContent = readFile(mappingFile);
		UnitBuilder processor = Components.getMappingProcessors().get("JMappingProcessor");
		return processor.parseMapping(mappingContent);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Model generateRDFSynchronously(Set<TranslationUnit> units) {
		Model model = ModelFactory.createDefaultModel();
		try {
			
			long startTime2 = System.nanoTime();
			units.stream().forEach(unit -> {
				try {
					Future<?> f = scheduledExecutorService.submit(unit.getTask());
					f.get();
					
					unit.getTranslations().forEach(fragment -> {
						try {
						model.read(new ByteArrayInputStream(fragment.getBytes()), null, "TURTLE");}
						catch(Exception e) {
							System.out.println(fragment);
							};
						});
					unit.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			});
			long endTime2 = (System.nanoTime()- startTime2) / 1000000;
			System.out.println("Translation time: "+endTime2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	public static Boolean compareModels(Model model1, Model model2) {
		if (model1 == null || model2 == null || model1.isEmpty() || model2.isEmpty())
			return false;
		Boolean model2Contains1 = contains(model1, model2);
		System.out.println("-----");
		Boolean model1Contains2 = contains(model2, model1);

		return model2Contains1 && model1Contains2;
	}

	public static Boolean contains(Model model1, Model model2) {
		Writer writer = new StringWriter();
		model1.write(writer, "NT");
		String[] triplet = writer.toString().split("\n");
		boolean result = true;
		for (String element : triplet) {
			String query = "ASK {\n" + element+ "\n}";
			try {
				Boolean aux = QueryExecutionFactory.create(query, model2).execAsk();
				if (!aux) {
					result = false;
					// System.out.println("Not present in model 2:"+ element);
					// break;
				}
			} catch (Exception e) {
				System.out.println(element);
				System.out.println(query);
				System.out.println(e.toString());
			}
		}
		return result;
	}

	private static boolean compare(RDFNode obj1, RDFNode obj2) {
		Boolean equal = false;
		if (obj1.isLiteral() && obj2.isLiteral()) {
			equal = obj1.asLiteral().getLexicalForm().equals(obj2.asLiteral().getLexicalForm());
		} else if (obj1.isResource() && obj2.isResource() && !obj1.isAnon() && !obj2.isAnon()) {
			equal = obj1.equals(obj2);
		}
		if (obj1.isAnon() && obj2.isAnon()) {
			equal = true;
		}

		return equal;
	}

}
