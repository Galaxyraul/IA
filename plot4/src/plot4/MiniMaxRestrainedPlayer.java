/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot4;

import java.util.ArrayList;

import static plot4.Constantes.*;

interface Constantes{
    int NIVEL_MAX = 8; //Nivel máximo
    int NIVEL_EXPANSION = 3;
    int CONECTA = 16; //Indicador del maximo valor que puede tener un nodo
}

/**
 *
 * @author Juan Carlos González Martínez - jcgm0022
 * @author Raúl Gomez Téllez - rgt00024
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Clase MiniMaxRestrainedPlayer para representar al jugador CPU que usa una técnica de IA
 */
public class MiniMaxRestrainedPlayer extends Player {
    private Nodo nodoActual = null;

    /**
     * @brief funcion que determina donde colocar la ficha este turno
     * @param tablero Tablero de juego
     * @param conecta Número de fichas consecutivas adyacentes necesarias para
     * ganar
     * @return Devuelve si ha ganado algun jugador
     */
    @Override
    public int turno(Grid tablero, int conecta) {
        //Comprobamos que el arbol no se ha creado
        if (nodoActual == null){
            nodoActual = new Nodo(tablero,0); //Nodo raiz
            long start = System.nanoTime();//Tiempo de inicio para la saber cuanto tarda en generar el arbol
            nodoActual.setSons(-1,1);
            long end = System.nanoTime() - start; //Tiempo final
            System.out.println("Ha tardado " + end/1e9 + "s en generar el arbol");
        }
        //Nos vamos al estado resultante de haber jugado el otro jugador
        if(!tablerosIguales(nodoActual.getState(),tablero)){
            for(int i = 0; i < nodoActual.sons.size();++i){
                if(tablerosIguales(nodoActual.sons.get(i).state,tablero)) {
                    nodoActual = nodoActual.sons.get(i);
                    break;
                }
            }
        }
        System.out.println("Posibles jugadas siguientes para:" + nodoActual.jugador);
        nodoActual.visualizaHijos();
        if(nodoActual.nivel >= NIVEL_EXPANSION){
            nodoActual.regenera(0);
        }
        int posicion = 0;
        //Buscamos el hijo con menor peso para maximizar
        for (int i = 0; i < nodoActual.sons.size(); ++i) {
            posicion = nodoActual.sons.get(i).peso < nodoActual.sons.get(posicion).peso?i:posicion;
        }
        Nodo aux = nodoActual;
        nodoActual = nodoActual.sons.get(posicion);
        //Devolvemos movimiento
        return aux.sons.get(posicion).movimiento;
    }
    
    public boolean tablerosIguales(Grid hijo,Grid tablero){
        boolean iguales = true;
        for (int i = 0; i < tablero.filas && iguales; i++) {
            for (int j = 0; j < tablero.columnas && iguales; j++) {
                iguales = hijo.get(i,j) == tablero.get(i,j);
            }
        }
        return iguales;
    }
}
class Nodo{
    public final ArrayList<Nodo> sons;
    public final Grid state;
    public float peso = 0;
    public int movimiento;

    public int jugador = 0;
    public int nivel = 0;

    public Nodo(Grid state,int nivel) {
        sons = new ArrayList<>();
        this.state = new Grid(state);
        this.nivel = nivel;
    }

    public Nodo( Grid state, int movimiento,int nivel) {
        sons = new ArrayList<>();
        this.state = new Grid(state);
        this.movimiento = movimiento;
        this.nivel = nivel;
    }

    public Grid getState() {
        return state;
    }

