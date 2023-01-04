package fr.uca.iut.myouafff.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import fr.uca.iut.myouafff.R
import fr.uca.iut.myouafff.data.NEW_DOG_ID

class DogActivity : SimpleFragmentActivity(), DogFragment.OnInteractionListener {

    companion object {
        private const val EXTRA_DOG_ID = "fr.uca.iut.myouafff.extra_dog_id"

        fun getIntent(context: Context, dogId: Long) =
            Intent(context, DogActivity::class.java).apply {
                putExtra(EXTRA_DOG_ID, dogId)
            }

    }

    private var dogId = NEW_DOG_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        dogId = intent.getLongExtra(EXTRA_DOG_ID, NEW_DOG_ID)
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun createFragment() = DogFragment.newInstance(dogId)
    override fun getLayoutResId() = R.layout.toolbar_activity

    override fun onDogSaved() = finish()

    override fun onDogDeleted() = finish()
}