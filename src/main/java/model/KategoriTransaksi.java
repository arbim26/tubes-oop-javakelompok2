package model;

public enum KategoriTransaksi {
    // Pemasukan (termasuk hutang masuk)
    GAJI("Pemasukan", true),
    PINJAMAN("Pinjaman", true),
    
    // Pengeluaran (termasuk pelunasan)
    PELUNASAN("Pelunasan Hutang", false),
    LAINNYA_PENGELUARAN("Pengeluaran", false);
    
    private final String nama;
    private final boolean pemasukan;
    
    KategoriTransaksi(String nama, boolean pemasukan) {
        this.nama = nama;
        this.pemasukan = pemasukan;
    }
    
    public String getNama() {
        return nama;
    }
    
    public boolean isPemasukan() {
        return pemasukan;
    }
    
    // Helper method untuk cek tipe khusus
    public boolean isPinjaman() {
        return this == PINJAMAN;
    }
    
    public boolean isPelunasan() {
        return this == PELUNASAN;
    }
    
    @Override
    public String toString() {
        return nama;
    }
}