/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot4;

import java.util.ArrayList;

import static plot4.Constantes.NIVEL_MAX;

/**
 *
 * @author José María Serrano
 * @version 1.7 Departamento de Informática. Universidad de Jáen 
 * Última revisión: 2023-03-30
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Clase MiniMaxPlayer para representar al jugador CPU que usa una técnica de IA
 *
 * Esta clase es en la que tenemos que implementar y completar el algoritmo
 * MiniMax
 *
 */
public class MiniMaxPlayer extends Player {
    private Node nodoActual = null;

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
            nodoActual = new Node(tablero);
            long start = System.nanoTime();
            nodoActual.setSons(-1);
            long end = System.nanoTime() - start;
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
        nodoActual.visualizaHijos();
        //Elegimos estado favorable
        int posicion = 0;
        int hijoEmpate = -1;
        for(int i = 0; i < nodoActual.sons.size() && nodoActual.sons.get(posicion).mark  != -1;++i){
            if(nodoActual.sons.get(posicion).mark == 0){hijoEmpate = i;}
            posicion = i;
        }
        posicion = nodoActual.sons.get(posicion).mark >= 0 && hijoEmpate != -1? 0:posicion;
        Node aux = nodoActual;
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
class Node{
    public final ArrayList<Node> sons;
    public final Grid state;
    public int mark = 0;
    public int movimiento;

    public Node(Grid state) {
        sons = new ArrayList<>();
        this.state = new Grid(state);
    }

    public Node(Grid state, int movimiento) {
        sons = new ArrayList<>();
        this.state = new Grid(state);
        this.movimiento = movimiento;
    }

    public Grid getState() {
        return state;
    }
    public void visualizaHijos(){
        for (int i = 0; i < this.sons.size(); i++) {
            System.out.println("hijo:" + i + " valor:" + sons.get(i).mark);
        }
    }
    public void setSons(int jugador){
        //Comprobamos que no haya ganado nadie
        if(state.checkWin() != 0){
            mark = state.checkWin() == 1?1:-1;
            return;
        }
        if(state.getCount(jugador) + state.getCount(-jugador) == state.getColumnas() * state.getFilas()){return;}
        //Generación Base
        for(int i = 0; i < state.columnas;++i){
            Grid aux = new Grid(state);
            if(aux.set(i,jugador) >= 0 ){
                Node nuevo = new Node(aux,i);
                nuevo.setSons(-jugador);
                sons.add(nuevo);
            }
        }
        //Asignar marca
        int hijoEmpate = -1;
        for(int i = 0; i < sons.size() && mark != jugador;++i){
            if(sons.get(i).mark == 0){hijoEmpate = i;}
            mark = sons.get(i).mark;
        }
        //En caso de que no pueda ganar pero pueda empatar busco el empate
        mark = mark >= 0 && hijoEmpate != -1? 0:mark;
    }
}
