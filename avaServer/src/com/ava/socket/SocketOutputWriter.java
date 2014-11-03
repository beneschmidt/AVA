package com.ava.socket;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.ava.utils.ResourceHelper;
import com.ava.utils.TimeUtils;

public class SocketOutputWriter {

    public void writeMessage(Socket socket, String message) {
	OutputStreamWriter writer = null;
	try {
	    OutputStream out = socket.getOutputStream();
	    writer = new OutputStreamWriter(out);
	    writer.write(message);
	    System.out.println("[OUT] "+TimeUtils.getCurrentTimestampString() + ": " + message);
	} catch (Exception e) {
	    System.err.println("Verbindung fehlgeschlagen!");
	} finally {
	    ResourceHelper.close(writer);
	}
    }
}
