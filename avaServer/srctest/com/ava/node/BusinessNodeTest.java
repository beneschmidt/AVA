package com.ava.node;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BusinessNodeTest {

	@Test
	public void testEtatInMinMaxRange() {
		NodeDefinition nodeDefinition = new NodeDefinition(1, "localhost", 3000, NodeType.business);
		BusinessNode node = new BusinessNode(nodeDefinition, null);
		assertTrue(node.getEtat() >= BusinessNode.MIN_ETAT && node.getEtat() <= BusinessNode.MAX_ETAT);
	}

	@Test
	public void testAdPlacing() {
		NodeDefinition nodeDefinition = new NodeDefinition(1, "localhost", 3000, NodeType.business);
		BusinessNode node = new BusinessNode(nodeDefinition, null);

		// place adds until no more etat available
		while (node.getEtat() > 0) {
			assertTrue(node.canIPlaceMoreAds());
			node.adPlaced();
		}

		assertFalse(node.canIPlaceMoreAds());
	}

}
