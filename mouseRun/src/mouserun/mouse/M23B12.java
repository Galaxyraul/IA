package mouserun.mouse;
import java.util.*;
import mouserun.game.*;
import java.util.HashMap;

/**
 * @author Raúl Gómez Téllez y Juan Carlos González Martínez
 */

public class M23B12 extends Mouse {
    private Stack<Grid> movimiento;
    private HashMap<Pair<Integer,Integer>, Grid> celdasVisitadas;
    private HashMap<Pair<Integer,Integer>, Grid> celdasVisitadas2;

    public M23B12() {
        super("Jerry");
        movimiento = new Stack<>();
        celdasVisitadas = new HashMap<>();
        celdasVisitadas2 = new HashMap<>();
    }

    @Override
    public int move(Grid currentGrid, Cheese cheese) {
        if(currentGrid.canGoLeft() && !visitada(currentGrid,LEFT)){
            movimiento.push(currentGrid);
            celdasVisitadas.put(new Pair(currentGrid.getX(),currentGrid.getY()),currentGrid);
            if(!celdasVisitadas2.containsKey(new Pair(currentGrid.getX(),currentGrid.getY()))){
                celdasVisitadas2.put(new Pair(currentGrid.getX(),currentGrid.getY()),currentGrid);
                incExploredGrids();
            }
            return Mouse.LEFT;
        }else if(currentGrid.canGoUp() && !visitada(currentGrid,UP)){
            movimiento.push(currentGrid);
            celdasVisitadas.put(new Pair(currentGrid.getX(),currentGrid.getY()),currentGrid);
            if(!celdasVisitadas2.containsKey(new Pair(currentGrid.getX(),currentGrid.getY()))){
                celdasVisitadas2.put(new Pair(currentGrid.getX(),currentGrid.getY()),currentGrid);
                incExploredGrids();
            }
            return Mouse.UP;
        }else if(currentGrid.canGoRight() && !visitada(currentGrid,RIGHT)){
            movimiento.push(currentGrid);
            celdasVisitadas.put(new Pair(currentGrid.getX(),currentGrid.getY()),currentGrid);
            if(!celdasVisitadas2.containsKey(new Pair(currentGrid.getX(),currentGrid.getY()))){
                celdasVisitadas2.put(new Pair(currentGrid.getX(),currentGrid.getY()),currentGrid);
                incExploredGrids();
            }
            return Mouse.RIGHT;
        }else if(currentGrid.canGoDown() && !visitada(currentGrid,DOWN)){
            movimiento.push(currentGrid);
            celdasVisitadas.put(new Pair(currentGrid.getX(),currentGrid.getY()),currentGrid);
            if(!celdasVisitadas2.containsKey(new Pair(currentGrid.getX(),currentGrid.getY()))){
                celdasVisitadas2.put(new Pair(currentGrid.getX(),currentGrid.getY()),currentGrid);
                incExploredGrids();
            }
            return Mouse.DOWN;
        }else{
            return LastGrid(currentGrid);}
    }

    @Override
    public void newCheese() {
    }

    @Override
    public void respawned() {
        celdasVisitadas.clear();
    }

    public boolean visitada(Grid casilla,int direccion) {
        int x = casilla.getX();
        int y = casilla.getY();

        switch (direccion) {
            case UP:
                y += 1;
                break;

            case DOWN:
                y -= 1;
                break;

            case LEFT:
                x -= 1;
                break;

            case RIGHT:
                x += 1;
                break;
        }
        Pair par = new Pair(x, y);
        return celdasVisitadas.containsKey(par);
    }

    int LastGrid(Grid currentGrid){
        if(!movimiento.empty()) {
            celdasVisitadas.put(new Pair(currentGrid.getX(),currentGrid.getY()),currentGrid);
            Grid ultimo = movimiento.lastElement();
            movimiento.pop();
            int x = currentGrid.getX();
            int y = currentGrid.getY();
            int xu = ultimo.getX();
            int yu = ultimo.getY();
            if(x == xu){
                if(y > yu){
                    return DOWN;
                }else{
                    return UP;
                }
            }else{
                if(x > xu){
                    return LEFT;
                }else{
                    return RIGHT;
                }
            }
        }
        return 0;
    }

    class Pair<U, V> {

        public final U first;       // el primer campo de un par
        public final V second;      // el segundo campo de un par

        // Construye un nuevo par con valores especificados
        private Pair(U first, V second) {
            this.first = first;
            this.second = second;
        }

        // Verifica que el objeto especificado sea "igual a" el objeto actual o no

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Pair<?, ?> pair = (Pair<?, ?>) o;

            // llamar al método `equals()` de los objetos subyacentes
            if (!first.equals(pair.first)) {
                return false;
            }
            return second.equals(pair.second);
        }


        @Override
        public int hashCode() {
            return 31 * first.hashCode() + second.hashCode();
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }

    }
}


