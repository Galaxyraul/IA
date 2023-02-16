package Agentes;
import jade.core.Agent;


public class AgenteEsqueleto extends Agent{
    @Override
    protected void setup() {
        System.out.println("Hola compañeros de IA, soy Raúl. Acabo de iniciar mi ejecución estoy, en MainContainer  y este es mi estado:" + this.getState());
    }

    @Override
    protected void takeDown() {
        System.out.println("Finaliza la ejecución del agente" + this.getName());
    }
}

