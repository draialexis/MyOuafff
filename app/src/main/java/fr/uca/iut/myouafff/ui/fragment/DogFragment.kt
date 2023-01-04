package fr.uca.iut.myouafff.ui.fragment

import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import fr.uca.iut.myouafff.data.Dog
import fr.uca.iut.myouafff.data.NEW_DOG_ID

class DogFragment : Fragment() {

    companion object {
        private const val EXTRA_DOG_ID = "fr.iut.ouafff.extra_dogid"
        private const val REQUEST_DATE = "DateRequest"
        private const val DIALOG_DATE = "DateDialog"

        fun newInstance(dogId: Long) = DogFragment().apply {
            arguments = bundleOf(EXTRA_DOG_ID to dogId)
        }
    }

    private lateinit var dog : Dog
    private var dogId: Long = NEW_DOG_ID

    private lateinit var dogNameEditor: EditText
    private lateinit var dogBreedEditor: EditText
    private lateinit var genderSpinner: Spinner
    private lateinit var dogWeightEditor: EditText
    private lateinit var aggressivenessRatingBar: RatingBar
    private lateinit var dogOwnerText: TextView
    private lateinit var dogAdmissionDateText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(REQUEST_DATE, this::onAdmissionDateChanged)
    }

    private fun onAdmissionDateChanged(requestKey: String, bundle: Bundle) {
        if(requestKey == REQUEST_DATE) {
            val year = bundle.getInt(DatePickerFragment.EXTRA_YEAR)
        }
    }
}