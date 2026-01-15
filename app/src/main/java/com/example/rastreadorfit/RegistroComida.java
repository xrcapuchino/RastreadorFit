package com.example.rastreadorfit;

public class RegistroComida {

    // Atributos del "FitTracker"
    private int id;
    private String alimento;      // Ej: "Ensalada César" (Antes titulo)
    private String notas;         // Ej: "Sin aderezo" (Antes descripcion)
    private String momento;       // Ej: "Desayuno", "Comida" (Antes materia)
    private String nivelCalorico; // Ej: "Alto", "Bajo" (Antes prioridad)
    private String fecha;         // Fecha y hora del registro
    private boolean esSaludable;  // Checkbox para marcar si fue una comida sana (Antes completada)

    // Constructor vacío (Requerido)
    public RegistroComida() {
    }

    // Constructor completo con ID (Para leer de la BD)
    public RegistroComida(int id, String alimento, String notas, String momento, String nivelCalorico, String fecha, boolean esSaludable) {
        this.id = id;
        this.alimento = alimento;
        this.notas = notas;
        this.momento = momento;
        this.nivelCalorico = nivelCalorico;
        this.fecha = fecha;
        this.esSaludable = esSaludable;
    }

    // Constructor sin ID (Para crear nuevos registros antes de guardar en BD)
    public RegistroComida(String alimento, String notas, String momento, String nivelCalorico, String fecha, boolean esSaludable) {
        this.alimento = alimento;
        this.notas = notas;
        this.momento = momento;
        this.nivelCalorico = nivelCalorico;
        this.fecha = fecha;
        this.esSaludable = esSaludable;
    }

    // --- Getters y Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAlimento() { return alimento; }
    public void setAlimento(String alimento) { this.alimento = alimento; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getMomento() { return momento; }
    public void setMomento(String momento) { this.momento = momento; }

    public String getNivelCalorico() { return nivelCalorico; }
    public void setNivelCalorico(String nivelCalorico) { this.nivelCalorico = nivelCalorico; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public boolean isEsSaludable() { return esSaludable; }
    public void setEsSaludable(boolean esSaludable) { this.esSaludable = esSaludable; }
}