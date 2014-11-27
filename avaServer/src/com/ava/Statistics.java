package com.ava;

import com.ava.socket.SocketMessage.SocketMessageAction;

public interface Statistics {

	/**
	 * @return number of nodes that have already been checked
	 */
	public int checkedNodesCount();

	/**
	 * @return file prefix for output file
	 */
	public String getFilePrefix();

	/**
	 * @return SocketMessageAction that is send to the nodes
	 */
	public SocketMessageAction getMessageAction();

	public void clear();
}
