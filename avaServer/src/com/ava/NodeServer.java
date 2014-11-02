package com.ava;

import java.util.List;

import com.ava.ueb01.NodeDefinition;

public interface NodeServer {

    void startServerAsThread();

    void closeServer();

    void connectToOtherNodes(List<NodeDefinition> othersNodes);

    void broadcastMessage();
}
