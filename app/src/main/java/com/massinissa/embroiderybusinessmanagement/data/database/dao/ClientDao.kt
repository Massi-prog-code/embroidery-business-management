package com.massinissa.embroiderybusinessmanagement.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.massinissa.embroiderybusinessmanagement.data.database.entities.Client

@Dao
interface ClientDao {

    @Query("SELECT * FROM clients ORDER BY name ASC")
    fun getAllClients(): LiveData<List<Client>>

    @Query("SELECT * FROM clients WHERE id = :clientId")
    fun getClientById(clientId: Long): LiveData<Client>

    @Query("SELECT * FROM clients WHERE name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%'")
    fun searchClients(query: String): LiveData<List<Client>>

    @Insert
    suspend fun insertClient(client: Client): Long

    @Update
    suspend fun updateClient(client: Client)

    @Delete
    suspend fun deleteClient(client: Client)
}