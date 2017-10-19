package co.edu.eafit.dis.parkingmovil.models;

import java.util.ArrayList;

/**
 * Created by alejocram on 29/06/16.
 */
public class ModelManager {
    public static final String TAG = ModelManager.class.getSimpleName();

    public ArrayList<Place> places = new ArrayList<>();

    // Singleton
    private static ModelManager mInstance;

    public ModelManager() {
        mInstance = this;
    }

    public static synchronized ModelManager getInstance() {
        if (mInstance == null){
            mInstance = new ModelManager();
        }
        return mInstance;
    }
}
