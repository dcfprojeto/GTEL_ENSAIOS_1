package br.com.gtel.ensaios.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY createdAt DESC")
    fun observeReports(): Flow<List<ReportEntity>>

    @Insert
    suspend fun insert(report: ReportEntity): Long

    @Delete
    suspend fun delete(report: ReportEntity)
}
