import model.KategoriTransaksi;
import model.Transaksi;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== UJI COBA KELAS TRANSAKSI & KATEGORI ===\n");
        
        // 1. Buat beberapa transaksi dengan kategori berbeda
        Transaksi transaksi1 = new Transaksi(
            "Gaji bulan Januari",
            5000000,
            KategoriTransaksi.GAJI,
            LocalDate.of(2024, 1, 28)
        );
        
        Transaksi transaksi2 = new Transaksi(
            "Pinjam dari teman",
            1000000,
            KategoriTransaksi.PINJAMAN,
            LocalDate.of(2024, 2, 5)
        );
        
        Transaksi transaksi3 = new Transaksi(
            "Bayar hutang",
            500000,
            KategoriTransaksi.PELUNASAN,
            LocalDate.of(2024, 2, 10)
        );
        
        Transaksi transaksi4 = new Transaksi(
            "Belanja bulanan",
            750000,
            KategoriTransaksi.LAINNYA_PENGELUARAN,
            LocalDate.of(2024, 2, 15)
        );
        
        // 2. Tampilkan semua transaksi
        System.out.println("=== DAFTAR TRANSAKSI ===");
        Transaksi[] semuaTransaksi = {transaksi1, transaksi2, transaksi3, transaksi4};
        for (Transaksi t : semuaTransaksi) {
            System.out.println(t.toString());
        }
        
        // 3. Tampilkan detail masing-masing transaksi
        System.out.println("\n=== DETAIL TRANSAKSI ===");
        for (int i = 0; i < semuaTransaksi.length; i++) {
            Transaksi t = semuaTransaksi[i];
            System.out.println("\nTransaksi " + (i + 1) + ":");
            System.out.println("  Deskripsi: " + t.getDeskripsi());
            System.out.println("  Jumlah: Rp " + String.format("%,.2f", t.getJumlah()));
            System.out.println("  Kategori: " + t.getKategori().getNama());
            System.out.println("  Pemasukan? " + (t.getKategori().isPemasukan() ? "Ya" : "Tidak"));
            System.out.println("  Tanggal: " + t.getFormattedTanggal());
            System.out.println("  Pinjaman? " + (t.isPinjaman() ? "Ya" : "Tidak"));
            System.out.println("  Pelunasan? " + (t.isPelunasan() ? "Ya" : "Tidak"));
        }
        
        // 4. Test selected attribute (untuk bulk action)
        System.out.println("\n=== TEST SELECTED ATTRIBUTE ===");
        transaksi1.setSelected(true);
        transaksi3.setSelected(true);
        
        System.out.println("Transaksi 1 selected: " + transaksi1.isSelected());
        System.out.println("Transaksi 2 selected: " + transaksi2.isSelected());
        System.out.println("Transaksi 3 selected: " + transaksi3.isSelected());
        System.out.println("Transaksi 4 selected: " + transaksi4.isSelected());
        
        // 5. Test semua kategori
        System.out.println("\n=== TEST SEMUA KATEGORI ===");
        for (KategoriTransaksi kategori : KategoriTransaksi.values()) {
            System.out.println("\nKategori: " + kategori.name());
            System.out.println("  Nama: " + kategori.getNama());
            System.out.println("  Pemasukan: " + kategori.isPemasukan());
            System.out.println("  Pinjaman: " + kategori.isPinjaman());
            System.out.println("  Pelunasan: " + kategori.isPelunasan());
        }
        
        // 6. Hitung total pemasukan dan pengeluaran
        System.out.println("\n=== RINGKASAN ===");
        double totalPemasukan = 0;
        double totalPengeluaran = 0;
        
        for (Transaksi t : semuaTransaksi) {
            if (t.getKategori().isPemasukan()) {
                totalPemasukan += t.getJumlah();
            } else {
                totalPengeluaran += t.getJumlah();
            }
        }
        
        System.out.println("Total Pemasukan: Rp " + String.format("%,.2f", totalPemasukan));
        System.out.println("Total Pengeluaran: Rp " + String.format("%,.2f", totalPengeluaran));
        System.out.println("Saldo: Rp " + String.format("%,.2f", (totalPemasukan - totalPengeluaran)));
        
        System.out.println("\n=== UJI COBA SELESAI ===");
    }
}