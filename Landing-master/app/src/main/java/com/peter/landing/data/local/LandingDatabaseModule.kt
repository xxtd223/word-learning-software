package com.peter.landing.data.local

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.peter.landing.data.local.affix.AffixCatalogDAO
import com.peter.landing.data.local.affix.AffixDAO
import com.peter.landing.data.local.help.HelpCatalogDAO
import com.peter.landing.data.local.help.HelpDAO
import com.peter.landing.data.local.history.SearchHistoryDAO
import com.peter.landing.data.local.ipa.IpaDAO
import com.peter.landing.data.local.note.NoteDAO
import com.peter.landing.data.local.plan.StudyPlanDAO
import com.peter.landing.data.local.progress.*
import com.peter.landing.data.local.vocabulary.VocabularyDAO
import com.peter.landing.data.local.vocabulary.VocabularyViewDAO
import com.peter.landing.data.local.word.WordDAO
import com.peter.landing.data.local.wrong.WrongDAO
import com.peter.landing.data.util.DATABASE_NAME
import com.peter.landing.data.util.DATABASE_PATH
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LandingDatabaseModule {

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // 删除可能存在的旧索引
            database.execSQL("DROP INDEX IF EXISTS `idx_wrong_study_progress_id`")
            database.execSQL("DROP INDEX IF EXISTS `index_wrong_study_progress_id`")
            
            // 创建新索引
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_wrong_study_progress_id` ON `wrong` (`study_progress_id`)")
        }
    }

    @Provides
    @Singleton
    fun provideLandingDatabase(application: Application): LandingDatabase {
        return Room.databaseBuilder(
            application,
            LandingDatabase::class.java,
            DATABASE_NAME
        )
        .createFromAsset(DATABASE_PATH)
        .addMigrations(MIGRATION_1_2)
        .build()
    }

    @Provides
    @Singleton
    fun provideHelpCatalogDAO(LandingDatabase: LandingDatabase): HelpCatalogDAO {
        return LandingDatabase.getHelpCatalogDAO()
    }

    @Provides
    @Singleton
    fun provideHelpDAO(LandingDatabase: LandingDatabase): HelpDAO {
        return LandingDatabase.getHelpDAO()
    }

    @Provides
    @Singleton
    fun provideStudyPlanDAO(LandingDatabase: LandingDatabase): StudyPlanDAO {
        return LandingDatabase.getStudyPlanDAO()
    }

    @Provides
    @Singleton
    fun provideStudyProgressDAO(LandingDatabase: LandingDatabase): StudyProgressDAO {
        return LandingDatabase.getStudyProgressDAO()
    }

    @Provides
    @Singleton
    fun provideWordDAO(LandingDatabase: LandingDatabase): WordDAO {
        return LandingDatabase.getWordDAO()
    }

    @Provides
    @Singleton
    fun provideVocabularyDAO(LandingDatabase: LandingDatabase): VocabularyDAO {
        return LandingDatabase.getVocabularyDAO()
    }

    @Provides
    @Singleton
    fun provideVocabularyViewDAO(LandingDatabase: LandingDatabase): VocabularyViewDAO {
        return LandingDatabase.getVocabularyViewDAO()
    }

    @Provides
    @Singleton
    fun provideNoteDAO(LandingDatabase: LandingDatabase): NoteDAO {
        return LandingDatabase.getNoteDAO()
    }

    @Provides
    @Singleton
    fun provideWrongDAO(LandingDatabase: LandingDatabase): WrongDAO {
        return LandingDatabase.getWrongDAO()
    }

    @Provides
    @Singleton
    fun provideSearchHistoryDAO(LandingDatabase: LandingDatabase): SearchHistoryDAO {
        return LandingDatabase.getSearchHistoryDAO()
    }

    @Provides
    @Singleton
    fun provideIpaDAO(LandingDatabase: LandingDatabase): IpaDAO {
        return LandingDatabase.getIpaDAO()
    }

    @Provides
    @Singleton
    fun provideAffixCatalogDAO(LandingDatabase: LandingDatabase): AffixCatalogDAO {
        return LandingDatabase.getAffixCatalogDAO()
    }

    @Provides
    @Singleton
    fun provideAffixDAO(LandingDatabase: LandingDatabase): AffixDAO {
        return LandingDatabase.getAffixDAO()
    }

}