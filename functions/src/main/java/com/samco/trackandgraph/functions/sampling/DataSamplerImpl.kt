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

package com.samco.trackandgraph.functions.sampling

import android.database.Cursor
import com.samco.trackandgraph.base.database.DataSource
import com.samco.trackandgraph.base.database.TrackAndGraphDatabaseDao
import com.samco.trackandgraph.functions.helpers.cache

class DataSamplerImpl(private val dao: TrackAndGraphDatabaseDao) :
    IDataSampler {
    private fun emptyDataSample() = DataSample.fromSequence(emptySequence())

    private fun dataSampleFromDb(cursor: Cursor): DataSample {
        val cursorSequence =
            DataPointCursorSequence(cursor)
        return DataSample.fromSequence(
            cursorSequence.cache(),
            DataSampleProperties(),
            cursorSequence::getRawDataPoints
        )
    }

    override fun getDataSampleForSource(dataSource: DataSource): DataSample {
        return when (dataSource) {
            is DataSource.FeatureDataSource -> {
                dataSampleFromDb(
                    dao.getDataPointsCursorForFeatureSync(dataSource.featureId)
                )
            }
            //TODO actually probably don't want two different types of data source. Just use feature
            // and a feature may point to a function and have data type FUNCTION .. ?
            is DataSource.FunctionDataSource -> emptyDataSample()
        }
    }
}