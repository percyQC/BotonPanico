package com.sise.botonpanico.repositories;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sise.botonpanico.entities.Usuario;
import com.sise.botonpanico.shared.BaseResponse;
import com.sise.botonpanico.shared.Callback;
import com.sise.botonpanico.shared.Constants;
import com.sise.botonpanico.shared.HttpUtil;

public class UsuarioRepository {

    public void insertarUsuario(Usuario usuario, Callback<Usuario> callback) {
        new Thread(() -> {
            try {
                String response = HttpUtil.POST(Constants.BASE_URL_API, "/usuarios", new Gson().toJson(usuario));

                if (response == null) {
                    callback.onFailure();
                    return;
                }

                BaseResponse<Usuario> baseResponse = new Gson().fromJson(response, TypeToken.getParameterized(BaseResponse.class, Usuario.class).getType());

                if (baseResponse == null) {
                    callback.onFailure();
                    return;
                }
                if (!baseResponse.isSuccess()) {
                    callback.onFailure();
                    return;
                }
                callback.onSuccess(baseResponse.getData());
            } catch (Exception e) {
                e.printStackTrace();
                callback.onFailure();             }
        }).start();
    }
}
