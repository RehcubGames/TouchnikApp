package de.rehcub.touchnik;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

public class HapticView extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = "### HapticView";
    private int screenWidth, screenHeight;
    private float scaleX, scaleY;
    private Bitmap background;
    private boolean relief = false;

    String address = null, name = null;

    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    boolean finish = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        finish = false;
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //disable sleep mode
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SurfaceView view = new SurfaceView(this);
        setContentView(view);
        view.getHolder().addCallback(this);

        String backgroundName = getIntent().getStringExtra("background");
        if(backgroundName.equals("relief") || backgroundName.equals("relief_big"))
            relief = true;

        int resID = getResources().getIdentifier(getIntent().getStringExtra("background"), "drawable", getPackageName());
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        background = BitmapFactory.decodeResource(getResources(), resID, bitmapOptions);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        scaleX = (float) background.getWidth() / (float) screenWidth;
        scaleY = (float) background.getHeight() / (float) screenHeight;

        try {
            bluetooth_connect_device();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bluetooth_connect_device() throws IOException {
        try {
            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            address = myBluetooth.getAddress();
            pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice bt : pairedDevices) {
                    address = bt.getAddress();
                    name = bt.getName();
                    Toast.makeText(this.getApplicationContext(), "Connected to " + name, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception we) {
            Toast.makeText(this.getApplicationContext(), "Connection failed!", Toast.LENGTH_SHORT).show();
        }
        myBluetooth = BluetoothAdapter.getDefaultAdapter();                         //get the mobile bluetooth device
        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);         //connects to the device's address and checks if it's available
        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);   //create a RFCOMM (SPP) connection
        btSocket.connect();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        createCanvas(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int frmt, int w, int h) {
        createCanvas(holder);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN)
            return true;

        if (event.getAction() == MotionEvent.ACTION_UP
                && !finish)
        {
            if(Settings.FOUR_DIRECTIONS)
            {
                bluetoothSend(251);
            }else
                bluetoothSend(0);
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE
                && !finish)
        {
            if(relief)
            {
                int pixel = background.getPixel((int) (x * scaleX), (int) (y * scaleY));
                bluetoothSend((int) (Color.red(pixel)/25.5));
                return true;
            }

            if(x > 640 && y > 40 && y < 155)
            {
                bluetoothSend(251);
                finish = true;
                super.finish();
                return true;
            }

            if (Settings.SURROUND_DETECTION)
            {
                evaluateVibrationStrength(x, y);
            }else{
                int pixel = background.getPixel((int) (x * scaleX), (int) (y * scaleY));
                if (Color.red(pixel) > 200)
                    bluetoothSend(10);
                else
                    bluetoothSend(0);
            }

            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    private void createCanvas(SurfaceHolder holder) {
        Log.i(TAG, "creating Canvas...");

        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            Log.e(TAG, "Cannot draw onto the canvas as it's null");
        } else {
            draw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void draw(final Canvas canvas) {
        Log.i(TAG, "Drawing...");
        Rect src;
        Rect dest;
        src = new Rect(0, 0, background.getWidth(), background.getHeight());
        dest = new Rect(0, 0, screenWidth, screenHeight);
        canvas.drawBitmap(background, src, dest, null);

        //canvas.drawBitmap(background, 0, 0,null);
    }

    private void bluetoothSend(int i)
    {
        Log.i(TAG, "trying to send: " + i);
        try {
            if (btSocket != null)
                btSocket.getOutputStream().write(i);

        } catch (Exception e) {
            //Toast.makeText(this.context.getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bluetoothSend(byte[] data)
    {
        try {
            if (btSocket != null)
                btSocket.getOutputStream().write(data);

        } catch (Exception e) {
            //Toast.makeText(this.context.getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void evaluateVibrationStrength(int x, int y)
    {
        byte[] pixels = new byte[4];

        pixels[0] = (byte) checkPixelEnvironment(0, -1, x, y); //North
        pixels[1] = (byte) checkPixelEnvironment(0, 1, x, y);  //South
        pixels[2] = (byte) checkPixelEnvironment(-1, 0, x, y); //West
        pixels[3] = (byte) checkPixelEnvironment(1, 0, x, y);  //East


        if (Settings.FOUR_DIRECTIONS)
        {
            bluetoothSend(pixels);
        }else{
            int vibrationStrength = getStrongestVibration(pixels);
            bluetoothSend(vibrationStrength);
        }
    }

    /**
     * @param xDirection 1 = East; -1 = West
     * @param yDirection 1 = South; -1 = North
     * @param x          x-Koordinate
     * @param y          y-Koordinate
     * @return Vibrationsstärke in Abhängigkeit der Entfernung zum nächsten weißen Pixel;
     * max 200 Pixel Entfernung ansonsten Rückgabe von 0 -> Keine Vibration
     */
    private int checkPixelEnvironment(int xDirection, int yDirection, int x, int y) {
        int iteration = 200;
        for (int i = 0; i <= iteration; i++)
        {
            boolean yValid = background.getHeight() - 1 >= (y * scaleY) + i * yDirection && 0 <= (y * scaleY) + i * yDirection;
            boolean xValid = background.getWidth() - 1 >= (x * scaleX) + i * xDirection && 0 <= (x * scaleX) + i * xDirection;

            if ((xValid && yValid))
            {
                int pixel;
                pixel = background.getPixel((int) ((x * scaleX) + i * xDirection), (int) ((y * scaleY) + i * yDirection));
                if (Color.red(pixel) > 200)
                    return (iteration - i) / (iteration/10);
            }else{
                Log.i(TAG, "Pixel out of bounds");
                return 0;
            }
        }
        return 0;
    }

    /**
     * @param pixels An Array of all four directions
     * @return The VibrationStrength of the nearest Pixel
     */
    private int getStrongestVibration(byte[] pixels)
    {
        int strength = 0;
        for (byte pixel : pixels) {
            if (pixel > strength)
                strength = pixel;
        }
        return strength;
    }
}
