public class Alumno_IA extends Alumno{
    private int grupoTeoria;
    private int notaAsociada;

    public Alumno_IA(int grupoTeoria, int notaAsociada, String nombre, String apellido, String dni, String correo) {
        super(nombre, apellido, dni, correo);
        this.grupoTeoria = grupoTeoria;
        this.notaAsociada = notaAsociada;
    }

    public Alumno_IA() {
    }
}
