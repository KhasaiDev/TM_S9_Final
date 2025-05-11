import java.util.ArrayList;
import java.util.Scanner;

public class App {
    // Definición de constantes
    // Cantidad máxima de entradas y asientos disponibles
    static final int MAX_ENTRADAS = 10;
    static final int MAX_ASIENTOS = 25;

    // Información de clientes
    static int[] idsClientes = new int[MAX_ENTRADAS];               // ID único para cada cliente
    static String[] tiposClientes = new String[MAX_ENTRADAS];       // Tipo de cliente (Niño, Mujer, etc.)
    static int[] edadesClientes = new int[MAX_ENTRADAS];            // Edad de cada cliente

    // Información de ventas
    static int[] idsVentas = new int[MAX_ENTRADAS];                 // ID único para cada venta
    static String[] asientosVendidos = new String[MAX_ENTRADAS];   // Código del asiento vendido (ej. A1)
    static int[] ventasClienteID = new int[MAX_ENTRADAS];          // Relación de la venta con el cliente
    static float[] preciosFinales = new float[MAX_ENTRADAS];       // Precio final de la venta con descuento

    // Asientos disponibles en el teatro
    static String[] asientos = new String[MAX_ASIENTOS];           // Nombres de asientos (A1, A2, ...)
    static boolean[] disponibilidadAsientos = new boolean[MAX_ASIENTOS]; // Si el asiento está disponible o no

    // Promociones disponibles
    static ArrayList<String> promociones = new ArrayList<>();

    // Reservas hechas (se registran al vender una entrada)
    static ArrayList<Integer> reservasID = new ArrayList<>();
    static ArrayList<Integer> reservasClienteID = new ArrayList<>();
    static ArrayList<String> reservasAsiento = new ArrayList<>();

    // Contadores e IDs automáticos
    static int totalClientes = 0;    // Cantidad total de clientes registrados
    static int totalVentas = 0;      // Cantidad total de entradas vendidas
    static int idClienteAuto = 1;    // ID automático para cada nuevo cliente
    static int idVentaAuto = 1;      // ID automático para cada nueva venta
    static int idReservaAuto = 1;    // ID automático para cada nueva reserva

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        inicializarAsientos();
        inicializarPromociones();

