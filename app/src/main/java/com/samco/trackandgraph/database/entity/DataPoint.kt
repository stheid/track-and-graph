/*
* This file is part of Track & Graph
* 
* Track & Graph is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* Track & Graph is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Track & Graph.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.samco.trackandgraph.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.samco.trackandgraph.database.doubleFormatter
import com.samco.trackandgraph.ui.formatTimeDuration
import org.threeten.bp.OffsetDateTime


interface IDataPoint {
    val timestamp: OffsetDateTime
    val featureId: Long
    val value: Double
    val label: String
    val note: String
}

@Entity(
    tableName = "data_points_table",
    primaryKeys = ["timestamp", "feature_id"],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Feature::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("feature_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class DataPoint(
    @ColumnInfo(name = "timestamp")
    override val timestamp: OffsetDateTime = OffsetDateTime.now(),

    @ColumnInfo(name = "feature_id", index = true)
    override val featureId: Long,

    @ColumnInfo(name = "value")
    override val value: Double,

    @ColumnInfo(name = "label")
    override val label: String,

    @ColumnInfo(name = "note")
    override val note: String
) : IDataPoint {
    companion object {
        fun getDisplayValue(dataPoint: IDataPoint, featureType: FeatureType): String {
            return when (featureType) {
                FeatureType.DISCRETE -> doubleFormatter.format(dataPoint.value) + " : ${dataPoint.label}"
                FeatureType.CONTINUOUS -> doubleFormatter.format(dataPoint.value)
                FeatureType.DURATION -> formatTimeDuration(dataPoint.value.toLong())
            }
        }
    }
}