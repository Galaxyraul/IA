
package mouserun.mouse;

import mouserun.game.Cheese;
import mouserun.game.Grid;
import mouserun.game.Mouse;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author Raúl Gómez Téllez y Juan Carlos González Martínez
 */




public class M23B12bpl extends Mouse {
    private final Stack<Grid> movimiento;
    private final HashMap<Pair<Integer, Integer>, Grid> celdasVisitadas;
    private final HashMap<Pair<Integer, Integer>, Grid> cerradas;
    private final LinkedList<Grid> camino;

    public M23B12bpl() {
        super("Rychard II");
        movimiento = new Stack<>();
        celdasVisitadas = new HashMap<>();
        cerradas = new HashMap<>();
        camino = new LinkedList<>();
    }

    @Override
    public int move(Grid currentGrid, Cheese cheese){
        if(!camino.isEmpty()){
            return mueveteBusqueda(currentGrid);
        }
        if(celdasVisitadas.containsKey(new Pair<>(cheese.getX(),cheese.getY()))){
            int limite = 50;
            while(!busquedaProfundidadLimitada(limite, currentGrid, cheese)){
                cerradas.clear();
                limite *= 2;
            }
            cerradas.clear();
            return mueveteBusqueda(currentGrid);
        }else {
            return MovimientoBase(currentGrid,cheese);
        }
    }

    private int MovimientoBase(Grid currentGrid, Cheese cheese){
        if (currentGrid.canGoLeft() && visitada(currentGrid, LEFT)) {
            movimiento.push(currentGrid);
            celdasVisitadas.put(new Pair(currentGrid.getX(), currentGrid.getY()), currentGrid);
            incExploredGrids();
            return Mouse.LEFT;
        } else if (currentGrid.canGoRight() && visitada(currentGrid, RIGHT)) {
            movimiento.push(currentGrid);
            celdasVisitadas.put(new Pair(currentGrid.getX(), currentGrid.getY()), currentGrid);
            incExploredGrids();
            return Mouse.RIGHT;
        } else if (currentGrid.canGoDown() && visitada(currentGrid, DOWN)) {
            movimiento.push(currentGrid);
            celdasVisitadas.put(new Pair(currentGrid.getX(), currentGrid.getY()), currentGrid);
            incExploredGrids();
            return Mouse.DOWN;
        } else if (currentGrid.canGoUp() && visitada(currentGrid, UP)) {
            movimiento.push(currentGrid);
            celdasVisitadas.put(new Pair(currentGrid.getX(), currentGrid.getY()), currentGrid);
            incExploredGrids();
            return Mouse.UP;
        } else {
            return LastGrid(currentGrid);
        }

    }

    @Override
    public void newCheese() {
        camino.clear();
        cerradas.clear();
    }

    @Override
    public void respawned() {
    }
    private boolean busquedaProfundidadLimitada(int limite,Grid actual,Cheese queso){
        cerradas.put(new Pair<>(actual.getX(), actual.getY()),actual);
        return BusquedaProfundidadBis(limite,actual,queso);
    }
    private boolean BusquedaProfundidadBis(int limite,Grid actual,Cheese queso){
        if(queso.getX() == actual.getX() && queso.getY() == actual.getY()){
            return true;
        }
        if (limite == 0){
            return false;
        }
        ArrayList<Grid> Sucesores = generarHijos(actual);
        for (int i = 0; i < Sucesores.size();++i){
            Grid hijo = Sucesores.get(i);
            camino.add(hijo);
            if(BusquedaProfundidadBis(limite - 1,hijo,queso)){
                return true;
            }else{
                camino.removeLast();
            }
        }
        return false;
    }

    private ArrayList<Grid> generarHijos(Grid actual){
        cerradas.put(new Pair<>(actual.getX(), actual.getY()),actual);
        Pair<Integer,Integer> key = new Pair<>(0,0);
        key.first = actual.getX();
        key.second = actual.getY() + 1;
        ArrayList <Grid> result =  new ArrayList<>();
        if(celdasVisitadas.containsKey(key)
                && actual.canGoUp()
                && !cerradas.containsKey(key)){
            result.add(celdasVisitadas.get(key));
        }
        key.second = actual.getY() - 1;
        if(celdasVisitadas.containsKey(key)
                && actual.canGoDown()
                && !cerradas.containsKey(key)){
            result.add(celdasVisitadas.get(key));
        }
        key.first = actual.getX() + 1;
        key.second = actual.getY();
        if(celdasVisitadas.containsKey(key)
                && actual.canGoRight()
                && !cerradas.containsKey(key)){
            result.add(celdasVisitadas.get(key));
        }
        key.first = actual.getX() - 1;
        if(celdasVisitadas.containsKey(key)
                && actual.canGoLeft()
                && !cerradas.containsKey(key)){
            result.add(celdasVisitadas.get(key));
        }
        return result;
    }
    private boolean visitada(Grid casilla,int direccion) {
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
            default:
                break;
        }
        Pair par = new Pair(x, y);
        return !celdasVisitadas.containsKey(par);
    }
    private int LastGrid(Grid currentGrid){
        celdasVisitadas.put(new Pair(currentGrid.getX(),currentGrid.getY()),currentGrid);
        Grid next = movimiento.pop();
        int x = currentGrid.getX();
        int y = currentGrid.getY();
        int xu = next.getX();
        int yu = next.getY();
        if(x != xu){
            return x > xu? LEFT:RIGHT;
        }else{
            return y > yu? DOWN:UP;
        }
    }
    private int mueveteBusqueda(Grid currentGrid){
        Grid next = camino.remove();
        movimiento.push(currentGrid);
        int x = currentGrid.getX();
        int y = currentGrid.getY();
        int xu = next.getX();
        int yu = next.getY();
        if(x != xu){
            return x > xu? LEFT:RIGHT;
        }else{
            return y > yu? DOWN:UP;
        }
    }
    static class Pair<U, V> {

        public U first;       // el primer campo de un par
        public V second;      // el segundo campo de un par

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

