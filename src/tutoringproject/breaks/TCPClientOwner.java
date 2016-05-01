package tutoringproject.breaks;

import android.os.Looper;

/**
 * Created by arsalan on 4/21/16.
 */
public interface TCPClientOwner {
    Looper getMainLooper();
    void disableButtons();
    void messageReceived(String message);
}