    public void visualizaHijos(){
        for (int i = 0; i < this.sons.size(); i++) {
            System.out.println("hijo:" + i + " valor:" + sons.get(i).peso + "\njugada:" + sons.get(i).movimiento);
        }
    }
    public void regenera(int nivel){
        this.nivel = nivel;
        if(sons.size() == 0 && jugador * peso != CONECTA){
            this.setSons(jugador,nivel + 1);
        }else {
            for (int i = 0; i < sons.size(); ++i) {
                sons.get(i).regenera(nivel + 1);
            }
            if(jugador == 1){
                maximiza();
            }else {
                minimiza();
            }
        }
    }
    public void setSons(int jugador,int nivel){
        this.jugador = jugador;
        this.nivel = nivel;
        //Comprobamos que no haya ganado nadie
        if (state.checkWin() != 0) {
            peso = (float) (-jugador*Math.pow(getBigger(state.checkWin()),2));
            return;
        }
        //Comprobamos que no este lleno
        if (state.getCount(jugador) + state.getCount(-jugador) == state.getColumnas() * state.getFilas()) {
            return;
        }
        //Comprobamos que no se ha alcanzado el nivel máximo
        if(nivel == NIVEL_MAX){
            //Si llegamos al nivel máximo de profundidad establecemos el valor que el mayor número de fichas conectadas
            peso = (float) (-jugador * Math.pow(getBigger(-jugador),2));
            return;
        }
        this.jugador = jugador;
        //Generación Base
        for (int i = 0; i < state.columnas; ++i) {
            Grid aux = new Grid(state);
            if (aux.set(i, jugador) >= 0) {
                Nodo candidato = new Nodo(aux, i,nivel+1);
                sons.add(candidato);
                //Generamos los hijos antes de añadir todos los del nivel para poder parar en caso de que uno sea hoja
                candidato.setSons(-jugador,nivel + 1);
                //Comprobamos si el que acabamos de añadir es hoja en cuyo caso paramos la ejecución
                if(candidato.peso*jugador == CONECTA){
                    break;
                }
            }
        }
        //Para gestionar los pesos buscamos el nodo con mayor y el nodo con menor peso
        if(jugador == 1){
            maximiza();
        }else {
            minimiza();
        }
    }
    void maximiza(){
        int posMax = 0;
        for (int i = 1; i < sons.size(); ++i) {
            posMax = sons.get(i).peso > sons.get(posMax).peso?i:posMax;
        }
        peso = (float) (sons.get(posMax).peso / 1.2);
    }
    void minimiza(){
        int posMin = 0;
        for (int i = 1; i < sons.size(); ++i) {
            posMin = sons.get(i).peso < sons.get(posMin).peso?i:posMin;
        }
        peso = (float) (sons.get(posMin).peso / 1.2);
    }
    public int getBigger(int jugador){
        int bigger = 0;
        int aux;
        //Comprobar vertical
        for(int i = 0; i < state.columnas; ++i){
            aux = 0;
            for (int j = 0; j < state.filas && state.get(state.filas - 1 - j,i) != 0;++j){
                int a =  state.get(state.filas - 1 - j,i);
                aux = state.get(state.filas - 1 - j, i) == jugador? aux + 1: 0;
                bigger = Math.max(bigger, aux);
            }
        }
        //Comprobar horizontal
        for(int i = 0; i < state.filas; ++i){
            aux = 0;
            for (int j = 0; j < state.columnas && state.get(state.filas - 1 - i,j) != 0;++j){
                int a =  state.get(state.filas - 1 - i,j);
                aux = state.get(state.filas - 1 - i,j) == jugador? aux + 1: 0;
                bigger = Math.max(bigger, aux);
            }
        }
        aux = compruebadiagonalDerecha(jugador);
        bigger =  Math.max(bigger, aux);
        aux = compruebadiagonalIzquierda(jugador);
        bigger = Math.max(bigger, aux);
        return bigger;
    }
    public int compruebadiagonalDerecha(int jugador){
        int x = 0;
        int y = 0;
        int bigger = 0;
        int aux;
        while (y < state.columnas){
            int a = x;
            int b = y;
            aux = 0;
            while (a < state.filas && b < state.columnas){
                aux= state.get(state.filas- 1 - a, b) == jugador?++aux: 0;
                bigger = Math.max(bigger, aux);
                a++;
                b++;
            }
            y++;
        }
        y = 0;
        int a;
        int b;
        while (x < state.filas){
            a = x;
            b = y;
            aux = 0;
            while (a < state.filas && b < state.columnas){
                aux= state.get(state.filas- 1 - a, b) == jugador?++aux: 0;
                bigger = Math.max(bigger, aux);
                a++;
                b++;
            }
            x++;
        }
        return  bigger;
    }
    public int compruebadiagonalIzquierda(int jugador){
        int x = 0;
        int y = state.columnas;
        int bigger = 0;
        int aux = 0;
        while (y >= 0){
            int a = x;
            int b = y;

            while (a < state.filas && b < state.columnas){
                aux= state.get(state.filas- 1 - a,b) == jugador?++aux: 0;
                bigger = Math.max(bigger, aux);
                a--;
                b++;
            }
            y--;
        }
        y = 0;
        int a;
        int b;
        while (x < state.filas){
            a = x;
            b = y;
            while (a < state.filas && b < state.columnas){
                aux= state.get(state.filas- 1 - a,b) == jugador?++aux: 0;
                bigger = Math.max(bigger, aux);
                a--;
                b++;
            }
            x++;
        }
        return  bigger;
    }
}

