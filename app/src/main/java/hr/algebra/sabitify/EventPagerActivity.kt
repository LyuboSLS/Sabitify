package hr.algebra.sabitify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.sabitify.adapter.EventPagerAdapter
import hr.algebra.sabitify.databinding.ActivityEventPagerBinding
import hr.algebra.sabitify.framework.fetchItems
import hr.algebra.sabitify.model.Item

const val ITEM_POSITION = "hr.algebra.sabitify.item_position"

class EventPagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventPagerBinding
    private lateinit var items: MutableList<Item>

    private var itemPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPager()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initPager() {
        items = fetchItems()
        itemPosition = intent.getIntExtra(ITEM_POSITION, 0)
        binding.viewEventPager.adapter = EventPagerAdapter(this, items)
        binding.viewEventPager.currentItem = itemPosition
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}