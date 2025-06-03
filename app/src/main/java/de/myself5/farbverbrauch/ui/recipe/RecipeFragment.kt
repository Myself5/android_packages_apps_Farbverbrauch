package de.myself5.farbverbrauch.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.myself5.farbverbrauch.R
import de.myself5.farbverbrauch.databinding.FragmentRecipeBinding
import org.json.JSONArray
import kotlin.random.Random

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

        val recipe: AutoCompleteTextView = binding.recipeInput
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
            arrayOf(
                binding.colorContent1,
                binding.colorContent2,
                binding.colorContent3,
                binding.colorContent4,
                binding.colorContent5
            )
        val amount =
            arrayOf(
                binding.colorAmount1,
                binding.colorAmount2,
                binding.colorAmount3,
                binding.colorAmount4,
                binding.colorAmount5
            )

        binding.recipeHelp.setOnClickListener({
            recipe.setText(
                array.getJSONObject(Random.nextInt(array.length())).get(KEY_COLORNAME).toString()
            )
            binding.recipeHelp.visibility = View.INVISIBLE
            binding.recipeHelp.isClickable = false
            binding.showReceiptHelp.visibility = View.VISIBLE
            binding.showReceiptHelp.isClickable = true
        })

        binding.showReceiptHelp.setOnClickListener({
            binding.showReceipt.isPressed = true
            binding.showReceipt.performClick()
            binding.showReceiptHelp.visibility = View.INVISIBLE
            binding.showReceiptHelp.isClickable = false
            binding.showReceipt.isPressed = false
        })

        binding.showReceipt.setOnClickListener({
            showContents(recipeKeys, array, content, amount)
        })

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.help_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_showHelpArrows -> {
                        binding.recipeHelp.visibility = View.VISIBLE
                        binding.recipeHelp.isClickable = true
                        binding.showReceiptHelp.visibility = View.INVISIBLE
                        binding.showReceiptHelp.isClickable = false

                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage(R.string.help_dialog_text)
                            .setPositiveButton(R.string.ok) { _, _ -> }
                        val dialog = builder.create()
                        dialog.show()
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        }, viewLifecycleOwner)

        return root
    }

    private fun showContents(
        recipeKeys: ArrayList<String>,
        array: JSONArray,
        content: Array<TextView>,
        amount: Array<TextView>
    ) {
        if (recipeKeys.contains(binding.recipeInput.text.toString())) {
            for (i in 0..<array.length()) {
                val obj = array.getJSONObject(i)
                if (obj.get(KEY_COLORNAME).equals(binding.recipeInput.text.toString())) {
                    for (i in 0..4) {
                        content[i].text = obj.get(KEY_COLOR + (i + 1)).toString()
                        amount[i].text = obj.get(KEY_AMOUNT + (i + 1)).toString()
                    }
                    break
                }
            }
        } else {
            if (binding.recipeInput.text.isNotBlank()) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(R.string.invalid_entry)
                    .setPositiveButton(R.string.ok) { _, _ -> }
                val dialog = builder.create()
                dialog.show()
            }
            for (i in 0..4) {
                content[i].text = ""
                amount[i].text = ""
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}