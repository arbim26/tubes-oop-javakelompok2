package com.mycompany.kalkulator.keuangan;

public class Main {
        public static void main(String[] args) {
        System.out.println("=== TEST KATEGORI TRANSAKSI ===");
        System.out.println();
        
        // 1. Menampilkan semua kategori
        System.out.println("Daftar Semua Kategori:");
        System.out.println("----------------------");
        for (KategoriTransaksi kategori : KategoriTransaksi.values()) {
            System.out.println("- " + kategori.getNama() + 
                             " (Pemasukan: " + kategori.isPemasukan() + ")");
        }
        System.out.println();
        
        // 2. Memisahkan pemasukan dan pengeluaran
        System.out.println("Kategori Pemasukan:");
        System.out.println("-------------------");
        for (KategoriTransaksi kategori : KategoriTransaksi.values()) {
            if (kategori.isPemasukan()) {
                System.out.println(kategori.getNama());
            }
        }
        System.out.println();
        
        System.out.println("Kategori Pengeluaran:");
        System.out.println("---------------------");
        for (KategoriTransaksi kategori : KategoriTransaksi.values()) {
            if (!kategori.isPemasukan()) {
                System.out.println(kategori.getNama());
            }
        }
        System.out.println();
        
        // 3. Menggunakan switch case untuk testing
        System.out.println("Testing dengan Switch Case:");
        System.out.println("--------------------------");
        
        KategoriTransaksi kategoriTest = KategoriTransaksi.GAJI;
        
        switch (kategoriTest) {
            case GAJI:
                System.out.println("Kategori: " + kategoriTest.getNama());
                System.out.println("Ini adalah pemasukan: " + kategoriTest.isPemasukan());
                break;
            case MAKANAN:
                System.out.println("Kategori: " + kategoriTest.getNama());
                System.out.println("Ini adalah pengeluaran: " + !kategoriTest.isPemasukan());
                break;
            default:
                System.out.println("Kategori lainnya");
        }
        System.out.println();
        
        // 4. Testing metode toString() (otomatis dari enum)
        System.out.println("Testing toString() method:");
        System.out.println("-------------------------");
        System.out.println("GAJI.toString() = " + KategoriTransaksi.GAJI.toString());
        System.out.println("MAKANAN.toString() = " + KategoriTransaksi.MAKANAN.toString());
        System.out.println();
        
        // 5. Testing dari interface Kategori
        System.out.println("Testing melalui Interface Kategori:");
        System.out.println("----------------------------------");
        Kategori kategoriInterface = KategoriTransaksi.BONUS;
        System.out.println("Nama kategori (dari interface): " + kategoriInterface.getNama());
        System.out.println("Apakah pemasukan? " + kategoriInterface.isPemasukan());
        System.out.println();
        
        // 6. Validasi bahwa semua method bekerja dengan baik
        System.out.println("Validasi Lengkap:");
        System.out.println("----------------");
        System.out.printf("%-25s | %-10s | %s%n", "NAMA KATEGORI", "TIPE", "NILAI ENUM");
        System.out.println("----------------------------------------------------------");
        
        for (KategoriTransaksi k : KategoriTransaksi.values()) {
            String tipe = k.isPemasukan() ? "PEMASUKAN" : "PENGELUARAN";
            System.out.printf("%-25s | %-10s | %s%n", 
                k.getNama(), 
                tipe, 
                k.name());
        }
        
        System.out.println("\n=== TEST BERHASIL ===");
    }
}
