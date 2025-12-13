package com.mycompany.kalkulator.keuangan;

public enum KategoriTransaksi implements Kategori {
    // Pemasukan
    GAJI("Gaji", true),
    BONUS("Bonus", true),
    INVESTASI("Investasi", true),
    LAINNYA_PEMASUKAN("Lainnya (Pemasukan)", true),
    
    // Pengeluaran
    MAKANAN("Makanan", false),
    TRANSPORTASI("Transportasi", false),
    HIBURAN("Hiburan", false),
    BELANJA("Belanja", false),
    TAGIHAN("Tagihan", false),
    LAINNYA_PENGELUARAN("Lainnya (Pengeluaran)", false);
    
    private final String nama;
    private final boolean pemasukan;
    
    KategoriTransaksi(String nama, boolean pemasukan) {
        this.nama = nama;
        this.pemasukan = pemasukan;
    }
    
    @Override
    public String getNama() {
        return nama;
    }
    
    @Override
    public boolean isPemasukan() {
        return pemasukan;
    }
    
    @Override
    public String toString() {
        return nama;
    }
}
