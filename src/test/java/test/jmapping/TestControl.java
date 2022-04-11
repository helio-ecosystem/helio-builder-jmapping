package test.jmapping;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Set;

import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import helio.blueprints.TranslationUnit;
import helio.blueprints.components.ComponentType;
import helio.blueprints.components.Components;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.TranslationUnitExecutionException;
import helio.jmapping.processor.JMappingProcessor;

public class TestControl {


	static {
		try {
			Components.registerAndLoad("/Users/andreacimmino/Desktop/helio-handler-csv-0.0.3.jar",
					"handlers.CsvHandler", ComponentType.HANDLER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad(
					"https://github.com/helio-ecosystem/helio-handler-jayway/releases/download/v0.0.2/helio-handler-jayway-0.0.2.jar",
					"handlers.JsonHandler", ComponentType.HANDLER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad(
					"https://github.com/helio-ecosystem/helio-handler-jsoup/releases/download/v0.0.1/helio-handler-jsoup-0.0.1.jar",
					"handlers.JsoupHandler", ComponentType.HANDLER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad(
					"https://github.com/helio-ecosystem/helio-provider-url/releases/download/v0.0.1/helio-provider-url-0.0.1.jar",
					"provider.URLProvider", ComponentType.PROVIDER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad(
					"https://github.com/helio-ecosystem/helio-handler-regex/releases/download/v0.0.1/helio-handler-regex-0.0.1.jar",
					"handlers.RegexHandler", ComponentType.HANDLER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad(
					"https://github.com/helio-ecosystem/helio-handler-xml/releases/download/v0.0.1/helio-handler-xml-0.0.1.jar",
					"handlers.XmlHandler", ComponentType.HANDLER);
		} catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}

		try {
			Components.registerAndLoad(
					"https://github.com/helio-ecosystem/helio-provider-file/releases/download/v.0.0.1/helio-provider-file-0.0.1.jar",
					"providers.FileProvider", ComponentType.PROVIDER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Components.registerAndLoad(
					"/Users/andreacimmino/Desktop/Lab/json-mapping/target/helio-processor-jmapping-0.2.2-jar-with-dependencies.jar",
					"helio.jmapping.processor.JMappingProcessor", ComponentType.PROCESSOR);
			Components.registerAndLoad("/Users/andreacimmino/Desktop/Lab/json-mapping/target/helio-processor-jmapping-0.2.2-jar-with-dependencies.jar", "helio.jmapping.functions.HF", ComponentType.FUNCTION);
			Components.registerAndLoad("/Users/andreacimmino/Desktop/Lab/json-mapping/target/helio-processor-jmapping-0.2.2-jar-with-dependencies.jar", "helio.jmapping.RDFHandler", ComponentType.HANDLER);

		} catch (Exception e) {
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

	public static Model runJMapping(String mappingFile) throws IncompatibleMappingException, TranslationUnitExecutionException{
		String mapping = readFile(mappingFile);
		Model result = ModelFactory.createDefaultModel();
		JMappingProcessor processor = new JMappingProcessor();
		Set<TranslationUnit> units = processor.parseMapping(mapping);
		units.parallelStream().forEach(unit -> {
			unit.translate();
			result.add(unit.getRDF());
			unit.clearRDF();
		});
		return result;
	}

	public static Boolean compareModels(Model model1, Model model2) {
		if (model1 == null || model2 == null || model1.isEmpty() || model2.isEmpty())
			return false;
		return contains(model1, model2);


	}

	public static Boolean contains(Model model1, Model model2) {
		Writer writer = new StringWriter();
		model1.write(writer, "NT");
		String[] triplet = writer.toString().split("\n");
		boolean result = true;
		for (String element : triplet) {
			String query = (new StringBuilder("ASK {\n")).append(element).append("\n}").toString();
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
}
