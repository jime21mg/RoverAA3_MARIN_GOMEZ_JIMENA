
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Random;
public class Rover

{
    private String nombre;
private String codigoRover;

private int posicionInicialX;
private int posicionInicialY;
private int posicionActualX;
private int posicionActualY;

private double potenciaInicial;
private double potenciaDisponible;

private int recargasMaximas;
private int recargasRealizadas;

private int cantidadDeteccionesDeCalor;

private double consumoPorDeteccion;
private double consumoPorMovimiento;

private ArrayList<ArrayList<String>> mandatosPosibles;
private ArrayList<ArrayList<String>> mandatosNoPosibles;

private static int contadorRovers = 0;
private static ArrayList<Rover> informacionRovers = new ArrayList<Rover>();
public Rover(String nombre, String codigoRover) {
    this(nombre, codigoRover, 100.0);
}
    public Rover(String nombre, String codigoRover, double potencia) {

    this.nombre = nombre;
    this.codigoRover = codigoRover;

    potenciaInicial = potencia;
    potenciaDisponible = potencia;

    posicionInicialX = 0;
    posicionInicialY = 0;

    posicionActualX = posicionInicialX;
    posicionActualY = posicionInicialY;

    recargasMaximas = 5;
    recargasRealizadas = 0;

    cantidadDeteccionesDeCalor = 0;

    consumoPorDeteccion = 0.25;
    consumoPorMovimiento = 0.5;

    mandatosPosibles = new ArrayList<ArrayList<String>>();
    mandatosNoPosibles = new ArrayList<ArrayList<String>>();

    contadorRovers++;

    informacionRovers.add(this);
}
    public String consultarPosicionActual() {
    return "(" + posicionActualX + ", " + posicionActualY + ")";
}
public double consultarPotenciaDisponible() {
    return potenciaDisponible;
}
public int consultarRecargasDisponibles() {
    return recargasMaximas - recargasRealizadas;
}
 private boolean validarRecarga() {
    return recargasRealizadas < recargasMaximas;
}   
private boolean validarPotenciaActual() {
    return potenciaDisponible >= consumoPorDeteccion + consumoPorMovimiento;
}
private boolean detectarFuga() {
    cantidadDeteccionesDeCalor++;
    potenciaDisponible -= consumoPorDeteccion;

    Random numeroAleatorio = new Random();
    double valor = numeroAleatorio.nextDouble();

    if (valor >= 0.5) {
        return true;
    } else {
        return false;
    }
}
private String obtenerFechaHoraActual() {
    Date fechaActual = new Date();

    DateFormat formatoFecha =
        new SimpleDateFormat("dd/MM/yy HH:mm:ss");

    return formatoFecha.format(fechaActual);
}

private void registrarMandato(String tipoMandato, String estadoMandato) {

    ArrayList<String> mandato = new ArrayList<String>();

    mandato.add(tipoMandato);
    mandato.add(estadoMandato);
    mandato.add(obtenerFechaHoraActual());

    if (estadoMandato.equals("Posible")) {
        mandatosPosibles.add(mandato);
    } else {
        mandatosNoPosibles.add(mandato);
    }
}
public void mover(String direccion) {

    if (validarPotenciaActual()) {

        if (!detectarFuga()) {

            boolean movimientoRealizado = true;

            switch (direccion) {

                case "adelante":
                    posicionActualY++;
                    break;

                case "atras":
                    posicionActualY--;
                    break;

                case "derecha":
                    posicionActualX++;
                    break;

                case "izquierda":
                    posicionActualX--;
                    break;

                default:
                    movimientoRealizado = false;
                    break;
            }

            if (movimientoRealizado) {
                potenciaDisponible -= consumoPorMovimiento;
                registrarMandato("Movimiento " + direccion, "Posible");
            } else {
                registrarMandato("Movimiento " + direccion, "No posible: dirección inválida");
            }

        } else {
            registrarMandato("Movimiento " + direccion, "No posible: fuga detectada");
        }

    } else {
        registrarMandato("Movimiento " + direccion, "No posible: potencia insuficiente");
    }
}
public void recargarPotencia(double potencia) {

    if (validarRecarga()) {
        potenciaDisponible += potencia;
        recargasRealizadas++;

        registrarMandato(
            "Recarga de potencia",
            "Posible"
        );

    } else {
        registrarMandato(
            "Recarga de potencia",
            "No posible: recargas agotadas"
        );
    }
}
public String toString() {
    String informacion = "";

    informacion += "Código: " + codigoRover + "\n";
    informacion += "Nombre: " + nombre + "\n";
    informacion += "Potencia inicial: " + potenciaInicial + "\n";
    informacion += "Potencia disponible: " + potenciaDisponible + "\n";
    informacion += "Recargas disponibles: " + consultarRecargasDisponibles() + "\n";
    informacion += "Recargas realizadas: " + recargasRealizadas + "\n";
    informacion += "Cantidad de detecciones de calor: "
        + cantidadDeteccionesDeCalor + "\n";
    informacion += "Posición inicial: (" + posicionInicialX
        + ", " + posicionInicialY + ")\n";
    informacion += "Posición actual: (" + posicionActualX
        + ", " + posicionActualY + ")\n";

    informacion += "\nMandatos posibles:\n";

    for (ArrayList<String> mandato : mandatosPosibles) {
        informacion += mandato + "\n";
    }

    informacion += "\nMandatos no posibles:\n";

    for (ArrayList<String> mandato : mandatosNoPosibles) {
        informacion += mandato + "\n";
    }

    return informacion;
}
public static int consultarCantidadRovers() {
    return contadorRovers;
}


public static String consultarRoversCreados() {
    String informacion = "";

    for (Rover rover : informacionRovers) {
        informacion += rover.toString() + "\n";
    }

    return informacion;
}
}