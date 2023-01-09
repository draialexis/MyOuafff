package fr.uca.iut.myouafff.ui.activity

import android.os.Bundle
import android.widget.FrameLayout
import fr.uca.iut.myouafff.R
import fr.uca.iut.myouafff.data.NEW_DOG_ID
import fr.uca.iut.myouafff.ui.fragment.DogFragment
import fr.uca.iut.myouafff.ui.fragment.DogListFragment

class DogListActivity : SimpleFragmentActivity(),
    DogListFragment.OnInteractionListener, DogFragment.OnInteractionListener {

    private var isTwoPane: Boolean = false
    private lateinit var masterFragment: DogListFragment

    override fun createFragment() = DogListFragment().also { masterFragment = it }
    override fun getLayoutResId() = R.layout.toolbar_md_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setIcon(R.mipmap.ic_launcher)
        isTwoPane = findViewById<FrameLayout>(R.id.container_fragment_detail) != null
        if (savedInstanceState != null)
            masterFragment =
                supportFragmentManager.findFragmentById(R.id.container_fragment) as DogListFragment

        if (!isTwoPane) {
            removeDisplayedFragment()
        }
    }

    override fun onDogSelected(dogId: Long) {
        if (isTwoPane) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_fragment_detail, DogFragment.newInstance(dogId))
                .commit()
        } else {
            startActivity(DogPagerActivity.getIntent(this, dogId))
        }
    }

    override fun onAddNewDog() = startActivity(DogActivity.getIntent(this, NEW_DOG_ID))

    override fun onDogSaved() {
        if (isTwoPane) {
            masterFragment.updateList()
        }
    }

    private fun removeDisplayedFragment() {
        supportFragmentManager.findFragmentById(R.id.container_fragment_detail)?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    override fun onDogDeleted() {
        if (isTwoPane) {
            removeDisplayedFragment()
        } else {
            finish()
        }
    }

    override fun onDogSwiped() {
        if (isTwoPane) {
            removeDisplayedFragment()
        }
    }

}