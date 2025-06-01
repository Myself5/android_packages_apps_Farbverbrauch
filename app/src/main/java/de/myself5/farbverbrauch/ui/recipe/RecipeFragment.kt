package de.myself5.farbverbrauch.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import de.myself5.farbverbrauch.databinding.FragmentRecipeBinding
import org.json.JSONArray

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    var KEY_COLORNAME = "colorname"
    var KEY_COLOR = "color_"
    var KEY_AMOUNT = "amount_"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun getColorNames(array: JSONArray): ArrayList<String> {
        val colors: ArrayList<String> = ArrayList()

        for (i in 0..<array.length()) {
            colors.add(array.getJSONObject(i).get(KEY_COLORNAME).toString())
        }

        return colors
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recipeViewModel =
            ViewModelProvider(this).get(RecipeViewModel::class.java)

        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recipe: AutoCompleteTextView = binding.rezept
        var array = JSONArray()
        var recipeKeys: ArrayList<String> = ArrayList()

        recipeViewModel.items.observe(viewLifecycleOwner) {
            array = it
            recipeKeys = getColorNames(array)
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                recipeKeys
            )
            recipe.setAdapter(adapter)
        }

        val content =
            arrayOf(binding.farbe1, binding.farbe2, binding.farbe3, binding.farbe4, binding.farbe5)
        val amount =
            arrayOf(binding.menge1, binding.menge2, binding.menge3, binding.menge4, binding.menge5)

        binding.showReceipt.setOnClickListener({
            if (recipeKeys.contains(recipe.text.toString())) {
                for (i in 0..<array.length()) {
                    val obj = array.getJSONObject(i)
                    if (obj.get(KEY_COLORNAME).equals(recipe.text.toString())) {
                        for (i in 0..4) {
                            content[i].text = obj.get(KEY_COLOR + (i + 1)).toString()
                            amount[i].text = obj.get(KEY_AMOUNT + (i + 1)).toString()
                        }
                        break
                    }
                }
            } else {
                for (i in 0..4) {
                    content[i].text = ""
                    amount[i].text = ""
                }
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}