package model;

/**
 * Enum untuk mendefinisikan kategori transaksi keuangan.
 * Digunakan untuk menentukan jenis transaksi dan dampaknya
 * terhadap saldo dan hutang.
 */
public enum KategoriTransaksi {

    /** Transaksi pemasukan */
    PEMASUKAN("Pemasukan", true, false, false),

    /** Transaksi pinjaman */
    PINJAMAN("Pinjaman", true, true, false),

    /** Transaksi pengeluaran */
    PENGELUARAN("Pengeluaran", false, false, false),

    /** Transaksi pelunasan hutang */
    PELUNASAN("Pelunasan Hutang", false, false, true);

    private final String nama;
    private final boolean pemasukan;
    private final boolean pinjaman;
    private final boolean pelunasan;

    /**
     * Konstruktor kategori transaksi.
     */
    KategoriTransaksi(String nama, boolean pemasukan, boolean pinjaman, boolean pelunasan) {
        this.nama = nama;
        this.pemasukan = pemasukan;
        this.pinjaman = pinjaman;
        this.pelunasan = pelunasan;
    }

    /** @return nama kategori */
    public String getNama() {
        return nama;
    }

    /** @return true jika menambah saldo */
    public boolean isPemasukan() {
        return pemasukan;
    }

    /** @return true jika transaksi pinjaman */
    public boolean isPinjaman() {
        return pinjaman;
    }

    /** @return true jika pelunasan hutang */
    public boolean isPelunasan() {
        return pelunasan;
    }
}
