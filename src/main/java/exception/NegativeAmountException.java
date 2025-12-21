package exception;

/**
 * NegativeAmountException
 * ------------------------------------
 * Exception khusus (custom exception)
 * Digunakan untuk menangani input nominal negatif atau tidak valid
 * pada aplikasi keuangan.
 */
public class NegativeAmountException extends Exception {

    /**
     * Constructor default
     * Menampilkan pesan error bawaan
     */
    public NegativeAmountException() {
        super("Jumlah tidak boleh bernilai negatif atau nol.");
    }

    /**
     * Constructor dengan pesan custom
     * @param message pesan error yang ditentukan programmer
     */
    public NegativeAmountException(String message) {
        super(message);
    }
}
