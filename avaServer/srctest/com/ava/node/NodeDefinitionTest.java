package com.ava.node;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ava.node.NodeDefinition;

public class NodeDefinitionTest {

	@Test
	public void testCreateDefinitionByString() {
		String testString = "1 testip:12";
		NodeDefinition definition = new NodeDefinition(testString);
		assertEquals(1, definition.getId());
		assertEquals("testip", definition.getIp());
		assertEquals(12, definition.getPort());
	}
	
	@Test (expected = RuntimeException.class)
	public void testCreateDefinitionWithUnparsableString() {
		String testString = "NICHT ZU PARSEN";
		new NodeDefinition(testString);
	}

}
