package de.myself5.farbverbrauch.ui.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    var KEY_RECIPE_JSON = "recipes.json"

    private val _array = MutableLiveData<JSONArray>().apply {
        val recipeJson = application.assets.open(KEY_RECIPE_JSON)
        val inputStream = InputStreamReader(recipeJson)
        val reader = BufferedReader(inputStream)
        val builder = StringBuilder()

        var line = reader.readLine()

        while (line != null) {
            builder.append(line).append("\n")
            line = reader.readLine()
        }

        reader.close()
        inputStream.close()
        recipeJson.close()

        val array = JSONArray(builder.toString().trim())

        value = array
    }

    val items: LiveData<JSONArray> = _array

}