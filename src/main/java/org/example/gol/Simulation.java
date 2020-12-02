package org.example.gol;
import org.example.gol.model.Board;
import org.example.gol.model.CellState;
import org.example.gol.model.SimulationRule;

public class Simulation {
    SimulationRule simulationRule;
    Board simulationBoard;
    public Simulation(Board simulationBoard, SimulationRule simulationRule) {
       this.simulationBoard = simulationBoard;
       this.simulationRule = simulationRule;
    }

    public void nextGeneration() {
        Board nextState = simulationBoard.copy();
        for (int y = 0; y < simulationBoard.getHeight(); y++) {
            for (int x = 0; x < simulationBoard.getWidth(); x++) {
                CellState newState = simulationRule.getNextState(x, y, simulationBoard);
                nextState.setState(x,y, newState);
            }
        }
        this.simulationBoard = nextState;
    }

    public Board getBoard() {
        return simulationBoard;
    }
}

