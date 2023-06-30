package com.example.Scrabble.Model.Player;

/**
 * Represents a player in the game of Scrabble.
 */
public interface Player {
    /**
     * Retrieves the name of the player.
     *
     * @return The player's name.
     */
    public String getName();

    /**
     * Retrieves the ID of the player.
     *
     * @return The player's ID.
     */
    public int getPlayerID();

    /**
     * Ends the player's turn.
     *
     * @return true if the turn ended successfully, false otherwise.
     */
    public boolean endTurn();

    /**
     * Sets the ID of the player.
     *
     * @param ID The ID to be set for the player.
     */
    public void setID(int ID);
}
