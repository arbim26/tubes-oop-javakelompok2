package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaksi {
    private String deskripsi;
    private double jumlah;
    private KategoriTransaksi kategori;
    private LocalDate tanggal;
    private boolean selected; // untuk bulk action
    
    public Transaksi(String deskripsi, double jumlah, KategoriTransaksi kategori, LocalDate tanggal) {
        this.deskripsi = deskripsi;
        this.jumlah = jumlah;
        this.kategori = kategori;
        this.tanggal = tanggal;
        this.selected = false;
    }
    
    // Getter & Setter
    public String getDeskripsi() { return deskripsi; }
    public double getJumlah() { return jumlah; }
    public KategoriTransaksi getKategori() { return kategori; }
    public LocalDate getTanggal() { return tanggal; }
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
    
    public String getFormattedTanggal() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return tanggal.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("%s | %s | Rp %,.2f | %s", 
            getFormattedTanggal(), kategori.getNama(), jumlah, deskripsi);
    }
// Tambahkan method yang belum diimplementasikan di Transaksi.java

public boolean isPinjaman() {
    return this.kategori.isPinjaman(); // Gunakan method dari KategoriTransaksi
}

public boolean isPelunasan() {
    return this.kategori.isPelunasan(); // Gunakan method dari KategoriTransaksi
}
}