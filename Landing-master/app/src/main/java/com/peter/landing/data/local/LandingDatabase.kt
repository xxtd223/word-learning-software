package com.peter.landing.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.data.local.affix.AffixCatalogDAO
import com.peter.landing.data.local.affix.AffixDAO
import com.peter.landing.data.local.help.Help
import com.peter.landing.data.local.help.HelpCatalog
import com.peter.landing.data.local.help.HelpCatalogDAO
import com.peter.landing.data.local.help.HelpDAO
import com.peter.landing.data.local.history.SearchHistory
import com.peter.landing.data.local.history.SearchHistoryDAO
import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.data.local.ipa.IpaDAO
import com.peter.landing.data.local.note.Note
import com.peter.landing.data.local.note.NoteDAO
import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.plan.StudyPlanDAO
import com.peter.landing.data.local.progress.*
import com.peter.landing.data.local.vocabulary.*
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.local.word.WordDAO
import com.peter.landing.data.local.wrong.Wrong
import com.peter.landing.data.local.wrong.WrongDAO
import com.peter.landing.data.util.Converters

@Database(
    entities = [
        HelpCatalog::class, Help::class,
        Word::class, Vocabulary::class,
        StudyPlan::class,
        StudyProgress::class,
        VocabularyBeginner::class,
        VocabularyIntermediate::class,
        Note::class, Wrong::class,
        SearchHistory::class,
        Ipa::class,
        AffixCatalog::class,
        Affix::class
    ],
    views = [
        VocabularyBeginnerView::class,
        VocabularyIntermediateView::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LandingDatabase : RoomDatabase() {

    abstract fun getHelpCatalogDAO(): HelpCatalogDAO
    abstract fun getHelpDAO(): HelpDAO
    abstract fun getWordDAO(): WordDAO
    abstract fun getVocabularyDAO(): VocabularyDAO
    abstract fun getVocabularyViewDAO(): VocabularyViewDAO
    abstract fun getStudyPlanDAO(): StudyPlanDAO
    abstract fun getStudyProgressDAO(): StudyProgressDAO
    abstract fun getNoteDAO(): NoteDAO
    abstract fun getWrongDAO(): WrongDAO
    abstract fun getSearchHistoryDAO(): SearchHistoryDAO
    abstract fun getIpaDAO(): IpaDAO
    abstract fun getAffixCatalogDAO(): AffixCatalogDAO
    abstract fun getAffixDAO(): AffixDAO

}