package br.com.gtel.ensaios.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val number: String = "GTEL-ENS-0001/2026",
    val client: String = "",
    val project: String = "",
    val testType: String = "Megger",
    val status: String = "Em preenchimento",
    val createdAt: Long = System.currentTimeMillis()
)
