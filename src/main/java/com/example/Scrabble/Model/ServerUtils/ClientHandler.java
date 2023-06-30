package com.example.Scrabble.Model.ServerUtils;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The `ClientHandler` interface defines the contract for handling communication with a client.
 * Classes implementing this interface are responsible for handling the input and output streams
 * of a client connected to the server.
 */
public interface ClientHandler {

	/**
	 * Handles the input and output streams of the client.
	 * This method is responsible for processing the client's input and sending responses back.
	 *
	 * @param inFromClient The input stream from the client.
	 * @param outToClient  The output stream to the client.
	 */
	void handleClient(InputStream inFromClient, OutputStream outToClient);

	/**
	 * Closes the client handler and releases any associated resources.
	 * This method should be called when the client connection is terminated.
	 */
	void close();

	/**
	 * Sends a message to the client.
	 * This method is used to send messages or responses back to the client.
	 *
	 * @param msg The message to send to the client.
	 */
	void sendMsg(String msg);
}
