package org.d3if1059.mobpro2.helloworld

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if1059.mobpro2.helloworld.model.Harian

class MainViewModel : ViewModel() {

    private val data = MutableLiveData<List<Harian>>()
    private val status = MutableLiveData<ApiStatus>()
    private val entries = MutableLiveData<List<Entry>>()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                requestData()
            }
        }
    }

    private fun getEntry(data: List<Harian>): List<Entry> {
        val result = ArrayList<Entry>()
        var index = 1f
        for (harian in data) {
            result.add(Entry(index, harian.jumlahPositif.value.toFloat()))
            index += 1
        }
        return result
    }

    private suspend fun requestData() {
        try {
            status.postValue(ApiStatus.LOADING)
            val result = Covid19Api.service.getData()
            data.postValue(result.update.harian)
            entries.postValue(getEntry(result.update.harian))
            status.postValue(ApiStatus.SUCCESS)
        } catch (e: Exception) {
            status.postValue(ApiStatus.FAILED)
        }
    }

    fun getData(): LiveData<List<Harian>> = data

    fun getStatus(): LiveData<ApiStatus> = status

    fun getEntries(): LiveData<List<Entry>> = entries

}