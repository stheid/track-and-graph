/*
 *  This file is part of Track & Graph
 *
 *  Track & Graph is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Track & Graph is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Track & Graph.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.samco.trackandgraph.functionslib.aggregation

import com.samco.trackandgraph.database.entity.IDataPoint
import org.threeten.bp.OffsetDateTime

data class AggregatedDataPoint(
    val timestamp: OffsetDateTime,
    val featureId: Long,
    val parents: List<IDataPoint>,
    val label: String = "",
    val note: String = "",
) {
    fun toDataPoint(value: Double): IDataPoint {
        return object : IDataPoint {
            override val timestamp = this@AggregatedDataPoint.timestamp
            override val featureId = this@AggregatedDataPoint.featureId
            override val value = value
            override val label = this@AggregatedDataPoint.label
            //TODO we should probably combine notes in some way and pass them in here
            override val note = this@AggregatedDataPoint.note
        }
    }
}
