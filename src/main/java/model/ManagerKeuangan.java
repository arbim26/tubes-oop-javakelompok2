package model;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.kalkulator.keuangan.NegativeAmountException;

public class ManagerKeuangan {
    private List<Transaksi> daftarTransaksi;
    
    public ManagerKeuangan() {
        this.daftarTransaksi = new ArrayList<>();
    }
    
    // Method untuk menambah transaksi dengan validasi
    public void tambahTransaksi(String deskripsi, double jumlah, 
                               KategoriTransaksi kategori, String tanggalStr) 
                               throws NegativeAmountException, IllegalArgumentException {
        
        // Validasi jumlah tidak negatif
        if (jumlah <= 0) {
            throw new NegativeAmountException("Jumlah transaksi harus lebih dari 0");
        }
        
        // Parse tanggal (format: dd-MM-yyyy)
        java.time.LocalDate tanggal;
        try {
            String[] parts = tanggalStr.split("-");
            int hari = Integer.parseInt(parts[0]);
            int bulan = Integer.parseInt(parts[1]);
            int tahun = Integer.parseInt(parts[2]);
            tanggal = java.time.LocalDate.of(tahun, bulan, hari);
        } catch (Exception e) {
            throw new IllegalArgumentException("Format tanggal salah. Gunakan dd-MM-yyyy");
        }
        
        // Buat dan tambahkan transaksi
        Transaksi transaksi = new Transaksi(deskripsi, jumlah, kategori, tanggal);
        daftarTransaksi.add(transaksi);
    }
    
    // Hitung total pemasukan
    public double hitungTotalPemasukan() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) { // Perulangan for-each
            if (t.getKategori().isPemasukan()) {
                total += t.getJumlah();
            }
        }
        return total;
    }
    
    // Hitung total pengeluaran
    public double hitungTotalPengeluaran() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) { // Perulangan for-each
            if (!t.getKategori().isPemasukan()) {
                total += t.getJumlah();
            }
        }
        return total;
    }
    
    // Hitung saldo akhir
    public double hitungSaldo() {
        return hitungTotalPemasukan() - hitungTotalPengeluaran();
    }
    
    // Generate laporan ringkasan menggunakan array
    public String[] generateLaporanRingkasan() {
        String[] laporan = new String[5]; // Array untuk laporan
        
        laporan[0] = "==== LAPORAN KEUANGAN ====";
        laporan[1] = String.format("Total Pemasukan: Rp %,.2f", hitungTotalPemasukan());
        laporan[2] = String.format("Total Pengeluaran: Rp %,.2f", hitungTotalPengeluaran());
        laporan[3] = String.format("Saldo: Rp %,.2f", hitungSaldo());
        
        // Analisis kesehatan keuangan
        double rasio = hitungTotalPengeluaran() / hitungTotalPemasukan() * 100;
        if (Double.isNaN(rasio)) rasio = 0;
        laporan[4] = String.format("Rasio Pengeluaran/Pemasukan: %.1f%%", rasio);
        
        return laporan;
    }
    
    // Get transaksi berdasarkan kategori
    public List<Transaksi> getTransaksiByKategori(KategoriTransaksi kategori) {
        List<Transaksi> result = new ArrayList<>();
        for (Transaksi t : daftarTransaksi) {
            if (t.getKategori() == kategori) {
                result.add(t);
            }
        }
        return result;
    }
    
    // Get semua transaksi
    public List<Transaksi> getAllTransaksi() {
        return new ArrayList<>(daftarTransaksi);
    }
    
    // Get transaksi bulan ini (contoh filter)
    public List<Transaksi> getTransaksiBulanIni() {
        List<Transaksi> result = new ArrayList<>();
        java.time.LocalDate sekarang = java.time.LocalDate.now();
        
        for (Transaksi t : daftarTransaksi) {
            if (t.getTanggal().getMonth() == sekarang.getMonth() && 
                t.getTanggal().getYear() == sekarang.getYear()) {
                result.add(t);
            }
        }
        return result;
    }
}

