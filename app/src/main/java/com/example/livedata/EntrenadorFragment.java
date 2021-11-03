package com.example.livedata;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.p_livedata.databinding.FragmentEntrenadorBinding;


public class EntrenadorFragment extends Fragment {

    private FragmentEntrenadorBinding binding;
    String[] mes = {"nada","Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    int i = 1;
    int año = 2021;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentEntrenadorBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EntrenadorViewModel entrenadorViewModel = new ViewModelProvider(this).get(EntrenadorViewModel.class);

        entrenadorViewModel.obtenerEjercicio().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer ejercicio) {
                Glide.with(EntrenadorFragment.this).load(ejercicio).into(binding.ejercicio);
            }
        });

        entrenadorViewModel.obtenerRepeticion().observe(getViewLifecycleOwner(), new Observer<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(String repeticion) {
                if(repeticion.equals("CAMBIO1")) {
                    binding.cambio.setText(1 + " mes despues");
                    binding.cambio.setVisibility(View.VISIBLE);
                    i += 1;

                }else if(repeticion.equals("CAMBIO2")) {
                    binding.cambio.setText(2 + " meses despues");
                    binding.cambio.setVisibility(View.VISIBLE);
                    i += 2;
                }else if(repeticion.equals("CAMBIO3")) {
                    binding.cambio.setText(3 + " meses despues");
                    binding.cambio.setVisibility(View.VISIBLE);
                    i += 3;
                }else{
                    if (i < mes.length){
                        binding.repeticion.setText(mes[i] + "-" + año);
                    }else{
                        i = i - 12;
                        año++;
                        binding.repeticion.setText(mes[i] + "-" + año);
                    }
                    binding.cambio.setVisibility(View.GONE);
                }
            }
        });
    }
}