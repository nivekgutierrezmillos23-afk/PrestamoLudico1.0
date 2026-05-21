package org.example.model;

public class Prestamo {
    private String estudiante;
    private String documento;
    private String objeto;
    private String horaSalida;

    public Prestamo(String estudiante, String documento, String objeto, String horaSalida) {
        this.estudiante = estudiante;
        this.documento = documento;
        this.objeto = objeto;
        this.horaSalida = horaSalida;
    }

    public String getEstudiante() { return estudiante; }
    public String getDocumento() { return documento; }
    public String getObjeto() { return objeto; }
    public String getHoraSalida() { return horaSalida; }
}
