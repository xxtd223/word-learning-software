package com.peter.landing.data.local.plan

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface StudyPlanDAO {

    @Query("SELECT * FROM study_plan")
    fun getStudyPlanFlow(): Flow<StudyPlan?>

    @Query("SELECT * FROM study_plan WHERE id == 1")
    suspend fun getStudyPlan(): StudyPlan?

    @Upsert
    suspend fun upsertStudyPlan(plan: StudyPlan)

    @Query("DELETE FROM study_plan")
    suspend fun deleteStudyPlan()

    @Query(
        """
        SELECT start_date FROM study_plan 
        WHERE start_date IS NOT NULL 
        ORDER BY start_date ASC
        """
    )
    suspend fun getAllStartDates(): List<Calendar>

    @Query(
        """
    SELECT start_date FROM study_plan 
    WHERE start_date IS NOT NULL 
    ORDER BY start_date ASC
    """
    )
    fun getAllStartDatesFlow(): Flow<List<Calendar>>

}