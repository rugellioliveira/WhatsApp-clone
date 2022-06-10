package com.example.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode){

        //recupera numero para saber a versão do android que estamos utilizando
        //versões anteriores a 23 não é neccesário validação
        if(Build.VERSION.SDK_INT <= 22){

            List<String> listaPermissoes = new ArrayList<>();

            //Percorre as permissões passadas
            for (String permissao : permissoes){
                //verificando uma a uma == se já tem a permissao liberada
                Boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                //solicita permissões que ainda não temos
                if (!temPermissao) listaPermissoes.add(permissao);

            }

            //Caso a lista esteja vazia, não pe neccessário solicitar permissão
            if(listaPermissoes.isEmpty()) return true;
            String[] novasPermissoes = new  String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //solicita permissão
            ActivityCompat.requestPermissions(activity,novasPermissoes, requestCode );


        }

        return true;
    }

}
