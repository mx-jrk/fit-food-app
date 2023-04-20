        package com.example.fitfood.data.data_sources.room.root;

        import android.content.Context;

        import androidx.room.Database;
        import androidx.room.Room;
        import androidx.room.RoomDatabase;

        import com.example.fitfood.data.data_sources.room.dao.PlanDAO;
        import com.example.fitfood.data.data_sources.room.entites.PlanEntity;

        @Database(entities = {PlanEntity.class}, version = 1, exportSchema = false)
        public abstract class PlanDatabase extends RoomDatabase {
            public abstract PlanDAO planDAO();
            private static final int NUMBER_OF_THREADS = 4;
            private static volatile PlanDatabase INSTANCE;

            public static PlanDatabase getDatabase(final Context context) {
                if (INSTANCE == null) {
                    synchronized (PlanDatabase.class) {
                        if (INSTANCE == null) {
                            INSTANCE = Room.databaseBuilder(context, PlanDatabase.class, "recipes_database.db")
                                    .createFromAsset("recipes_database.db")
                                    .build();
                        }
                    }
                }
                return INSTANCE;
            }
        }
