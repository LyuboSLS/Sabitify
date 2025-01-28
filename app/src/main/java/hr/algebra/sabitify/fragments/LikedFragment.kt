package hr.algebra.sabitify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.sabitify.R
import hr.algebra.sabitify.adapter.EventAdapter
import hr.algebra.sabitify.databinding.FragmentEventsBinding
import hr.algebra.sabitify.model.Item


class LikedFragment : Fragment() {

    private lateinit var binding: FragmentEventsBinding
    private lateinit var items: MutableList<Item>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = EventAdapter(requireContext(), items)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_liked, container, false)
    }
}