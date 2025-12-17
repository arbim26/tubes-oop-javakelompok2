import gui.AplikasiKeuanganGUI;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Aplikasi Keuangan Dimulai...");
        System.out.println("Version 1.0");
        
        // Jalankan GUI dalam EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new AplikasiKeuanganGUI();
                    System.out.println("GUI berhasil diload.");
                } catch (Exception e) {
                    System.err.println("Error loading GUI: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}