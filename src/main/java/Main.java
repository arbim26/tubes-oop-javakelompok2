import model.ManagerKeuangan;
import model.KategoriTransaksi;
import model.Transaksi;
import exception.NegativeAmountException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== TEST PROGRAM MANAGER KEUANGAN ===\n");
        
        // Inisialisasi ManagerKeuangan
        ManagerKeuangan manager = new ManagerKeuangan();
        
        try {
            // Test 1: Tambah transaksi normal
            System.out.println("1. MENAMBAH TRANSAKSI:");
            System.out.println("----------------------");
            
            manager.tambahTransaksi("Gaji Januari", 5000000, KategoriTransaksi.GAJI, "28-01-2024");
            System.out.println("✓ Transaksi 1: Gaji ditambahkan");
            
            manager.tambahTransaksi("Pinjam dari Bank", 2000000, KategoriTransaksi.PINJAMAN, "05-02-2024");
            System.out.println("✓ Transaksi 2: Pinjaman ditambahkan");
            
            manager.tambahTransaksi("Bayar cicilan Bank", 500000, KategoriTransaksi.PELUNASAN, "10-02-2024");
            System.out.println("✓ Transaksi 3: Pelunasan ditambahkan");
            
            manager.tambahTransaksi("Belanja bulanan", 750000, KategoriTransaksi.LAINNYA_PENGELUARAN, "15-02-2024");
            System.out.println("✓ Transaksi 4: Pengeluaran ditambahkan\n");
            
            // Test 2: Tambah transaksi yang akan gagal (validasi)
            System.out.println("2. VALIDASI TRANSAKSI:");
            System.out.println("----------------------");
            
            try {
                manager.tambahTransaksi("Test negatif", -100000, KategoriTransaksi.GAJI, "01-01-2024");
            } catch (NegativeAmountException e) {
                System.out.println("✓ Validasi 1: Jumlah negatif ditolak - " + e.getMessage());
            }
            
            try {
                manager.tambahTransaksi("Test nol", 0, KategoriTransaksi.GAJI, "01-01-2024");
            } catch (NegativeAmountException e) {
                System.out.println("✓ Validasi 2: Jumlah nol ditolak - " + e.getMessage());
            }
            
            try {
                manager.tambahTransaksi("Test format tanggal salah", 100000, KategoriTransaksi.GAJI, "2024-01-01");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Validasi 3: Format tanggal salah - " + e.getMessage());
            }
            
            // Tambah transaksi lagi untuk test saldo
            manager.tambahTransaksi("Bonus", 1000000, KategoriTransaksi.GAJI, "20-02-2024");
            System.out.println("✓ Transaksi 5: Bonus ditambahkan\n");
            
            // Test 3: Tampilkan semua transaksi
            System.out.println("3. DAFTAR SEMUA TRANSAKSI:");
            System.out.println("--------------------------");
            List<Transaksi> semuaTransaksi = manager.getAllTransaksi();
            if (semuaTransaksi.isEmpty()) {
                System.out.println("Tidak ada transaksi");
            } else {
                for (Transaksi t : semuaTransaksi) {
                    System.out.println(t.toString());
                }
            }
            System.out.println();
            
            // Test 4: Hitung berbagai total
            System.out.println("4. PERHITUNGAN KEUANGAN:");
            System.out.println("------------------------");
            System.out.printf("Total Pemasukan: Rp %,.2f%n", manager.hitungTotalPemasukan());
            System.out.printf("Total Pengeluaran: Rp %,.2f%n", manager.hitungTotalPengeluaran());
            System.out.printf("Saldo Aktual: Rp %,.2f%n", manager.hitungSaldo());
            System.out.printf("Total Pinjaman: Rp %,.2f%n", manager.hitungTotalPinjaman());
            System.out.printf("Total Pelunasan: Rp %,.2f%n", manager.hitungTotalPelunasan());
            System.out.printf("Sisa Hutang: Rp %,.2f%n", manager.hitungSisaHutang());
            System.out.printf("Saldo Bersih: Rp %,.2f%n", manager.hitungSaldoBersih());
            System.out.println();
            
            // Test 5: Generate laporan
            System.out.println("5. LAPORAN RINGKASAN:");
            System.out.println("---------------------");
            String[] laporan = manager.generateLaporanRingkasan();
            for (String line : laporan) {
                System.out.println(line);
            }
            System.out.println();
            
            // Test 6: Filter transaksi
            System.out.println("6. FILTER TRANSAKSI:");
            System.out.println("--------------------");
            
            // Filter pemasukan
            List<Transaksi> pemasukan = manager.getTransaksiPemasukan();
            System.out.println("Transaksi Pemasukan (" + pemasukan.size() + " item):");
            for (Transaksi t : pemasukan) {
                System.out.println("  - " + t.getDeskripsi() + ": Rp " + String.format("%,.2f", t.getJumlah()));
            }
            
            // Filter pengeluaran
            List<Transaksi> pengeluaran = manager.getTransaksiPengeluaran();
            System.out.println("\nTransaksi Pengeluaran (" + pengeluaran.size() + " item):");
            for (Transaksi t : pengeluaran) {
                System.out.println("  - " + t.getDeskripsi() + ": Rp " + String.format("%,.2f", t.getJumlah()));
            }
            
            // Filter by kategori
            List<Transaksi> pinjamanList = manager.getTransaksiByKategori(KategoriTransaksi.PINJAMAN);
            System.out.println("\nTransaksi Pinjaman (" + pinjamanList.size() + " item):");
            for (Transaksi t : pinjamanList) {
                System.out.println("  - " + t.getDeskripsi() + ": Rp " + String.format("%,.2f", t.getJumlah()));
            }
            System.out.println();
            
            // Test 7: Bulk action (select & delete)
            System.out.println("7. BULK ACTION (SELECT & DELETE):");
            System.out.println("----------------------------------");
            
            // Tampilkan sebelum seleksi
            System.out.println("Sebelum seleksi - Jumlah transaksi: " + manager.getAllTransaksi().size());
            
            // Select semua
            manager.pilihSemuaTransaksi(true);
            System.out.println("Setelah select semua - Ada transaksi terpilih: " + manager.adaTransaksiTerpilih());
            
            // Hapus hanya 1 transaksi (pilih ulang)
            manager.pilihSemuaTransaksi(false); // Reset
            List<Transaksi> semua = manager.getAllTransaksi();
            if (!semua.isEmpty()) {
                semua.get(0).setSelected(true); // Pilih transaksi pertama
                System.out.println("Pilih 1 transaksi untuk dihapus");
                
                System.out.println("Sebelum hapus - Jumlah transaksi: " + manager.getAllTransaksi().size());
                manager.hapusTransaksiTerpilih();
                System.out.println("Setelah hapus - Jumlah transaksi: " + manager.getAllTransaksi().size());
            }
            System.out.println();
            
            // Test 8: Tambah transaksi yang akan menyebabkan saldo negatif
            System.out.println("8. TEST VALIDASI SALDO:");
            System.out.println("-----------------------");
            System.out.println("Saldo saat ini: Rp " + String.format("%,.2f", manager.hitungSaldo()));
            
            try {
                // Coba tambah pengeluaran besar yang melebihi saldo
                manager.tambahTransaksi("Pembelian barang mewah", 10000000, 
                                      KategoriTransaksi.LAINNYA_PENGELUARAN, "25-02-2024");
            } catch (NegativeAmountException e) {
                System.out.println("✓ Validasi saldo berhasil - " + e.getMessage());
            }
            System.out.println();
            
            // Test 9: Pelunasan melebihi hutang
            System.out.println("9. TEST VALIDASI PELUNASAN:");
            System.out.println("---------------------------");
            System.out.println("Sisa hutang saat ini: Rp " + String.format("%,.2f", manager.hitungSisaHutang()));
            
            try {
                // Coba bayar pelunasan lebih dari sisa hutang
                manager.tambahTransaksi("Bayar hutang berlebih", 3000000, 
                                      KategoriTransaksi.PELUNASAN, "28-02-2024");
            } catch (NegativeAmountException e) {
                System.out.println("✓ Validasi pelunasan berhasil - " + e.getMessage());
            }
            System.out.println();
            
            // Test 10: Final laporan
            System.out.println("10. LAPORAN FINAL:");
            System.out.println("------------------");
            String[] laporanFinal = manager.generateLaporanRingkasan();
            for (String line : laporanFinal) {
                System.out.println(line);
            }
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== TEST SELESAI ===");
    }
}