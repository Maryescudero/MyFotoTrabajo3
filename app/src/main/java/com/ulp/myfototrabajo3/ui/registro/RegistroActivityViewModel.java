package com.ulp.myfototrabajo3.ui.registro;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ulp.myfototrabajo3.model.Usuario;
import com.ulp.myfototrabajo3.request.ApiCliente;
import com.ulp.myfototrabajo3.ui.login.MainActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegistroActivityViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Usuario> mUsuario;
    private MutableLiveData<Bitmap> mFoto;
    private MutableLiveData<String> registroError = new MutableLiveData<>(); // defino variable error de mis textview NO OLVIDAR TAMPOCO, SINO NO FUNCIONA MIS TOAST

    public RegistroActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }
    public LiveData<Usuario> getMUsuario() {
        if(mUsuario == null){
            mUsuario = new MutableLiveData<>();
        }
        return mUsuario;
    }

    public LiveData<Bitmap> getMFoto() {
        if(mFoto == null){
            mFoto = new MutableLiveData<>();
        }
        return mFoto;
    }

    public LiveData<String> getRegistroError() { // manejo error de textview
        return registroError;
    }

    public void LeerUsuario(){
        Usuario u = ApiCliente.leer(context);
        if( u != null) {
            mUsuario.setValue(u);
        }
    }

    public void GuardarUsuario(String dni, String apellido, String nombre, String email, String password) {
        if (areFieldsValid(dni, apellido, nombre, email, password)) {  // Validamos los campos antes de guardar
            Usuario u = new Usuario(Long.parseLong(dni), apellido, nombre, email, password);
            ApiCliente.guardar(context, u);
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            registroError.setValue("Todos los campos son obligatorios");  // Mostrar error si faltan campos
        }
    }

    private boolean areFieldsValid(String dni, String apellido, String nombre, String email, String password) {
        // Verificar que los campos no estén vacíos
        return !dni.trim().isEmpty() &&
                !apellido.trim().isEmpty() &&
                !nombre.trim().isEmpty() &&
                !email.trim().isEmpty() &&
                !password.trim().isEmpty();
    }

    public void respuestaCamara(int requestCode, int resultCode, @Nullable Intent data, int REQUEST_IMAGE_CAPTURE){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Recupero los datos provenientes de la camara.
            Bundle extras = data.getExtras();
            //Casteo a bitmap lo obtenido de la camara.
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

            //Rutina para convertir a un arreglo de byte los datos de la imagen
            byte [] b = baos.toByteArray();

            File archivo = new File(context.getFilesDir(), "foto.png");
            if(archivo.exists()){
                archivo.delete();
            }

            try{
                FileOutputStream fos = new FileOutputStream(archivo);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(b);
                bos.flush();
                bos.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            mFoto.setValue(imageBitmap);
        }
    }

    public void LeerFoto(String nombre){
        File archivo = new File(context.getFilesDir(), nombre);

        try {
            FileInputStream fis = new FileInputStream(archivo);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //ObjectInputStream ois = new ObjectInputStream(bis);

            byte b[];
            b = new byte[bis.available()];
            bis.read(b);

            Bitmap bm = BitmapFactory.decodeByteArray(b, 0, b.length);
            mFoto.setValue(bm);

            bis.close();
            fis.close();
        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
