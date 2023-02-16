package Agentes;
import jade.core.Agent;
public class AgenteEsqueleto extends Agent{
    @Override
    protected void setup() {
        System.out.println("Se inicia la ejecución del agente" + this.getName());
    }

    @Override
    protected void takeDown() {
        System.out.println("Finaliza la ejecución del agente" + this.getName());
    }
}

