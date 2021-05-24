package org.tix.source

import kotlinx.coroutines.flow.Flow

interface Source<T> {
    fun source(): Flow<T>
}