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
 * Clase AlfaBetaPlayer para representar al jugador CPU que usa una técnica de IA
 *
 * Esta clase es en la que tenemos que implementar y completar
 * el algoritmo MiniMax con Poda AlfaBeta
 *
 */
interface ConstantesAlfaBeta{
    int NIVEL_MAX = Integer.MAX_VALUE;
    int NIVEL_PODA = 6;
}

public class AlfaBetaPlayer extends Player {

    private NodoAlfaBeta nodoActual = null;

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
            nodoActual = new NodoAlfaBeta(null,tablero); //Nodo raiz
            long start = System.nanoTime();
            nodoActual.setSons(-1,1);
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
        System.out.println("Jugadas del jugador:" + nodoActual.jugador);
        nodoActual.visualizaHijos();
        if(nodoActual.sons.size() != 0) { //Si tenemos hijos buscamos el más favorable en nuestro caso el menor
            int posicion = 0;
            for (int i = 0; i < nodoActual.sons.size(); ++i) {
                posicion = nodoActual.sons.get(i).peso < nodoActual.sons.get(posicion).peso?i:posicion;
            }
            NodoAlfaBeta aux = nodoActual;
            nodoActual = nodoActual.sons.get(posicion);
            //Devolvemos movimiento
            System.out.println("Jugadas del jugador:" + nodoActual.jugador);
            nodoActual.visualizaHijos();
            return aux.sons.get(posicion).movimiento;
        }else{ //Si no tenemos hijos escogemos aleatoriamente una columna
            return getRandomColumn(tablero);
        }
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
    class NodoAlfaBeta {
        public final NodoAlfaBeta parent;
        public final ArrayList<NodoAlfaBeta> sons;
        public final Grid state;
        public float peso = 0;
        private int jugador;
        public int movimiento;

        public NodoAlfaBeta(NodoAlfaBeta parent, Grid state) {
            this.parent = parent;
            sons = new ArrayList<>();
            this.state = new Grid(state);
        }

        public NodoAlfaBeta(NodoAlfaBeta parent, Grid state, int movimiento) {
            this.parent = parent;
            sons = new ArrayList<>();
            this.state = new Grid(state);
            this.movimiento = movimiento;
        }

        public Grid getState() {
            return state;
        }

        public void visualizaHijos(){
            for (int i = 0; i < this.sons.size(); i++) {
                System.out.println("hijo:" + i + " valor:" + sons.get(i).peso + "\njugada:" + sons.get(i).movimiento);
            }
        }

        public void setSons(int jugador,int nivel){
            //Comprobamos que no haya ganado nadie
            if (state.checkWin() != 0) {
                peso = (float) (-jugador*Math.pow(getBigger(state.checkWin()),2));
                return;
            }
            //Comprobamos que no este lleno
            if (state.getCount(jugador) + state.getCount(-jugador) == state.getColumnas() * state.getFilas()) {
                return;
            }
            /*
            if(nivel == ConstantesAlfaBeta.NIVEL_MAX){
                peso = (float) (-jugador * Math.pow(getBigger(-jugador),2));
                return;
            }
            */
            //Generación Base
            this.jugador = jugador;
            for (int i = 0; i < state.columnas; ++i) {
                Grid aux = new Grid(state);
                if (aux.set(i, jugador) >= 0) {
                    NodoAlfaBeta candidato = new NodoAlfaBeta(this, aux, i);
                    sons.add(candidato);
                    candidato.setSons(-jugador,nivel + 1);
                    if(candidato.peso*jugador == 16){
                        if(sons.size() > 1){
                            poda(jugador,nivel);
                        }
                        break;
                    }
                    if(nivel >= ConstantesAlfaBeta.NIVEL_PODA && sons.size() > 1){
                        poda(jugador,nivel);
                    }
                }
            }

            //Gestionar los pesos
            int posMin = 0;
            int posMax = 0;
            for (int i = 1; i < sons.size(); ++i) {
                posMin = sons.get(i).peso < sons.get(posMin).peso?i:posMin;
                posMax = sons.get(i).peso > sons.get(posMax).peso?i:posMax;
            }
            peso = (float) (jugador == 1? sons.get(posMax).peso/1.2:sons.get(posMin).peso/1.2);
        }
        public int getBigger(int jugador){
            int bigger = 0;
            int aux = 0;
            //Comprobar vertical
            for(int i = 0; i < state.columnas; ++i){
                for (int j = 0; j < state.filas && state.get(state.filas - 1 - j,i) != 0;++j){
                    int a =  state.get(state.filas - 1 - j,i);
                    aux = state.get(state.filas - 1 - j, i) == jugador? aux + 1: 0;
                    bigger = Math.max(bigger, aux);
                }
            }
            aux = 0;
            //Comprobar horizontal
            for(int i = 0; i < state.filas; ++i){
                for (int j = 0; j < state.columnas && state.get(state.filas - 1 - i,j) != 0;++j){
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
            int aux = 0;
            while (y < state.columnas){
                int a = x;
                int b = y;

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
        public void poda(int jugador,int nivel){
            if(jugador == 1){
                if(sons.get(0).peso >= sons.get(1).peso){
                    sons.remove(1);
                }else {
                    sons.remove(0);
                }
            }else {
                if(sons.get(0).peso <= sons.get(1).peso){
                    sons.remove(1);
                }else {
                    sons.remove(0);
                }
            }
        }
    }


} // AlfaBetaPlayer
