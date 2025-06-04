package hr.algebra.sabitify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.sabitify.adapter.EventAdapter
import hr.algebra.sabitify.databinding.FragmentLikedBinding
import hr.algebra.sabitify.framework.fetchItems
import hr.algebra.sabitify.model.Item


class LikedFragment : Fragment() {
    private lateinit var binding: FragmentLikedBinding
    private lateinit var items: MutableList<Item>
   private var likedItems: MutableList<Item> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        items = requireContext().fetchItems().toMutableList()
        items.forEach{
           if (it.liked) likedItems.add(it)
        }

        binding = FragmentLikedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView with the filtered list of read events
        binding.rvLikedItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = EventAdapter(requireContext(), likedItems)
        }
    }
    }
