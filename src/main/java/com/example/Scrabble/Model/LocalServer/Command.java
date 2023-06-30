package com.example.Scrabble.Model.LocalServer;

/**
 * The `Command` interface represents a command to be executed in the local server.
 * Classes implementing this interface must provide an implementation for the `execute` method.
 */
public interface Command {
    /**
     * Executes the command and returns the result.
     *
     * @return The result of executing the command.
     */
    String execute();
}
