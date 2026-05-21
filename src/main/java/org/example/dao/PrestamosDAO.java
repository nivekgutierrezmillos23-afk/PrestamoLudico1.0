package org.example.dao;

import org.example.model.ObjetoCatalogo;
import org.example.model.Prestamo;
import java.util.List;

public interface PrestamosDAO {
    List<ObjetoCatalogo> listarCatalogo();
    List<Prestamo> listarPrestamosActivos();
    boolean registrarPrestamo(String estudiante, String documento, String objeto);
    boolean devolverObjeto(String documento, String objeto);
}
