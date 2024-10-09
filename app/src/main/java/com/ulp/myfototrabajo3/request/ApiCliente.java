package com.ulp.myfototrabajo3.request;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ulp.myfototrabajo3.model.Usuario;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ApiCliente implements Serializable { // NO OLVIDAR COLOCAR SIEMPRE EL SERIALIZABLE

    public static void guardar(Context context, Usuario usuario){
        Log.d("salida", usuario.toString());
        Log.d("salida", context.getFilesDir().toString());
        File archivo = new File(context.getFilesDir(), "usuario.dat");
        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream ous = new ObjectOutputStream(bos);
            ous.writeObject(usuario);
            bos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context,"Error al guardar", Toast.LENGTH_LONG).show();
        } catch (IOException io){
            io.printStackTrace();
            Toast.makeText(context,"Error de Entrada/Salida "+io.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static Usuario leer(Context context){
        File archivo = new File(context.getFilesDir(), "usuario.dat");
        if(!archivo.exists()){
            return null;
        }
        Usuario usuario = null;
        try {
            FileInputStream fis = new FileInputStream(archivo);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            usuario = (Usuario)ois.readObject();
            Log.d("salida", usuario.toString());
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context,"Error al guardar",Toast.LENGTH_LONG).show();
        } catch (ClassNotFoundException e) {
            Toast.makeText(context,"Error de Clase",Toast.LENGTH_LONG).show();
        } catch (IOException io) {
            Toast.makeText(context, "Error de Entrada/Salida", Toast.LENGTH_LONG).show();
        }
        return usuario;
    }

    public static Usuario login(Context context, String email, String pass){
        Usuario usuario = leer(context);
        if( usuario.getMail().equals(email) && usuario.getPassword().equals(pass)){
            return usuario;
        }
        return null;
    }
}
