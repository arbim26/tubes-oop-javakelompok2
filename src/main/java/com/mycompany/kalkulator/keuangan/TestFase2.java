package com.mycompany.kalkulator.keuangan;

import java.time.LocalDate;
import java.util.List;

public class TestFase2 {
    public static void main(String[] args) {
        System.out.println("=== TEST FASE 2: BUSINESS LOGIC ===");
        System.out.println();

        //1. Testing Transaksi class
        System.out.println("1. Testing Transaksi Class:");
        System.out.println("---------------------------");

        Transaksi t1 = new Transaksi("Gaji Bulan Januari", 5000000, 
                                    KategoriTransaksi.GAJI, LocalDate.of(2024, 1, 15));
        System.out.println("Transaksi 1:");
        System.out.println("  Deskripsi: " + t1.getDeskripsi());
        System.out.println("  Jumlah: Rp " + String.format("%,.2f", t1.getJumlah()));
        System.out.println("  Kategori: " + t1.getKategori().getNama());
        System.out.println("  Tanggal: " + t1.getFormattedTanggal());
        System.out.println("  toString(): " + t1.toString());
        System.out.println();

        Transaksi t2 = new Transaksi("Makan Siang", 50000, 
                                    KategoriTransaksi.MAKANAN, LocalDate.of(2024, 1, 16));
        System.out.println("Transaksi 2:");
        System.out.println("  Deskripsi: " + t2.getDeskripsi());
        System.out.println("  Kategori: " + t2.getKategori().getNama());
        System.out.println("  isPemasukan? " + t2.getKategori().isPemasukan());
        System.out.println("  toString(): " + t2.toString());
        System.out.println();

        //2. Testing ManagerKeuangan class
        System.out.println("2. Testing ManagerKeuangan Class:");
        System.out.println("--------------------------------");

        ManagerKeuangan manager = new ManagerKeuangan();

        try {
            //Tambah beberapa transaksi
            manager.tambahTransaksi("Gaji Januari", 5000000, 
                                   KategoriTransaksi.GAJI, "15-01-2024");
            manager.tambahTransaksi("Bonus Proyek", 1000000, 
                                   KategoriTransaksi.BONUS, "20-01-2024");
            manager.tambahTransaksi("Makan Siang", 50000, 
                                   KategoriTransaksi.MAKANAN, "16-01-2024");
            manager.tambahTransaksi("Bensin Motor", 30000, 
                                   KategoriTransaksi.TRANSPORTASI, "18-01-2024");
            manager.tambahTransaksi("Belanja Bulanan", 750000, 
                                   KategoriTransaksi.BELANJA, "22-01-2024");
            
            System.out.println("Transaksi berhasil ditambahkan!");
            System.out.println("Total transaksi: "+ manager.getAllTransaksi().size());
        
    }catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
    System.out.println();

    //3.Testing perhitungan
    System.out.println("3.Testing Perhitungan:");
    System.out.println("----------------------");

    double totalPemasukan = manager.hitungTotalPemasukan();
    double totalPengeluaran = manager.hitungTotalPengeluaran();
    double saldo = manager.hitungSaldo();

    System.out.println("Total Pemasukan: Rp " + String.format("%,.2f", totalPemasukan));
        System.out.println("Total Pengeluaran: Rp " + String.format("%,.2f", totalPengeluaran));
        System.out.println("Saldo: Rp " + String.format("%,.2f", saldo));
        System.out.println();

        //4.Testing laporan ringkasan
        System.out.println("4. Testing Laporan Ringkasan:");
        System.out.println("-----------------------------");
        
        String[] laporan = manager.generateLaporanRingkasan();
        for (String line : laporan) {
            System.out.println(line);
        }
        System.out.println();

        //5.Testing get transaksi by kategori
                System.out.println("5. Testing Transaksi Berdasarkan Kategori:");
        System.out.println("-----------------------------------------");
        
        List<Transaksi> transaksiMakanan = manager.getTransaksiByKategori(KategoriTransaksi.MAKANAN);
        System.out.println("Transaksi Kategori Makanan: " + transaksiMakanan.size() + " item");
        for (Transaksi t : transaksiMakanan) {
            System.out.println("  - " + t.toString());
        }
        System.out.println();

        //6.Testing semua transaksi
        System.out.println("6. Semua Transaksi:");
        System.out.println("------------------");
        
        List<Transaksi> semuaTransaksi = manager.getAllTransaksi();
        for (int i = 0; i < semuaTransaksi.size(); i++) {
            System.out.println((i + 1) + ". " + semuaTransaksi.get(i).toString());
        }
        System.out.println();

        //7.Testing exception handling
        System.out.println("7. Testing Exception Handling:");
        System.out.println("-----------------------------");
        
        try {
            // Test jumlah negatif
            System.out.println("Mencoba menambah transaksi dengan jumlah 0:");
            manager.tambahTransaksi("Test Jumlah 0", 0, 
                                   KategoriTransaksi.GAJI, "01-01-2024");
        } catch (NegativeAmountException e) {
            System.out.println("✓ NegativeAmountException berhasil ditangkap: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Exception lain: " + e.getMessage());
        }

        try {
            // Test format tanggal salah
            System.out.println("\nMencoba menambah transaksi dengan format tanggal salah:");
            manager.tambahTransaksi("Test Tanggal", 10000, 
                                   KategoriTransaksi.MAKANAN, "2024-01-01");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ IllegalArgumentException berhasil ditangkap: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Exception lain: " + e.getMessage());
        }

        try {
            // Test jumlah negatif
            System.out.println("\nMencoba menambah transaksi dengan jumlah negatif:");
            manager.tambahTransaksi("Test Negatif", -1000, 
                                   KategoriTransaksi.MAKANAN, "01-01-2024");
        } catch (NegativeAmountException e) {
            System.out.println("✓ NegativeAmountException berhasil ditangkap: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Exception lain: " + e.getMessage());
        }

        //8.Testing transaksi bulan ini
        System.out.println("\n8. Testing Transaksi Bulan Ini:");
        System.out.println("------------------------------");
        
        // Tambah transaksi bulan ini (gunakan tanggal sekarang)
        String tanggalSekarang = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        try {
            manager.tambahTransaksi("Gaji Bulan Ini", 5500000, 
                                   KategoriTransaksi.GAJI, tanggalSekarang);
            manager.tambahTransaksi("Makan Siang Hari Ini", 45000, 
                                   KategoriTransaksi.MAKANAN, tanggalSekarang);

            List<Transaksi> transaksiBulanIni = manager.getTransaksiBulanIni();
            System.out.println("Transaksi bulan ini: " + transaksiBulanIni.size() + " item");

            for (Transaksi t : transaksiBulanIni) {
                System.out.println("  - " + t.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println("\n=== TEST FASE 2 SELESAI ===");
    }
}