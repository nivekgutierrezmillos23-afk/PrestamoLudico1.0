package org.example;

import org.example.dao.PrestamosDAO;
import org.example.dao.PrestamosDAOImpl;
import org.example.model.ObjetoCatalogo;
import org.example.model.Prestamo;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final PrestamosDAO dao = new PrestamosDAOImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("=============================================");
            System.out.println("     PRESTAMOS LUDICOS U-CUNDINAMARCA        ");
            System.out.println("=============================================");
            System.out.println("1. Ver Catalogo de Objetos (Stock)");
            System.out.println("2. Solicitar un Prestamo");
            System.out.println("3. Ver Lista de Prestamos Activos");
            System.out.println("4. Realizar una Devolucion");
            System.out.println("5. Salir del Sistema");
            System.out.print("Seleccione una opcion: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Opcion invalida. Ingrese un numero: ");
                scanner.next();
            }
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    mostrarCatalogo();
                    break;
                case 2:
                    crearPrestamo();
                    break;
                case 3:
                    mostrarPrestamos();
                    break;
                case 4:
                    procesarDevolucion();
                    break;
                case 5:
                    System.out.println("Gracias por usar el sistema, Saliendo...");
                    break;
                default:
                    System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (opcion != 5);
    }

    private static void mostrarCatalogo() {
        List<ObjetoCatalogo> objetos = dao.listarCatalogo();
        System.out.println("--- CATALOGO DE OBJETOS EN DOCKER ---");
        System.out.printf("%-25s | %-10s", "Nombre del Objeto", "Cantidad");
        System.out.println("----------------------------------------");
        for (ObjetoCatalogo obj : objetos) {
            System.out.printf("%-25s | %-10d", obj.getNombre(), obj.getCantidad());
        }
    }

    private static void mostrarPrestamos() {
        List<Prestamo> prestamos = dao.listarPrestamosActivos();
        System.out.println("--- LISTA DE PRESTAMOS ACTIVOS ---");
        System.out.printf("%-15s | %-12s | %-20s | %-10s", "Estudiante", "Documento", "Objeto", "Hora");
        System.out.println("----------------------------------------------------------------------");
        for (Prestamo p : prestamos) {
            System.out.printf("%-15s | %-12s | %-20s | %-10s", p.getEstudiante(), p.getDocumento(), p.getObjeto(), p.getHoraSalida());
        }
    }

    private static void crearPrestamo() {
        System.out.println("--- REGISTRAR NUEVO PRESTAMO ---");
        System.out.print("Nombre del Estudiante: ");
        String estudiante = scanner.nextLine().trim();
        System.out.print("Documento de Identidad: ");
        String documento = scanner.nextLine().trim();
        System.out.print("Nombre exacto del Objeto a prestar: ");
        String objeto = scanner.nextLine().trim();

        if (estudiante.isEmpty() || documento.isEmpty() || objeto.isEmpty()) {
            System.out.println(" Error: No se permiten campos vacios.");
            return;
        }

        boolean exito = dao.registrarPrestamo(estudiante, documento, objeto);
        if (exito) {
            System.out.println("Prestamo guardado con exito en MySQL");
        } else {
            System.out.println(" Error: No hay stock disponible o el objeto no existe.");
        }
    }

    private static void procesarDevolucion() {
        System.out.println("--- REALIZAR DEVOLUCION ---");
        System.out.print("Documento del Estudiante: ");
        String documento = scanner.nextLine().trim();
        System.out.print("Nombre exacto del Objeto a devolver: ");
        String objeto = scanner.nextLine().trim();

        boolean exito = dao.devolverObjeto(documento, objeto);
        if (exito) {
            System.out.println("Devolucion procesada e inventario actualizado");
        } else {
            System.out.println(" Error: No se encontro ningun prestamo activo con esos datos.");
        }
    }
}