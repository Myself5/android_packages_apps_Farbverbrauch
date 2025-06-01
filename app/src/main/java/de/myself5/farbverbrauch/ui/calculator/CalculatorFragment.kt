package de.myself5.farbverbrauch.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.myself5.farbverbrauch.databinding.FragmentCalculatorBinding
import org.json.JSONArray

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

        val fabric: AutoCompleteTextView = binding.gewebe
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

        binding.berechnen.setOnClickListener({
            if (fabricKeys.contains(fabric.text.toString())
                && binding.anzDrucke.text.isNotBlank()
                && binding.bedruckungsgrad.text.isNotBlank()
                && binding.drucklaengeCM.text.isNotBlank()
                && binding.druckbreiteCM.text.isNotBlank()
            ) {
                for (i in 0..<array.length()) {
                    val obj = array.getJSONObject(i)
                    if (obj.get(KEY_FABRICNAME).equals(fabric.text.toString())) {
                        val factor = obj.getDouble(KEY_FACTOR)
                        val amountPrints =
                            Integer.parseInt(binding.anzDrucke.text.toString()).toFloat()
                        val printLength =
                            Integer.parseInt(binding.drucklaengeCM.getText().toString()).toFloat()
                        val printWidth =
                            Integer.parseInt(binding.druckbreiteCM.getText().toString()).toFloat()
                        val printDegree =
                            Integer.parseInt(binding.bedruckungsgrad.getText().toString()).toFloat()

                        binding.druckflaecheM2.text =
                            (amountPrints * printWidth * printLength).toString()
                        binding.farbmengeCM3.text =
                            (factor * amountPrints * printLength * printWidth * printDegree / 1000000).toString()
                        binding.farbmengeL.text =
                            (factor * amountPrints * printLength * printWidth * printDegree / 1000000000).toString()
                        break
                    }
                }
            } else {
                binding.druckflaecheM2.text = ""
                binding.farbmengeCM3.text = ""
                binding.farbmengeL.text = ""
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}