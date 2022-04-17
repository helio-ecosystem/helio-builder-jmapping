package test.generic;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.jena.rdf.model.Model;
import org.junit.Assert;
import org.junit.Test;

import helio.blueprints.TranslationUnit;
import helio.blueprints.UnitBuilder;
import helio.blueprints.components.Components;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.blueprints.exceptions.TranslationUnitExecutionException;
import helio.tests.TestUtils;

public class ExceptionHandlingTests {


	
	@Test
	public void test1() throws IncompatibleMappingException, IncorrectMappingException, ExtensionNotFoundException, TranslationUnitExecutionException  {
		boolean exceptionThrown = false;
		Set<TranslationUnit> units  =TestUtils.processJMapping("/Users/andreacimmino/Desktop/Lab/helio-core/src/test/resources/exceptions/mapping-01.json");
		TranslationUnit unit= units.iterator().next();

		try {
			
			unit.getTask().run();
			unit.getDataTranslated();
		}catch (Exception e) {
			
			exceptionThrown = true;
		}
		
		Assert.assertTrue(exceptionThrown);
	}

	
	
}
