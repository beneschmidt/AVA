package com.ava;

import java.util.List;

import com.ava.node.NodeDefinition;

public interface NodeServer {

    void startServerAsThread();

    void closeServer();

    void connectToOtherNodes(List<NodeDefinition> othersNodes);

    void broadcastMessage(String message);
}
