package fr.uca.iut.myouafff.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import fr.uca.iut.myouafff.R
import fr.uca.iut.myouafff.ui.fragment.DogFragment
import fr.uca.iut.myouafff.ui.utils.DogPagerAdapter

class DogPagerActivity : AppCompatActivity(), DogFragment.OnInteractionListener {

    companion object {
        private const val EXTRA_DOG_ID = "fr.uca.iut.myouafff.extra_dog_id"

        fun getIntent(context: Context, dogId: Long) =
            Intent(context, DogPagerActivity::class.java).apply {
                putExtra(EXTRA_DOG_ID, dogId)
            }
    }

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)

        setSupportActionBar(findViewById(R.id.toolbar_activity))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewPager = ViewPager2(this)
        viewPager.id = R.id.view_pager
        findViewById<LinearLayout>(R.id.pager_layout).addView(viewPager)

        val adapter = DogPagerAdapter(this)
        viewPager.adapter = adapter
        val initialPosition = adapter.positionFromId(intent.getLongExtra(EXTRA_DOG_ID, 1))
        viewPager.currentItem = initialPosition
        supportActionBar?.subtitle = getString(R.string.dogs_subtitle_format, initialPosition + 1)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                supportActionBar?.subtitle = getString(R.string.dogs_subtitle_format, position + 1)
            }
        })
    }

    override fun onDogSaved() = finish()
    override fun onDogDeleted() = finish()
}