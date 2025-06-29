package hr.algebra.sabitify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.sabitify.adapter.EventAdapter
import hr.algebra.sabitify.databinding.FragmentEventsBinding
import hr.algebra.sabitify.framework.fetchItems
import hr.algebra.sabitify.model.Item


class EventsFragment : Fragment() {
    private lateinit var binding: FragmentEventsBinding
    private lateinit var items: MutableList<Item>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        items = requireContext().fetchItems()
        binding = FragmentEventsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = EventAdapter(requireContext(), items)
        }
    }

}