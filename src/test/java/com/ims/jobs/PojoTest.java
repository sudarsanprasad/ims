package com.ims.jobs;

import org.junit.Test;

import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsForAll;

public class PojoTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void Should_Test_Pojo() {
		final Class[] classesUnderTest = { TriggerDescriptor.class, JobDescriptor.class };
		assertPojoMethodsForAll(classesUnderTest).testing(Method.EQUALS).testing(Method.HASH_CODE).areWellImplemented();

	}
}