        while (true) {
            System.out.println("\n----- MENÚ -----");
            System.out.println("1. Comprar entrada");
            System.out.println("2. Ver promociones");
            System.out.println("3. Ver reservas");
            System.out.println("4. Eliminar entrada");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    comprarEntrada(scanner);
                    break;
                case 2:
                    verPromociones();
                    break;
                case 3:
                    verReservas();
                    break;
                case 4:
                    System.out.print("Ingrese el ID de la venta a eliminar: ");
                    int idVenta = scanner.nextInt();
                    eliminarEntrada(idVenta);
                    break;
                case 5:
                    System.out.println("Gracias por usar el sistema.");
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    static void inicializarAsientos() {
        for (int i = 0; i < MAX_ASIENTOS; i++) {
            asientos[i] = "A" + (i + 1);
            disponibilidadAsientos[i] = true;
        }
    }

    static void inicializarPromociones() {
        promociones.add("Niños: 10% de descuento (<13 años)");
        promociones.add("Mujeres: 20% de descuento");
        promociones.add("Estudiantes: 15% de descuento");
        promociones.add("Tercera Edad: 25% de descuento (>=60 años)");
    }

    static void comprarEntrada(Scanner scanner) {
        if (totalVentas >= MAX_ENTRADAS) {
            System.out.println("Límite de ventas alcanzado.");
            return;
        }

        System.out.println("Asientos disponibles:");
        for (int i = 0; i < MAX_ASIENTOS; i++) {
            if (disponibilidadAsientos[i]) {
                System.out.print(asientos[i] + " ");
            }
        }

        System.out.print("\nSeleccione un asiento: ");
        String asientoSeleccionado = scanner.next();
        int indiceAsiento = -1;
        for (int i = 0; i < MAX_ASIENTOS; i++) {
            if (asientos[i].equalsIgnoreCase(asientoSeleccionado)) {
                if (disponibilidadAsientos[i]) {
                    indiceAsiento = i;
                }
                break;
            }
        }
        if (indiceAsiento == -1) {
            System.out.println("Asiento no disponible o no existe.");
            return;
        }

        // Calcular el tipo de cliente y descuentos aplicables
        System.out.print("Ingrese su edad: ");
        int edad = scanner.nextInt();
        
        System.out.println("Elige tu género:");
        System.out.println("1. Hombre");
        System.out.println("2. Mujer");
        int genero = scanner.nextInt();
        
        System.out.println("¿Es estudiante?");
        System.out.println("1. Sí");
        System.out.println("2. No");
        int estudiante = scanner.nextInt();

        // Determinar tipo de cliente y descuento
        String tipoCliente = "General";
        float descuento = 0f;

        if (edad < 13) {
            tipoCliente = "Niño";
            descuento = 0.10f;
        } else if (edad >= 60) {
            tipoCliente = "Tercera Edad";
            descuento = 0.25f;
        } else if (genero == 2) {
            tipoCliente = "Mujer";
            descuento = 0.20f;
        } else if (estudiante == 1) {
            tipoCliente = "Estudiante";
            descuento = 0.15f;
        }

        // Registrar cliente
        int idCliente = idClienteAuto++;
        idsClientes[totalClientes] = idCliente;
        edadesClientes[totalClientes] = edad;
        tiposClientes[totalClientes] = tipoCliente;
        totalClientes++;

        // Precio base según la sección del asiento
        float precioBase = 10000f;
        float precioFinal = precioBase * (1 - descuento);

         // Registrar la venta
        int idVenta = idVentaAuto++;
        idsVentas[totalVentas] = idVenta;
        asientosVendidos[totalVentas] = asientos[indiceAsiento];
        ventasClienteID[totalVentas] = idCliente;
        preciosFinales[totalVentas] = precioFinal;
        totalVentas++;

        disponibilidadAsientos[indiceAsiento] = false; // Marcar el asiento como vendido

        // Registrar reserva
        reservasID.add(idReservaAuto++);
        reservasClienteID.add(idCliente);
        reservasAsiento.add(asientos[indiceAsiento]);

        // La boleta:
        System.out.println("\n--- BOLETA ---");
        System.out.println("Cliente ID: " + idCliente);
        System.out.println("Edad: " + edad);
        System.out.println("Tipo: " + tipoCliente);
        System.out.println("Asiento: " + asientos[indiceAsiento]);
        System.out.println("Precio base: $" + precioBase);
        System.out.println("Descuento: " + (int)(descuento * 100) + "%");
        System.out.printf("Total a pagar: $%.2f\n", precioFinal);
        System.out.println("---------------");
    }

    static void eliminarEntrada(int idVenta) {
    for (int i = 0; i < totalVentas; i++) {
        if (idsVentas[i] == idVenta) {
            // Marcar el asiento como disponible
            int indiceAsiento = i;
            disponibilidadAsientos[indiceAsiento] = true;

            // Eliminar la reserva correspondiente
            for (int j = 0; j < reservasID.size(); j++) {
                if (reservasID.get(j) == idVenta) {
                    // Eliminar la reserva en las listas de reservas
                    reservasID.remove(j);
                    reservasClienteID.remove(j);
                    reservasAsiento.remove(j);
                    break;
                }
            }

            // Desplazar todas las entradas después de la entrada eliminada
            for (int j = i; j < totalVentas - 1; j++) {
                idsVentas[j] = idsVentas[j + 1];
                asientosVendidos[j] = asientosVendidos[j + 1];
                ventasClienteID[j] = ventasClienteID[j + 1];
                preciosFinales[j] = preciosFinales[j + 1];
            }

            // Reducir el total de ventas
            totalVentas--;

            System.out.println("Entrada eliminada con éxito.");
            return;
        }
    }
    System.out.println("ID de venta no encontrado.");
}

    static void verPromociones() {
        System.out.println("Promociones disponibles:");
        for (String promo : promociones) {
            System.out.println("- " + promo);
        }
    }

    static void verReservas() {
        System.out.println("Reservas registradas:");
        for (int i = 0; i < reservasID.size(); i++) {
            int clienteID = reservasClienteID.get(i);
            String tipo = "Desconocido";
            for (int j = 0; j < totalClientes; j++) {
                if (idsClientes[j] == clienteID) {
                    tipo = tiposClientes[j];
                    break;
                }
            }
            System.out.println("Reserva ID: " + reservasID.get(i) +
                    ", Cliente ID: " + clienteID +
                    " (" + tipo + ")" +
                    ", Asiento: " + reservasAsiento.get(i));
        }
    }
}
