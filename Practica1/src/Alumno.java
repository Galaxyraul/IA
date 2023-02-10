public class Alumno {
    private final String nombre;
    private final String apellido;
    private final String dni;
    private final String correo ;

    public Alumno(String nombre, String apellido, String dni, String correo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.correo = correo;
    }

    public Alumno() {
        this.nombre = "";
        this.apellido = "";
        this.dni = "";
        this.correo = "";
    }

    @Override
    public String toString() {
        return nombre + ',' + apellido + ',' + dni + ',' + correo;
    }

    public String getDni() {
        return dni;
    }
}
