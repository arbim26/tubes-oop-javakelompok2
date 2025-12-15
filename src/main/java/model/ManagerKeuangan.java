package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import exception.NegativeAmountException;

public class ManagerKeuangan {
    private List<Transaksi> daftarTransaksi;
    
    public ManagerKeuangan() {
        this.daftarTransaksi = new ArrayList<>();
    }
    
    // Method untuk menambah transaksi
    public void tambahTransaksi(String deskripsi, double jumlah, 
                               KategoriTransaksi kategori, String tanggalStr) 
                               throws NegativeAmountException, IllegalArgumentException {
        
        if (jumlah <= 0) {
            throw new NegativeAmountException("Jumlah transaksi harus lebih dari 0");
        }
        
        // Validasi: Untuk pengeluaran, cek apakah saldo mencukupi
        if (!kategori.isPemasukan()) {
            double saldoSekarang = hitungSaldo();
            
            // Jika kategori adalah pelunasan hutang, kita perlu cek khusus
            if (kategori == KategoriTransaksi.PELUNASAN) {
                // Untuk pelunasan, cek apakah ada hutang yang bisa dilunasi
                double sisaHutang = hitungSisaHutang();
                if (jumlah > sisaHutang) {
                    throw new NegativeAmountException(
                        String.format("Jumlah pelunasan (Rp %,.2f) melebihi sisa hutang (Rp %,.2f)", 
                        jumlah, sisaHutang)
                    );
                }
            }
            
            // Cek saldo untuk SEMUA pengeluaran (termasuk pelunasan)
            if (saldoSekarang - jumlah < 0) {
                throw new NegativeAmountException(
                    String.format("Saldo tidak cukup! Saldo saat ini: Rp %,.2f", saldoSekarang)
                );
            }
        }
        
        // Parse tanggal
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
        
        Transaksi transaksi = new Transaksi(deskripsi, jumlah, kategori, tanggal);
        daftarTransaksi.add(transaksi);
    }
    
    // Hapus transaksi terpilih
    public void hapusTransaksiTerpilih() {
        Iterator<Transaksi> iterator = daftarTransaksi.iterator();
        while (iterator.hasNext()) {
            Transaksi transaksi = iterator.next();
            if (transaksi.isSelected()) {
                iterator.remove();
            }
        }
    }
    
    // Pilih semua transaksi
    public void pilihSemuaTransaksi(boolean selected) {
        for (Transaksi transaksi : daftarTransaksi) {
            transaksi.setSelected(selected);
        }
    }
    
    // Hitung total pemasukan (TERMASUK pinjaman)
    public double hitungTotalPemasukan() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) {
            if (t.getKategori().isPemasukan()) {
                total += t.getJumlah();
            }
        }
        return total;
    }
    
    // Hitung total pengeluaran (TERMASUK pelunasan)
    public double hitungTotalPengeluaran() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) {
            if (!t.getKategori().isPemasukan()) {
                total += t.getJumlah();
            }
        }
        return total;
    }
    
    // Hitung saldo AKTUAL (pemasukan - pengeluaran)
    public double hitungSaldo() {
        double totalPemasukan = hitungTotalPemasukan();
        double totalPengeluaran = hitungTotalPengeluaran();
        System.out.println("DEBUG - Total Pemasukan: " + totalPemasukan);
        System.out.println("DEBUG - Total Pengeluaran: " + totalPengeluaran);
        System.out.println("DEBUG - Saldo: " + (totalPemasukan - totalPengeluaran));
        return totalPemasukan - totalPengeluaran;
    }
    
    // Hitung total pinjaman (hutang yang masuk)
    public double hitungTotalPinjaman() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) {
            if (t.isPinjaman()) {
                total += t.getJumlah();
            }
        }
        return total;
    }
    
    // Hitung total pelunasan
    public double hitungTotalPelunasan() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) {
            if (t.isPelunasan()) {
                total += t.getJumlah();
            }
        }
        return total;
    }
    
    // Hitung sisa hutang (pinjaman - pelunasan)
    public double hitungSisaHutang() {
        double pinjaman = hitungTotalPinjaman();
        double pelunasan = hitungTotalPelunasan();
        return Math.max(0, pinjaman - pelunasan);
    }
    
    // Generate laporan ringkasan
    public String[] generateLaporanRingkasan() {
        String[] laporan = new String[10];
        
        double pemasukan = hitungTotalPemasukan();
        double pengeluaran = hitungTotalPengeluaran();
        double saldo = hitungSaldo();
        double pinjaman = hitungTotalPinjaman();
        double pelunasan = hitungTotalPelunasan();
        double sisaHutang = hitungSisaHutang();
        
        laporan[0] = "==== LAPORAN KEUANGAN ====";
        laporan[1] = String.format("Total Pemasukan: Rp %,.2f", pemasukan);
        laporan[2] = String.format("  - Termasuk Pinjaman: Rp %,.2f", pinjaman);
        laporan[3] = String.format("Total Pengeluaran: Rp %,.2f", pengeluaran);
        laporan[4] = String.format("  - Termasuk Pelunasan: Rp %,.2f", pelunasan);
        laporan[5] = "-----------------------------------";
        laporan[6] = String.format("SALDO AKTUAL: Rp %,.2f", saldo);
        laporan[7] = String.format("Sisa Hutang: Rp %,.2f", sisaHutang);
        
        // Analisis
        if (saldo < 0) {
            laporan[8] = "⚠ SALDO NEGATIF! Perlu tambahan pemasukan.";
        } else if (saldo < 100000) {
            laporan[8] = "⚠ Saldo rendah! Hati-hati dalam pengeluaran.";
        } else {
            laporan[8] = "✓ Saldo sehat.";
        }
        
        if (sisaHutang > 0) {
            laporan[9] = "⚠ PERINGATAN: Masih ada hutang yang belum dilunasi!";
        } else if (pinjaman > 0) {
            laporan[9] = "✓ Semua hutang sudah dilunasi!";
        } else {
            laporan[9] = "✓ Tidak ada hutang.";
        }
        
        return laporan;
    }
    
    // Get semua transaksi
    public List<Transaksi> getAllTransaksi() {
        return new ArrayList<>(daftarTransaksi);
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
    
    // Get transaksi pemasukan saja
    public List<Transaksi> getTransaksiPemasukan() {
        List<Transaksi> result = new ArrayList<>();
        for (Transaksi t : daftarTransaksi) {
            if (t.getKategori().isPemasukan()) {
                result.add(t);
            }
        }
        return result;
    }
    
    // Get transaksi pengeluaran saja
    public List<Transaksi> getTransaksiPengeluaran() {
        List<Transaksi> result = new ArrayList<>();
        for (Transaksi t : daftarTransaksi) {
            if (!t.getKategori().isPemasukan()) {
                result.add(t);
            }
        }
        return result;
    }
    
    // Cek apakah ada transaksi yang dipilih
    public boolean adaTransaksiTerpilih() {
        for (Transaksi transaksi : daftarTransaksi) {
            if (transaksi.isSelected()) {
                return true;
            }
        }
        return false;
    }

// Tambahkan method yang belum diimplementasikan di ManagerKeuangan.java
public double hitungTotalHutang() {
    return hitungTotalPinjaman(); // Total hutang adalah total pinjaman
}

public double hitungSaldoBersih() {
    // Saldo bersih = Saldo aktual - Sisa hutang
    double saldoAktual = hitungSaldo();
    double sisaHutang = hitungSisaHutang();
    return saldoAktual - sisaHutang;
}
}