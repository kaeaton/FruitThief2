package edu.mills.cs115.fruitthief.ui.addtree

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import edu.mills.cs115.fruitthief.R
import edu.mills.cs115.fruitthief.database.FruitTreeDatabase
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.mills.cs115.fruitthief.databinding.FragmentAddTreeBinding
import kotlinx.coroutines.runBlocking


class AddTreeFragment : Fragment() {
    private lateinit var viewModel: AddTreeViewModel

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil
            .inflate<FragmentAddTreeBinding>(
                inflater, R.layout.fragment_add_tree, container, false
            )

        viewModel = ViewModelProvider(this).get(AddTreeViewModel::class.java)

        val dataSource =
            FruitTreeDatabase.getInstance(requireNotNull(this.activity).application).fruitTreeDAO

        binding.addTreeSpinner.adapter =
            this.context?.let {
                runBlocking {
                    ArrayAdapter(
                        it,
                        R.layout.support_simple_spinner_dropdown_item,
                        dataSource.getFruitNamesList()
                    )
                }
            }

        runBlocking {
            binding.addTreeSpinner.setSelection(dataSource.getFruitNamesList().size - 1)
        }

        binding.addTreeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>, view: View,
                position: Int, id: Long
            ) {
                val item = adapterView.getItemAtPosition(position)
                viewModel.onItemSelected(item?.toString() ?: "Unknown")
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                viewModel.onItemSelected("Unknown")
            }
        }

        viewModel.setLocation(
            requireArguments().getDouble("lat"),
            requireArguments().getDouble("long")
        )

        binding.addTreeButton.setOnClickListener {
            viewModel.onButtonClicked(dataSource)
            val navController = findNavController()
            navController.navigate(R.id.action_addTreeFragment_to_mapFragment)
        }

        return binding.root
    }

}