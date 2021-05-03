package edu.mills.cs115.fruitthief.ui.filtertrees

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.mills.cs115.fruitthief.R
import edu.mills.cs115.fruitthief.database.FruitTreeDatabase
import edu.mills.cs115.fruitthief.databinding.FragmentFilterTreesBinding
import kotlinx.coroutines.runBlocking

class FilterTreesFragment : Fragment() {
    private lateinit var viewModel: FilterTreesViewModel

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil
            .inflate<FragmentFilterTreesBinding>(
                inflater, R.layout.fragment_filter_trees, container, false)

        viewModel = ViewModelProvider(this).get(FilterTreesViewModel::class.java)

        val dataSource =
            FruitTreeDatabase.getInstance(requireNotNull(this.activity).application).fruitTreeDAO

        binding.filterSpinner.adapter =
            this.context?.let {
                runBlocking {
                    ArrayAdapter(
                        it,
                        R.id.filterSpinner,
                        dataSource.getFruitNamesList()
                    )
                }
            }

        runBlocking {
            binding.filterSpinner.setSelection(dataSource.getFruitNamesList().size-1)
        }

        binding.filterSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>, view: View,
                position: Int, id: Long
            ) {
                val item = adapterView.getItemAtPosition(position)
                if (item != null) {
                    viewModel.onItemSelected(item.toString())
                } else { viewModel.onItemSelected("Any")}
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                viewModel.onItemSelected("Any")
            }
        }

        binding.filterButton.setOnClickListener{
            viewModel.onButtonClicked(dataSource)
        }

        return binding.root

    }
}