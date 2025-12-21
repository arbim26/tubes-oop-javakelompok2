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

    // Tambah transaksi baru
    public void tambahTransaksi(
            String deskripsi,
            double jumlah,
            KategoriTransaksi kategori,
            String tanggalStr
    ) throws NegativeAmountException, IllegalArgumentException {

        // Validasi jumlah
        if (jumlah <= 0) {
            throw new NegativeAmountException("Jumlah transaksi harus lebih dari 0");
        }

        // Validasi saldo untuk pengeluaran & pelunasan
        if (!kategori.isPemasukan() && kategori != KategoriTransaksi.PELUNASAN) {
            double saldoSekarang = hitungSaldo();

            // Validasi khusus pelunasan hutang
            if (kategori == KategoriTransaksi.PELUNASAN) {
                double sisaHutang = hitungSisaHutang();
                if (jumlah > sisaHutang) {
                    throw new NegativeAmountException(
                        String.format(
                            "Jumlah pelunasan (Rp %,.2f) melebihi sisa hutang (Rp %,.2f)",
                            jumlah, sisaHutang
                        )
                    );
                }
            }

            // Cek saldo untuk semua pengeluaran
            if (saldoSekarang - jumlah < 0) {
                throw new NegativeAmountException(
                    String.format("Saldo tidak cukup! Saldo saat ini: Rp %,.2f", saldoSekarang)
                );
            }
        }

        // Parsing tanggal (dd-MM-yyyy)
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

    // Hapus transaksi yang dipilih
    public void hapusTransaksiTerpilih() {
        Iterator<Transaksi> iterator = daftarTransaksi.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isSelected()) {
                iterator.remove();
            }
        }
    }

    // Pilih / batalkan pilih semua transaksi
    public void pilihSemuaTransaksi(boolean selected) {
        for (Transaksi transaksi : daftarTransaksi) {
            transaksi.setSelected(selected);
        }
    }

    // Total pemasukan (termasuk pinjaman)
    public double hitungTotalPemasukan() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) {
            if (t.getKategori().isPemasukan()) {
                total += t.getJumlah();
            }
        }
        return total;
    }

    // Total pengeluaran (termasuk pelunasan)
    public double hitungTotalPengeluaran() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) {
            if (!t.getKategori().isPemasukan()) {
                total += t.getJumlah();
            }
        }
        return total;
    }

    // Saldo aktual
    public double hitungSaldo() {
        double pemasukan = hitungTotalPemasukan();
        double pengeluaran = hitungTotalPengeluaran();
        return pemasukan - pengeluaran;
    }

    // Total pinjaman
    public double hitungTotalPinjaman() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) {
            if (t.isPinjaman()) {
                total += t.getJumlah();
            }
        }
        return total;
    }

    // Total pelunasan
    public double hitungTotalPelunasan() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) {
            if (t.isPelunasan()) {
                total += t.getJumlah();
            }
        }
        return total;
    }

    // Sisa hutang
    public double hitungSisaHutang() {
        return Math.max(0, hitungTotalPinjaman() - hitungTotalPelunasan());
    }

    // Laporan ringkasan
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

        laporan[8] = saldo < 0
                ? "⚠ SALDO NEGATIF! Perlu tambahan pemasukan."
                : saldo < 100_000
                ? "⚠ Saldo rendah! Hati-hati dalam pengeluaran."
                : "✓ Saldo sehat.";

        laporan[9] = sisaHutang > 0
                ? "⚠ PERINGATAN: Masih ada hutang yang belum dilunasi!"
                : pinjaman > 0
                ? "✓ Semua hutang sudah dilunasi!"
                : "✓ Tidak ada hutang.";

        return laporan;
    }

    // Getter data transaksi
    public List<Transaksi> getAllTransaksi() {
        return new ArrayList<>(daftarTransaksi);
    }

    public List<Transaksi> getTransaksiByKategori(KategoriTransaksi kategori) {
        List<Transaksi> result = new ArrayList<>();
        for (Transaksi t : daftarTransaksi) {
            if (t.getKategori() == kategori) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Transaksi> getTransaksiPemasukan() {
        List<Transaksi> result = new ArrayList<>();
        for (Transaksi t : daftarTransaksi) {
            if (t.getKategori().isPemasukan()) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Transaksi> getTransaksiPengeluaran() {
        List<Transaksi> result = new ArrayList<>();
        for (Transaksi t : daftarTransaksi) {
            if (!t.getKategori().isPemasukan()) {
                result.add(t);
            }
        }
        return result;
    }

    public boolean adaTransaksiTerpilih() {
        for (Transaksi t : daftarTransaksi) {
            if (t.isSelected()) {
                return true;
            }
        }
        return false;
    }

    // Tambahan method
    public double hitungTotalHutang() {
        return hitungTotalPinjaman() - hitungTotalPelunasan();
    }
    

    public double hitungSaldoBersih() {
        return hitungSaldo() - hitungTotalHutang();
    }
    
}
