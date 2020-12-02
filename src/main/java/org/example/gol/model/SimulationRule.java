package org.example.gol.model;

public interface SimulationRule {
    CellState getNextState (int x, int y, Board board);
}
