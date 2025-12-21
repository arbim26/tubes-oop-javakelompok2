package model;

/**
 * Interface Kategori merepresentasikan jenis kategori dalam suatu transaksi keuangan.
 * 
 * Kategori dapat berupa pemasukan, pengeluaran, atau tipe transaksi khusus lainnya
 * (misalnya pelunasan, pinjaman, dll).
 * 
 * Interface ini digunakan untuk memastikan setiap kategori memiliki
 * identitas nama, tipe transaksi, serta penanda apakah kategori tersebut
 * termasuk pemasukan atau bukan.
 */
public interface Kategori {

    /**
     * Mengembalikan nama kategori transaksi.
     * 
     * Contoh:
     * - "Gaji"
     * - "Makan"
     * - "Pelunasan Hutang"
     *
     * @return nama kategori dalam bentuk String
     */
    String getNama();

    /**
     * Menentukan apakah kategori ini termasuk transaksi pemasukan.
     * 
     * @return true jika kategori merupakan pemasukan,
     *         false jika kategori merupakan pengeluaran atau transaksi lainnya
     */
    boolean isPemasukan();

    /**
     * Mengembalikan tipe kategori transaksi.
     * 
     * Tipe dapat digunakan untuk klasifikasi tambahan, misalnya:
     * - "PEMASUKAN"
     * - "PENGELUARAN"
     * - "PELUNASAN"
     *
     * @return tipe kategori dalam bentuk String
     */
    String getTipe();
}
