package com.example.dogbreedslist.data.local.favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "favorites")
data class FavoriteData @JvmOverloads constructor(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "photo") var photoUrl: String? = null,
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
)