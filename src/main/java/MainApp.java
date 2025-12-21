import gui.AplikasiKeuanganGUI;
import javax.swing.SwingUtilities;

/**
 * MainApp
 * ------------------------------------
 * Entry point (titik awal) aplikasi keuangan pribadi.
 * Bertugas menjalankan GUI menggunakan Event Dispatch Thread (EDT).
 */
public class MainApp {

    /**
     * Method utama yang pertama kali dieksekusi oleh JVM
     * @param args argumen command line
     */
    public static void main(String[] args) {

        // Informasi awal aplikasi
        System.out.println("Aplikasi Keuangan Dimulai...");
        System.out.println("Version 1.0");

        // Menjalankan GUI di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    // Membuat dan menampilkan GUI utama
                    new AplikasiKeuanganGUI();
                    System.out.println("GUI berhasil diload.");

                } catch (Exception e) {
                    // Menangani error saat GUI gagal dijalankan
                    System.err.println("Error loading GUI: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}
