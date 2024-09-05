
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

public class Jugador {

    private final int TOTAL_CARTA = 10;
    private final int MARGEN = 20;
    private final int DISTANCIA = 48;
    private Carta[] cartas = new Carta[TOTAL_CARTA];
    private Random r = new Random();

    public void repartir() {
        int i = 0;
        for (Carta c : cartas) {
            cartas[i++] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        int p = 1;
        for (Carta c : cartas) {
            c.mostrar(pnl, MARGEN + TOTAL_CARTA * DISTANCIA - p++ * DISTANCIA, MARGEN);
        }
        pnl.repaint();
    }

    //Metodo para encontrar grupos de cartas (No definidas por su pinta)
    public String getGrupos() {
        String mensaje = "No se encontraron figuras";
        int[] contadores = new int[NombreCarta.values().length];
        for (Carta c : cartas) {
            contadores[c.getNombre().ordinal()]++;
        }
        boolean hayGrupos = false;
        for (int i = 0; i < contadores.length; i++) {
            if (contadores[i] >= 2) {
                if (!hayGrupos) {
                    hayGrupos = true;
                    mensaje = "Se encontraron los siguientes grupos:\n";
                }
                mensaje += Grupo.values()[contadores[i]] + " de " + NombreCarta.values()[i] + "\n";
            }
        }
        return mensaje;
    }

    //Metodo para hayar las escaleras por pinta
    public List<List<Carta>> getEscaleras() {
        List<List<Carta>> escaleras = new ArrayList<>();

        for (Pinta pinta : Pinta.values()) {
            List<Carta> cartasDePinta = new ArrayList<>();

            for (Carta c : cartas) {
                if (c.getPinta() == pinta) {
                    cartasDePinta.add(c);
                }
            }

            if (cartasDePinta.size() >= 2) {
                Collections.sort(cartasDePinta, (a, b) -> a.getNombre().ordinal() - b.getNombre().ordinal());

                List<Carta> escaleraActual = new ArrayList<>();
                escaleraActual.add(cartasDePinta.get(0));

                for (int i = 1; i < cartasDePinta.size(); i++) {
                    Carta cartaActual = cartasDePinta.get(i);
                    Carta cartaAnterior = cartasDePinta.get(i - 1);

                    if (cartaActual.getNombre().ordinal() == cartaAnterior.getNombre().ordinal() + 1) {
                        escaleraActual.add(cartaActual);
                    } else {
                        if (escaleraActual.size() >= 2) {
                            escaleras.add(new ArrayList<>(escaleraActual));
                        }
                        escaleraActual.clear();
                        escaleraActual.add(cartaActual);
                    }
                }

                if (escaleraActual.size() >= 2) {
                    escaleras.add(escaleraActual);
                }
            }
        }
        return escaleras;
    }

    /*Metodo para calcular puntaje de los jugadores por el valor de la carta sin contar aquellas que
    pertenezcan a una escalera (es decir el puntaje solo sera valido para aquellas cartas que no 
    hagan parte de una escalera)*/
    public int calcularPuntaje() {
        List<List<Carta>> escaleras = getEscaleras();
        List<Carta> cartasUsadas = new ArrayList<>();

        for (List<Carta> escalera : escaleras) {
            cartasUsadas.addAll(escalera);
        }

        int puntaje = 0;
        for (Carta c : cartas) {
            if (!cartasUsadas.contains(c)) {
                NombreCarta nombre = c.getNombre();
                if (nombre == NombreCarta.AS || nombre == NombreCarta.JACK
                        || nombre == NombreCarta.QUEEN || nombre == NombreCarta.KING) {
                    puntaje += 10;
                } else {
                    puntaje += nombre.ordinal() + 1;
                }
            }
        }
        return puntaje;
    }

    /*Metodo necesario para definir el nombre de la carta en la escalera
    definiendo su nombre (UNO, DOS,AS,JACK...) y la pinta a la que pertenece*/
    public String mostrarEscaleras() {
        List<List<Carta>> escaleras = getEscaleras();
        StringBuilder mensaje = new StringBuilder();

        if (escaleras.isEmpty()) {
            mensaje.append("No se encontraron escaleras.");
        } else {
            mensaje.append("Se encontraron la(s) siguiente(s) escalera(s):\n");
            for (List<Carta> escalera : escaleras) {
                Pinta pinta = escalera.get(0).getPinta();
                mensaje.append("Escalera de ").append(pinta).append(": ");
                for (Carta carta : escalera) {
                    mensaje.append(carta.getNombre()).append(" de ").append(carta.getPinta()).append(" ; ");
                }
                mensaje.append("\n");
            }
        }
        return mensaje.toString();
    }
}
//Chat GPT usado en el proceso por lo menos un 50%
