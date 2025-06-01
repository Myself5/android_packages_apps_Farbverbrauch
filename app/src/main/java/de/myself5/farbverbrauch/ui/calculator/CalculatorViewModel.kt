package de.myself5.farbverbrauch.ui.calculator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    var KEY_CONSUMPTION_JSON = "consumption.json"

    private val _array = MutableLiveData<JSONArray>().apply {
        val consumptionJson = application.assets.open(KEY_CONSUMPTION_JSON)
        val inputStream = InputStreamReader(consumptionJson)
        val reader = BufferedReader(inputStream)
        val builder = StringBuilder()

        var line = reader.readLine()

        while (line != null) {
            builder.append(line).append("\n")
            line = reader.readLine()
        }

        reader.close()
        inputStream.close()
        consumptionJson.close()

        val array = JSONArray(builder.toString().trim())

        value = array
    }

    val items: LiveData<JSONArray> = _array
}