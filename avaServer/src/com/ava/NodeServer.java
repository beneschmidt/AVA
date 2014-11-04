package com.ava;

import java.util.List;

import com.ava.node.NodeDefinition;
import com.ava.socket.SocketMessage;

public interface NodeServer {

    void startServerAsThread();

    void closeServer();

    void connectToOtherNodes(List<NodeDefinition> othersNodes);

    void broadcastMessage(SocketMessage message);
    
    void sendMessage(NodeDefinition node, SocketMessage message);
}
