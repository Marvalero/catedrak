package com.valero.catedrak.catedrapp.data;

import android.provider.BaseColumns;

/**
 * Created by valero on 27/11/2017.
 */

public class CatedrappContract {
    public static final class ListEntry implements BaseColumns {
        public static final String TABLE_NAME = "list";
        public static final String COLUMN_LIST_NAME = "listName";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
