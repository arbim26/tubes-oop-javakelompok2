package com.mycompany.kalkulator.keuangan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//Class untuk menyimpan data transaksi
public class Transaksi {
    private String deskripsi;
    private double jumlah;
    private KategoriTransaksi kategori;
    private LocalDate tanggal;

    public Transaksi(String deskripsi, double jumlah, KategoriTransaksi gaji, LocalDate tanggal) {
        this.deskripsi = deskripsi;
        this.jumlah = jumlah;
        this.kategori = gaji;
        this.tanggal = tanggal;
    }

    //Getter methods
    public String getDeskripsi() {return deskripsi;}
    public double getJumlah() {return jumlah;}
    public KategoriTransaksi getKategori () {return kategori;}
    public LocalDate getTanggal() {return tanggal;}

    public String getFormattedTanggal() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return tanggal.format(formatter);
    }

    @Override
    public String toString() {
        return String.format("%s|%s|Rp %,.2f|%s", getFormattedTanggal(), kategori.getNama(), jumlah, deskripsi);
    } 
}