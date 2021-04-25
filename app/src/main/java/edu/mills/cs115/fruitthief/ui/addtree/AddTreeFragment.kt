package edu.mills.cs115.fruitthief.ui.addtree

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.android.gms.maps.model.LatLng
import edu.mills.cs115.fruitthief.R
import edu.mills.cs115.fruitthief.database.FruitTreeDatabase
import edu.mills.cs115.fruitthief.database.Tree
import kotlin.properties.Delegates


class AddTreeFragment : Fragment() {
    lateinit var fruit: String
    lateinit var loc: LatLng

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_tree, container, false)
        val view =
            inflater.inflate(R.layout.fragment_add_tree, container, false)

        val spinner = view.findViewById(R.id.addTreeSpinner) as Spinner
        val button = view.findViewById(R.id.addTreeButton) as Button

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>, view: View,
                position: Int, id: Long
            ) {
                val item = adapterView.getItemAtPosition(position)
                if (item != null) {
                    fruit = item.toString()
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }

        val dataSource =
            FruitTreeDatabase.getInstance(requireNotNull(this.activity).application).fruitTreeDAO

        val dataAdapter =
            this.context?.let {
                ArrayAdapter(
                    it,
                    R.id.addTreeSpinner,
                    dataSource.getFruitNamesList()
                )
            }
        spinner.adapter = dataAdapter


        button.setOnClickListener{
            dataSource.insert(Tree(0, dataSource.getFruitByName(fruit).fruitId, loc.latitude, loc.longitude))
        }

        return view.rootView
    }

}