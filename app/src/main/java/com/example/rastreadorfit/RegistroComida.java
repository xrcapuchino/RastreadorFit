package com.example.rastreadorfit;

public class RegistroComida {

    private int id;
    private String alimento;
    private String notas;
    private String momento; // Esto ahora se elegirá con RadioButtons
    private int calorias;   // NUEVO: Número entero para hacer sumas
    private String fecha;
    private boolean esSaludable;

    public RegistroComida() { }

    public RegistroComida(int id, String alimento, String notas, String momento, int calorias, String fecha, boolean esSaludable) {
        this.id = id;
        this.alimento = alimento;
        this.notas = notas;
        this.momento = momento;
        this.calorias = calorias;
        this.fecha = fecha;
        this.esSaludable = esSaludable;
    }

    public RegistroComida(String alimento, String notas, String momento, int calorias, String fecha, boolean esSaludable) {
        this.alimento = alimento;
        this.notas = notas;
        this.momento = momento;
        this.calorias = calorias;
        this.fecha = fecha;
        this.esSaludable = esSaludable;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAlimento() { return alimento; }
    public void setAlimento(String alimento) { this.alimento = alimento; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getMomento() { return momento; }
    public void setMomento(String momento) { this.momento = momento; }

    public int getCalorias() { return calorias; }
    public void setCalorias(int calorias) { this.calorias = calorias; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public boolean isEsSaludable() { return esSaludable; }
    public void setEsSaludable(boolean esSaludable) { this.esSaludable = esSaludable; }
}