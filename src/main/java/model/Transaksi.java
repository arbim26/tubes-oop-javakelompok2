package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaksi {

    private String deskripsi;
    private double jumlah;
    private KategoriTransaksi kategori;
    private LocalDate tanggal;
    private boolean selected; // untuk bulk action (checkbox, dll)

    // Constructor
    public Transaksi(String deskripsi, double jumlah, KategoriTransaksi kategori, LocalDate tanggal) {
        this.deskripsi = deskripsi;
        this.jumlah = jumlah;
        this.kategori = kategori;
        this.tanggal = tanggal;
        this.selected = false;
    }

    // Getter
    public String getDeskripsi() {
        return deskripsi;
    }

    public double getJumlah() {
        return jumlah;
    }

    public KategoriTransaksi getKategori() {
        return kategori;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public boolean isSelected() {
        return selected;
    }

    // Setter
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // Format tanggal agar user-friendly
    public String getFormattedTanggal() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return tanggal.format(formatter);
    }

    // Representasi string untuk GUI / log
    @Override
    public String toString() {
        return String.format(
            "%s | %s | Rp %,.2f | %s",
            getFormattedTanggal(),
            kategori.getNama(),
            jumlah,
            deskripsi
        );
    }

    // Logic helper (delegasi ke KategoriTransaksi)
    public boolean isPinjaman() {
        return kategori.isPinjaman();
    }

    public boolean isPelunasan() {
        return kategori.isPelunasan();
    }
}
