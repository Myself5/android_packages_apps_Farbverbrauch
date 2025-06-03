package de.myself5.farbverbrauch.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.myself5.farbverbrauch.R
import de.myself5.farbverbrauch.databinding.FragmentCalculatorBinding
import org.json.JSONArray
import kotlin.random.Random

class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null
    var KEY_FABRICNAME = "fabric"
    var KEY_FACTOR = "factor"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun getFabricName(array: JSONArray): ArrayList<String> {
        val fabrics: ArrayList<String> = ArrayList()

        for (i in 0..<array.length()) {
            fabrics.add(array.getJSONObject(i).get(KEY_FABRICNAME).toString())
        }

        return fabrics
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val calculatorViewModel =
            ViewModelProvider(this).get(CalculatorViewModel::class.java)

        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val fabric: AutoCompleteTextView = binding.fabric
        var array = JSONArray()
        var fabricKeys: ArrayList<String> = ArrayList()

        calculatorViewModel.items.observe(viewLifecycleOwner) {
            array = it
            fabricKeys = getFabricName(array)
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                fabricKeys
            )
            fabric.setAdapter(adapter)
        }

        binding.calculate.setOnClickListener({
            calculateResult(fabricKeys, array)
        })

        var previousStepCompleted = false

        binding.valueInputHelp.setOnClickListener({
            fabric.setText(
                array.getJSONObject(Random.nextInt(array.length())).get(KEY_FABRICNAME).toString()
            )
            binding.printAmount.setText("50")
            binding.printDegree.setText("27")
            binding.printLengthCm.setText("30")
            binding.printWidthCm.setText("20")
            binding.valueInputHelp.visibility = View.INVISIBLE
            binding.valueInputHelp.isClickable = false
            binding.calculateButtonHelp.visibility = View.VISIBLE
            binding.calculateButtonHelp.isClickable = true
        })

        binding.calculateButtonHelp.setOnClickListener({
            binding.calculate.isPressed = true
            binding.calculate.performClick()
            binding.calculateButtonHelp.visibility = View.INVISIBLE
            binding.calculateButtonHelp.isClickable = false
            binding.calculate.isPressed = false
        })

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.help_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_showHelpArrows -> {
                        binding.valueInputHelp.visibility = View.VISIBLE
                        binding.valueInputHelp.isClickable = true
                        binding.calculateButtonHelp.visibility = View.INVISIBLE
                        binding.calculateButtonHelp.isClickable = false

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

    private fun calculateResult(
        fabricKeys: ArrayList<String>,
        array: JSONArray
    ) {
        if (fabricKeys.contains(binding.fabric.text.toString())
            && binding.printAmount.text.isNotBlank()
            && binding.printDegree.text.isNotBlank()
            && binding.printLengthCm.text.isNotBlank()
            && binding.printWidthCm.text.isNotBlank()
        ) {
            for (i in 0..<array.length()) {
                val obj = array.getJSONObject(i)
                if (obj.get(KEY_FABRICNAME).equals(binding.fabric.text.toString())) {
                    val factor = obj.getDouble(KEY_FACTOR)
                    val amountPrints =
                        Integer.parseInt(binding.printAmount.text.toString()).toFloat()
                    val printLength =
                        Integer.parseInt(binding.printLengthCm.getText().toString()).toFloat()
                    val printWidth =
                        Integer.parseInt(binding.printWidthCm.getText().toString()).toFloat()
                    val printDegree =
                        Integer.parseInt(binding.printDegree.getText().toString()).toFloat()

                    val area = (amountPrints * printWidth * printLength / 10000)
                    binding.printArea.text = area.toString()
                    binding.colorAmountCm3.text = (factor * area * printDegree / 100).toString()
                    binding.colorAmountL.text = (factor * area * printDegree / 100000).toString()
                    break
                }
            }
        } else {
            if (binding.fabric.text.isNotBlank()
                && binding.printAmount.text.isNotBlank()
                && binding.printDegree.text.isNotBlank()
                && binding.printLengthCm.text.isNotBlank()
                && binding.printWidthCm.text.isNotBlank()
            ) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(R.string.invalid_entry)
                    .setPositiveButton(R.string.ok) { _, _ -> }
                val dialog = builder.create()
                dialog.show()
            }
            binding.printArea.text = ""
            binding.colorAmountCm3.text = ""
            binding.colorAmountL.text = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}