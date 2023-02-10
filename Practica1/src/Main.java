import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static final int MAX_NOTAS = 4;
    public static Alumno leerDatos(){
        String nombre,apellido,dni,correo;
        Scanner sc = new Scanner(System.in);
        System.out.print("Introduzca el nombre del alumno:");
        nombre = sc.nextLine();
        System.out.print("Introduzca el apellido del alumno:");
        apellido = sc.nextLine();
        System.out.print("Introduzca el DNI del alumno:");
        dni = sc.nextLine();
        System.out.print("Introduzca el correo del alumno:");
        correo = sc.nextLine();
        return new Alumno(nombre,apellido,dni,correo);
    }
    public static void leerNotas(Alumno_IA a){
        Scanner sc = new Scanner(System.in);
        int numNotas = 0;
        float media = 0;
        int nota;
        while (numNotas < MAX_NOTAS){
            do {
                System.out.print("Introduzca la nota del alumno:");
                nota = sc.nextInt();
            }while(nota <= 0 || nota >4);
            media+=nota;
            numNotas++;
        }
        System.out.println("La nota del alumno de IA es:" + media/MAX_NOTAS);
    }
    public static ArrayList<Alumno> leerFichero(String nombreFichero){
        DataInputStream di = null;
        ArrayList<Alumno> alumnos = new ArrayList<>();
        try{
            //Abrir archivo origen
            FileInputStream fileI = new FileInputStream(nombreFichero);
            di = new DataInputStream(fileI);
            String line;
            int i = 0;
            while((line = di.readLine()) != null){
                System.out.println(line);
                String datos[] = line.split(",");
                alumnos.add(new Alumno(datos[0],datos[1],datos[2],datos[3]));
                i++;
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return alumnos;
    }
    public static void escribirEnArchivo(ArrayList<Alumno> alumnos, String destino){
        DataOutputStream dO = null;
        File file = new File("C:\\Users\\admin\\Downloads\\"+destino);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileO = new FileOutputStream(file);
            dO = new DataOutputStream(fileO);
            for (int i = 0; i < alumnos.size();++i){
                if(Integer.parseInt(alumnos.get(i).getDni().replace(" ","")) % 2 == 0){
                    dO.writeChars(alumnos.get(i).toString()+ "\n");
                }
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
    public static void main(String[] args) {
        ArrayList<Alumno> datos = leerFichero("C:\\Users\\admin\\Downloads\\2-datos.txt");
        escribirEnArchivo(datos,"pares.txt");
    }
}