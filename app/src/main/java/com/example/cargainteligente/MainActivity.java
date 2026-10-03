package com.example.cargainteligente;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    private final AtomicBoolean cargaInicialTerminada = new AtomicBoolean(false);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private View panelContenido;
    private View panelCarga;
    private TextView txtEstado;
    private Button btnActualizar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        splashScreen.setKeepOnScreenCondition(() -> !cargaInicialTerminada.get());
        setContentView(R.layout.activity_main);

        panelContenido = findViewById(R.id.panelContenido);
        panelCarga     = findViewById(R.id.panelCarga);
        txtEstado      = findViewById(R.id.txtEstado);
        btnActualizar  = findViewById(R.id.btnActualizar);

        btnActualizar.setOnClickListener(v -> actualizarDatos());

        prepararDatosIniciales();

    }

    private void prepararDatosIniciales() {
        executor.execute(() -> {
            if(!esperarSoloParaDemostracion(900)) {
                return;
            }
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                txtEstado.setText(R.string.mensaje_inicial);
                cargaInicialTerminada.set(true);
            });
        });
    }

    private void actualizarDatos() {
        panelCarga.setVisibility(View.VISIBLE);
        panelContenido.setVisibility(View.INVISIBLE);

        executor.execute(() -> {
            if (!esperarSoloParaDemostracion(1800)) {
                return;
            }

            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                txtEstado.setText(R.string.estado_actualizado);
                panelCarga.setVisibility(View.GONE);
                panelContenido.setVisibility(View.VISIBLE);
            });
        });
    }

    private boolean esperarSoloParaDemostracion(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override

    protected void onDestroy() {
        executor.shutdownNow();
        super.onDestroy();
    }


}