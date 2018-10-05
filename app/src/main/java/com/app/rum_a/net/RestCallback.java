package com.app.rum_a.net;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Harish on 24/08/18.
 */
public interface RestCallback<T> {

    public void onFailure(Call<T> call, Throwable t, int serviceMode);

    public void onSuccess(Call<T> call, Response<T> model, int serviceMode);

    void onLogout();

}