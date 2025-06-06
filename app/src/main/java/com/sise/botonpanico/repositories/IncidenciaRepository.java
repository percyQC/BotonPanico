package com.sise.botonpanico.repositories;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sise.botonpanico.entities.Incidencia;
import com.sise.botonpanico.shared.BaseResponse;
import com.sise.botonpanico.shared.Callback;
import com.sise.botonpanico.shared.Constants;
import com.sise.botonpanico.shared.ExceptionMessage;
import com.sise.botonpanico.shared.HttpUtil;

public class IncidenciaRepository {

    //retrofit
    public BaseResponse<Incidencia> insertarIncidencia(Incidencia incidencia) throws Exception{
        //hace peticion enviando el objeto incidencia como String, y devuelve la respuesta del API en string
        String response = HttpUtil.POST(Constants.BASE_URL_API,"incidencias", new Gson().toJson(incidencia));
        if(response == null){
            throw new Exception(ExceptionMessage.RESPONSE_IS_NULL);
        }
        //Convertir el response string en el objeto BaseResponse<Incidencia>
        BaseResponse<Incidencia> baseResponse = new Gson().fromJson(response, TypeToken.getParameterized(BaseResponse.class, Incidencia.class).getType());
        return baseResponse;
    }
}
