package com.ava;

import java.util.List;

import com.ava.node.NodeDefinition;
import com.ava.socket.SocketMessage;

/**
 * interface to specify functions a node server has to implement
 */
public interface NodeServer {

    void startServerAsThread();

    void closeServer();

    void connectToOtherNodes(List<NodeDefinition> othersNodes);

    void broadcastMessage(SocketMessage message);
    
    void sendMessage(NodeDefinition node, SocketMessage message);
}
