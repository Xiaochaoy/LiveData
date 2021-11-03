package com.example.livedata;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

import androidx.lifecycle.LiveData;

public class Entrenador {

    interface EntrenadorListener {
        void cuandoDeLaOrden(String orden);
    }

    Random random = new Random();
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> entrenando;

    void iniciarEntrenamiento(EntrenadorListener entrenadorListener) {
        if (entrenando == null || entrenando.isCancelled()) {
            entrenando = scheduler.scheduleAtFixedRate(new Runnable() {
                int ejercicio = 0;
                int repeticiones = -1;
                int e = 1;

                @Override
                public void run() {
                    if (repeticiones < 0) {
                        repeticiones = 5;
                        ejercicio += e;
                        e = random.nextInt(3) + 1;

                    }
                    if (ejercicio <= 12 && ejercicio > 0){
                        entrenadorListener.cuandoDeLaOrden("EJERCICIO" + ejercicio + ":" + (repeticiones == 0 ? "CAMBIO" + e : repeticiones));
                    }else if (ejercicio > 12){
                        ejercicio = ejercicio-12;
                        entrenadorListener.cuandoDeLaOrden("EJERCICIO" + ejercicio + ":" + (repeticiones == 0 ? "CAMBIO" + e : repeticiones));
                    }
                    repeticiones--;

                }
            }, 0, 1, SECONDS);
        }
    }

    void pararEntrenamiento() {
        if (entrenando != null) {
            entrenando.cancel(true);
        }
    }
    LiveData<String> ordenLiveData = new LiveData<String>() {
        @Override
        protected void onActive() {
            super.onActive();

            iniciarEntrenamiento(new EntrenadorListener() {
                @Override
                public void cuandoDeLaOrden(String orden) {
                    postValue(orden);
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            pararEntrenamiento();
        }
    };
}