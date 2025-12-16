package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import exception.NegativeAmountException;
import model.*;

public class AplikasiKeuanganGUI {
    private JFrame frame;
    private ManagerKeuangan manager;
    
    // Komponen GUI
    private JTextField tfDeskripsi, tfJumlah, tfTanggal;
    private JComboBox<KategoriTransaksi> cbKategori;
    private JTextArea taLaporan;
    private JLabel lblSaldo, lblSisaHutang;
    
    // Komponen tabel
    private JTable tableTransaksi;
    private DefaultTableModel tableModel;
    private JButton btnHapusTerpilih, btnPilihSemua, btnBatalPilih;
    
    public AplikasiKeuanganGUI() {
        manager = new ManagerKeuangan();
        initialize();
    }
    
    private void initialize() {
        frame = new JFrame("Aplikasi Keuangan - Saldo Aktual");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout(10, 10));
        
        // Panel Utama dengan tab
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Transaksi", createTransaksiPanel());
        tabbedPane.addTab("Laporan", createLaporanPanel());
        
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(createBottomPanel(), BorderLayout.SOUTH);
        frame.setVisible(true);
        
        refreshTable();
        updateInfo();
    }
    
    private JPanel createTransaksiPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(createInputPanel(), BorderLayout.WEST);
        panel.add(createTablePanel(), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Judul
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblJudul = new JLabel("TAMBAH TRANSAKSI");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 14));
        lblJudul.setForeground(new Color(0, 100, 200));
        panel.add(lblJudul, gbc);
        
        // Deskripsi
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Deskripsi:"), gbc);
        
        gbc.gridx = 1;
        tfDeskripsi = new JTextField(20);
        panel.add(tfDeskripsi, gbc);
        
        // Jumlah
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Jumlah (Rp):"), gbc);
        
        gbc.gridx = 1;
        tfJumlah = new JTextField(20);
        panel.add(tfJumlah, gbc);
        
        // Tanggal
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Tanggal (dd-MM-yyyy):"), gbc);
        
        gbc.gridx = 1;
        tfTanggal = new JTextField(20);
        tfTanggal.setText(java.time.LocalDate.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        panel.add(tfTanggal, gbc);
        
        // Kategori
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Kategori:"), gbc);
        
        gbc.gridx = 1;
        cbKategori = new JComboBox<>(KategoriTransaksi.values());
        panel.add(cbKategori, gbc);
        
        // Tombol
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton btnTambah = new JButton("Tambah Transaksi");
        btnTambah.setBackground(new Color(0, 150, 0));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.addActionListener(e -> {
            try {
                tambahTransaksi();
            } catch (NegativeAmountException ex) {
                System.getLogger(AplikasiKeuanganGUI.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
        buttonPanel.add(btnTambah);
        
        JButton btnClear = new JButton("Clear Form");
        btnClear.addActionListener(e -> clearInput());
        buttonPanel.add(btnClear);
        
        panel.add(buttonPanel, gbc);
        
        // Panel info
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informasi Saldo"));
        
        JLabel lblInfoSaldo = new JLabel("Saldo: Rp 0,00");
        lblInfoSaldo.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblInfoSaldo);
        
        JLabel lblInfoHutang = new JLabel("Hutang: Rp 0,00");
        lblInfoHutang.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblInfoHutang);
        
        JLabel lblInfoSaldoBersih = new JLabel("Saldo Bersih: Rp 0,00");
        lblInfoSaldoBersih.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblInfoSaldoBersih);
        
        // Timer untuk update info saldo
        Timer timer = new Timer(2000, e -> {
            double saldo = manager.hitungSaldo();
            double hutang = manager.hitungTotalHutang();
            double saldoBersih = manager.hitungSaldoBersih();
            
            lblInfoSaldo.setText(String.format("Saldo: Rp %,.2f", saldo));
            lblInfoHutang.setText(String.format("Hutang: Rp %,.2f", hutang));
            lblInfoSaldoBersih.setText(String.format("Saldo Bersih: Rp %,.2f", saldoBersih));
            
            // Warning jika saldo rendah
            if (saldo < 100000) {
                lblInfoSaldo.setForeground(Color.RED);
            } else {
                lblInfoSaldo.setForeground(Color.BLACK);
            }
        });
        timer.start();
        
        panel.add(infoPanel, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Judul
        JLabel lblJudul = new JLabel("DAFTAR TRANSAKSI");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 14));
        lblJudul.setForeground(new Color(0, 100, 200));
        panel.add(lblJudul, BorderLayout.NORTH);
        
        // Tabel
        String[] columnNames = {"Pilih", "No", "Tanggal", "Tipe", "Kategori", "Jumlah", "Deskripsi"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        
        tableTransaksi = new JTable(tableModel);
        tableTransaksi.setRowHeight(25);
        tableTransaksi.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableTransaksi.getColumnModel().getColumn(1).setPreferredWidth(40);
        tableTransaksi.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableTransaksi.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableTransaksi.getColumnModel().getColumn(4).setPreferredWidth(120);
        tableTransaksi.getColumnModel().getColumn(5).setPreferredWidth(120);
        tableTransaksi.getColumnModel().getColumn(6).setPreferredWidth(250);
        
        // Custom renderer untuk warna
        tableTransaksi.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    String kategori = (String) table.getValueAt(row, 4);
                    String tipe = (String) table.getValueAt(row, 3);
                    
                    // Warna berdasarkan tipe
                    if (tipe.equals("PEMASUKAN")) {
                        c.setBackground(new Color(230, 255, 230)); // Hijau muda
                    } else {
                        c.setBackground(new Color(255, 230, 230)); // Merah muda
                    }
                    
                    // Warna khusus untuk hutang
                    if (kategori.equals("Pinjaman")) {
                        c.setBackground(new Color(255, 255, 200)); // Kuning muda
                    } else if (kategori.equals("Pelunasan Hutang")) {
                        c.setBackground(new Color(200, 230, 255)); // Biru muda
                    }
                    
                    // Warna teks untuk jumlah
                    if (column == 5) {
                        String jumlahStr = (String) value;
                        if (tipe.equals("PENGELUARAN")) {
                            c.setForeground(Color.RED);
                        } else {
                            c.setForeground(new Color(0, 128, 0));
                        }
                    }
                }
                
                return c;
            }
        });
        
        // Listener untuk checkbox
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 0) {
                Boolean selected = (Boolean) tableModel.getValueAt(row, column);
                List<Transaksi> transaksiList = manager.getAllTransaksi();
                if (row < transaksiList.size()) {
                    transaksiList.get(row).setSelected(selected);
                }
                btnHapusTerpilih.setEnabled(manager.adaTransaksiTerpilih());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableTransaksi);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel bulk action
        JPanel bulkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        btnPilihSemua = new JButton("Pilih Semua");
        btnPilihSemua.addActionListener(e -> {
            manager.pilihSemuaTransaksi(true);
            refreshTable();
        });
        bulkPanel.add(btnPilihSemua);
        
        btnBatalPilih = new JButton("Batal Pilih");
        btnBatalPilih.addActionListener(e -> {
            manager.pilihSemuaTransaksi(false);
            refreshTable();
        });
        bulkPanel.add(btnBatalPilih);
        
        btnHapusTerpilih = new JButton("Hapus Terpilih");
        btnHapusTerpilih.setBackground(new Color(220, 0, 0));
        btnHapusTerpilih.setForeground(Color.WHITE);
        btnHapusTerpilih.setEnabled(false);
        btnHapusTerpilih.addActionListener(e -> hapusTransaksiTerpilih());
        bulkPanel.add(btnHapusTerpilih);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> {
            refreshTable();
            updateInfo();
        });
        bulkPanel.add(btnRefresh);
        
        panel.add(bulkPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createLaporanPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        taLaporan = new JTextArea(20, 50);
        taLaporan.setEditable(false);
        taLaporan.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(taLaporan);
        
        JPanel buttonPanel = new JPanel();
        
        JButton btnLaporanLengkap = new JButton("Laporan Lengkap");
        btnLaporanLengkap.addActionListener(e -> generateLaporanLengkap());
        buttonPanel.add(btnLaporanLengkap);
        
        JButton btnLaporanPemasukan = new JButton("Laporan Pemasukan");
        btnLaporanPemasukan.addActionListener(e -> generateLaporanPemasukan());
        buttonPanel.add(btnLaporanPemasukan);
        
        JButton btnLaporanPengeluaran = new JButton("Laporan Pengeluaran");
        btnLaporanPengeluaran.addActionListener(e -> generateLaporanPengeluaran());
        buttonPanel.add(btnLaporanPengeluaran);
        
        JButton btnLaporanHutang = new JButton("Laporan Hutang");
        btnLaporanHutang.addActionListener(e -> generateLaporanHutang());
        buttonPanel.add(btnLaporanHutang);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Saldo Panel
        JPanel saldoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        saldoPanel.add(new JLabel("Saldo Aktual: "));
        lblSaldo = new JLabel("Rp 0,00");
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 18));
        saldoPanel.add(lblSaldo);
        panel.add(saldoPanel);
        
        // Sisa Hutang Panel
        JPanel hutangPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        hutangPanel.add(new JLabel("Sisa Hutang: "));
        lblSisaHutang = new JLabel("Rp 0,00");
        lblSisaHutang.setFont(new Font("Arial", Font.BOLD, 18));
        hutangPanel.add(lblSisaHutang);
        panel.add(hutangPanel);
        
        // Timer untuk update
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateInfo();
            }
        });
        timer.start();
        
        return panel;
    }
    
    private void tambahTransaksi() throws NegativeAmountException {
        try {
            String deskripsi = tfDeskripsi.getText().trim();
            String jumlahStr = tfJumlah.getText().trim();
            String tanggal = tfTanggal.getText().trim();
            KategoriTransaksi kategori = (KategoriTransaksi) cbKategori.getSelectedItem();
            
            if (deskripsi.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Deskripsi tidak boleh kosong!");
                return;
            }
            
            double jumlah = Double.parseDouble(jumlahStr);
            
            manager.tambahTransaksi(deskripsi, jumlah, kategori, tanggal);
            
            JOptionPane.showMessageDialog(frame, "Transaksi berhasil ditambahkan!");
            clearInput();
            refreshTable();
            updateInfo();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, 
                "Format jumlah salah! Masukkan angka yang valid.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NegativeAmountException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(frame, 
                e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearInput() {
        tfDeskripsi.setText("");
        tfJumlah.setText("");
        tfTanggal.setText(java.time.LocalDate.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        cbKategori.setSelectedIndex(0);
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Transaksi> transaksiList = manager.getAllTransaksi();
        
        for (int i = 0; i < transaksiList.size(); i++) {
            Transaksi t = transaksiList.get(i);
            String tipe = t.getKategori().isPemasukan() ? "PEMASUKAN" : "PENGELUARAN";
            
            tableModel.addRow(new Object[]{
                t.isSelected(),
                i + 1,
                t.getFormattedTanggal(),
                tipe,
                t.getKategori().getNama(),
                String.format("Rp %,.2f", t.getJumlah()),
                t.getDeskripsi()
            });
        }
        
        btnHapusTerpilih.setEnabled(manager.adaTransaksiTerpilih());
    }
    
    private void hapusTransaksiTerpilih() {
        if (!manager.adaTransaksiTerpilih()) {
            JOptionPane.showMessageDialog(frame, "Tidak ada transaksi yang dipilih!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Apakah Anda yakin ingin menghapus transaksi yang dipilih?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            manager.hapusTransaksiTerpilih();
            refreshTable();
            updateInfo();
            JOptionPane.showMessageDialog(frame, "Transaksi berhasil dihapus!");
        }
    }
    
    private void updateInfo() {
        double saldo = manager.hitungSaldo();
        double sisaHutang = manager.hitungSisaHutang();
        
        lblSaldo.setText(String.format("Rp %,.2f", saldo));
        lblSisaHutang.setText(String.format("Rp %,.2f", sisaHutang));
        
        // Warna berdasarkan nilai
        if (saldo < 0) {
            lblSaldo.setForeground(Color.RED);
        } else if (saldo < 100000) {
            lblSaldo.setForeground(Color.ORANGE);
        } else {
            lblSaldo.setForeground(new Color(0, 128, 0));
        }
        
        if (sisaHutang > 0) {
            lblSisaHutang.setForeground(Color.RED);
        } else {
            lblSisaHutang.setForeground(Color.BLACK);
        }
    }
    
    private void generateLaporanLengkap() {
        StringBuilder sb = new StringBuilder();
        String[] ringkasan = manager.generateLaporanRingkasan();
        
        sb.append("LAPORAN KEUANGAN LENGKAP\n");
        sb.append("=".repeat(60) + "\n\n");
        
        for (String line : ringkasan) {
            sb.append(line).append("\n");
        }
        
        sb.append("\n").append("=".repeat(60)).append("\n");
        
        taLaporan.setText(sb.toString());
    }
    
    private void generateLaporanPemasukan() {
        StringBuilder sb = new StringBuilder();
        List<Transaksi> pemasukanList = manager.getTransaksiPemasukan();
        
        sb.append("LAPORAN PEMASUKAN\n");
        sb.append("=".repeat(50) + "\n\n");
        
        double total = 0;
        for (Transaksi t : pemasukanList) {
            sb.append(String.format("• %s\n", t.toString()));
            total += t.getJumlah();
        }
        
        sb.append("\n").append("-".repeat(40)).append("\n");
        sb.append(String.format("TOTAL PEMASUKAN: Rp %,.2f\n", total));
        sb.append(String.format("Jumlah Transaksi: %d\n", pemasukanList.size()));
        
        if (!pemasukanList.isEmpty()) {
            sb.append(String.format("Rata-rata: Rp %,.2f\n", total / pemasukanList.size()));
        }
        
        taLaporan.setText(sb.toString());
    }
    
    private void generateLaporanPengeluaran() {
        StringBuilder sb = new StringBuilder();
        List<Transaksi> pengeluaranList = manager.getTransaksiPengeluaran();
        
        sb.append("LAPORAN PENGELUARAN\n");
        sb.append("=".repeat(50) + "\n\n");
        
        double total = 0;
        for (Transaksi t : pengeluaranList) {
            sb.append(String.format("• %s\n", t.toString()));
            total += t.getJumlah();
        }
        
        sb.append("\n").append("-".repeat(40)).append("\n");
        sb.append(String.format("TOTAL PENGELUARAN: Rp %,.2f\n", total));
        sb.append(String.format("Jumlah Transaksi: %d\n", pengeluaranList.size()));
        
        if (!pengeluaranList.isEmpty()) {
            sb.append(String.format("Rata-rata: Rp %,.2f\n", total / pengeluaranList.size()));
        }
        
        taLaporan.setText(sb.toString());
    }
    
    private void generateLaporanHutang() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("LAPORAN HUTANG\n");
        sb.append("=".repeat(50) + "\n\n");
        
        double totalPinjaman = manager.hitungTotalPinjaman();
        double totalPelunasan = manager.hitungTotalPelunasan();
        double sisaHutang = manager.hitungSisaHutang();
        
        sb.append("Daftar Pinjaman:\n");
        List<Transaksi> semuaTransaksi = manager.getAllTransaksi();
        boolean adaPinjaman = false;
        for (Transaksi t : semuaTransaksi) {
            if (t.isPinjaman()) {
                sb.append(String.format("  [+] %s\n", t.toString()));
                adaPinjaman = true;
            }
        }
        if (!adaPinjaman) {
            sb.append("  - Tidak ada pinjaman\n");
        }
        
        sb.append("\nDaftar Pelunasan:\n");
        boolean adaPelunasan = false;
        for (Transaksi t : semuaTransaksi) {
            if (t.isPelunasan()) {
                sb.append(String.format("  [-] %s\n", t.toString()));
                adaPelunasan = true;
            }
        }
        if (!adaPelunasan) {
            sb.append("  - Tidak ada pelunasan\n");
        }
        
        sb.append("\n").append("=".repeat(40)).append("\n");
        sb.append(String.format("Total Pinjaman: Rp %,.2f\n", totalPinjaman));
        sb.append(String.format("Total Pelunasan: Rp %,.2f\n", totalPelunasan));
        sb.append(String.format("Sisa Hutang: Rp %,.2f\n", sisaHutang));
        
        if (sisaHutang > 0) {
            sb.append("\n⚠ PERINGATAN: Masih ada hutang yang belum dilunasi!\n");
            sb.append(String.format("Anda perlu melunasi: Rp %,.2f\n", sisaHutang));
        } else if (totalPinjaman > 0) {
            sb.append("\n✓ Semua hutang sudah dilunasi!\n");
        }
        
        taLaporan.setText(sb.toString());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AplikasiKeuanganGUI());
    }
}