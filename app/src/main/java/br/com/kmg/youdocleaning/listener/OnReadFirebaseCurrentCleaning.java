package br.com.kmg.youdocleaning.listener;

import br.com.kmg.youdocleaning.model.Cleaning;

public interface OnReadFirebaseCurrentCleaning {
    void onReadCurrentCleaning(Cleaning cleaning);
}
