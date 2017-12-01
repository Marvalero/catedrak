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

    public static final class ItemListEntry implements BaseColumns {
        public static final String TABLE_NAME = "list_item";
        public static final String COLUMN_ITEM_NAME = "name";
        public static final String COLUMN_COMPLETED_AT = "completed_at";
        public static final String COLUMN_IDENTIFIER = "identifier";
        public static final String COLUMN_NOTES = "notes";
        public static final String COLUMN_LIST_ID = "list_id";
    }
}
