package com.ulp.myfototrabajo3.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ulp.myfototrabajo3.model.Usuario;
import com.ulp.myfototrabajo3.request.ApiCliente;
import com.ulp.myfototrabajo3.ui.registro.RegistroActivity;

public class MainActivityViewModel extends AndroidViewModel {
    private Context context;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }
    public void Login(String email, String password){
        Usuario usuario = ApiCliente.login(context, email, password);
        if( usuario != null ){
            Intent intent = new Intent(context, RegistroActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("flag", 1);
            context.startActivity(intent);
        }  else {
            Toast.makeText(context, "Mail o Password incorrecto.", Toast.LENGTH_LONG).show();
        }
    }

    public void Registro(){
        Intent intent = new Intent(context, RegistroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
