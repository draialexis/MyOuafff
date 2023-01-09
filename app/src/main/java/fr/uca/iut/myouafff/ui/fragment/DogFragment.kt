package fr.uca.iut.myouafff.ui.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.text.format.DateFormat
import android.view.*
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import fr.uca.iut.myouafff.R
import fr.uca.iut.myouafff.data.Dog
import fr.uca.iut.myouafff.data.NEW_DOG_ID
import fr.uca.iut.myouafff.data.persistence.DogDatabase
import fr.uca.iut.myouafff.data.persistence.converter.toGender
import fr.uca.iut.myouafff.ui.dialog.DatePickerFragment
import java.util.Date
import java.util.Calendar

class DogFragment : Fragment() {

    companion object {
        private const val EXTRA_DOG_ID = "fr.uca.iut.myouafff.extra_dogid"
        private const val REQUEST_DATE = "DateRequest"
        private const val DIALOG_DATE = "DateDialog"

        fun newInstance(dogId: Long) = DogFragment().apply {
            arguments = bundleOf(EXTRA_DOG_ID to dogId)
        }
    }

    private lateinit var dog: Dog
    private var dogId: Long = NEW_DOG_ID

    private lateinit var dogNameEditor: EditText
    private lateinit var dogBreedEditor: EditText
    private lateinit var genderSpinner: Spinner
    private lateinit var dogWeightEditor: EditText
    private lateinit var aggressivenessRatingBar: RatingBar
    private lateinit var dogOwnerText: TextView
    private lateinit var dogAdmissionDateText: TextView

    private val pickOwner =
        registerForActivityResult(ActivityResultContracts.PickContact()) { contactUri ->
            if (contactUri != null) {
                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                val contactCursor = activity?.contentResolver?.query(
                    contactUri, queryFields, null, null, null
                )

                contactCursor?.let {
                    if (it.count != 0) {
                        it.moveToFirst()
                        dog.owner = it.getString(0)
                    }
                    it.close()
                }
                dogOwnerText.text = dog.owner
            }
        }

    interface OnInteractionListener {
        fun onDogSaved()
        fun onDogDeleted()
    }

    private var listener: OnInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(REQUEST_DATE, this::onAdmissionDateChanged)

        dogId = savedInstanceState?.getLong(EXTRA_DOG_ID) ?: arguments?.getLong(EXTRA_DOG_ID)
                ?: NEW_DOG_ID

        dog = if (dogId == NEW_DOG_ID) {
            requireActivity().setTitle(R.string.title_add_dog)
            Dog()
        } else {
            DogDatabase.getInstance().dogDAO().findById(dogId)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(EXTRA_DOG_ID, dogId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dog, container, false)
        dogNameEditor = view.findViewById(R.id.dog_name_editor)
        dogBreedEditor = view.findViewById(R.id.dog_breed_editor)
        genderSpinner = view.findViewById(R.id.gender_spinner)
        dogWeightEditor = view.findViewById(R.id.dog_weight_editor)
        aggressivenessRatingBar = view.findViewById(R.id.aggressiveness_rating_bar)
        dogOwnerText = view.findViewById(R.id.dog_owner_text)
        dogAdmissionDateText = view.findViewById(R.id.dog_admission_date_text)

        updateViewFromCurrentDog()

        dogOwnerText.setOnClickListener {
            pickOwner.launch()
        }

        dogAdmissionDateText.setOnClickListener {
            val dateDialog = DatePickerFragment.newInstance(REQUEST_DATE, dog.admissionDate)
            dateDialog.show(parentFragmentManager, DIALOG_DATE)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                if (dogId == NEW_DOG_ID) {
                    menu.findItem(R.id.action_delete)?.isVisible = false
                }
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_dog, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_save -> {
                        saveDog()
                        true
                    }
                    R.id.action_delete -> {
                        deleteDog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun updateViewFromCurrentDog() {
        dogNameEditor.setText(dog.name)
        dogBreedEditor.setText(dog.breed)
        genderSpinner.setSelection(dog.gender.ordinal)
        dogWeightEditor.setText(dog.weight.toString())
        aggressivenessRatingBar.rating = dog.aggressiveness.toFloat()
        dogOwnerText.text = dog.owner
        dog.admissionDate?.let {
            dogAdmissionDateText.text = DateFormat.getDateFormat(activity).format(it)
        }
    }

    private fun saveDog() {
        val dogName = dogNameEditor.text.trim()
        val dogWeight = dogWeightEditor.text.trim()
        if (dogName.isEmpty() || dogWeight.isEmpty() || dogWeight == ".") {
            AlertDialog.Builder(requireActivity())
                .setTitle(R.string.create_dog_error_dialog_title)
                .setMessage(R.string.create_dog_error_message)
                .setNeutralButton(android.R.string.ok, null)
                .show()
            return
        }

        dog.name = dogName.toString()
        dog.breed = dogBreedEditor.text.toString()
        dog.gender = genderSpinner.selectedItemPosition.toGender()
        dog.weight = dogWeight.toString().toFloat()
        dog.aggressiveness = aggressivenessRatingBar.rating.toInt()

        if (dog.id == NEW_DOG_ID)
            DogDatabase.getInstance().dogDAO().insert(dog)
        else
            DogDatabase.getInstance().dogDAO().update(dog)

        listener?.onDogSaved()
    }

    private fun deleteDog() {
        if (dogId != NEW_DOG_ID) {
            DogDatabase.getInstance().dogDAO().delete(dog)
            listener?.onDogDeleted()
        }
    }

    private fun onAdmissionDateChanged(requestKey: String, bundle: Bundle) {
        if (requestKey == REQUEST_DATE) {
            val year = bundle.getInt(DatePickerFragment.EXTRA_YEAR)
            val month = bundle.getInt(DatePickerFragment.EXTRA_MONTH)
            val day = bundle.getInt(DatePickerFragment.EXTRA_DAY)

            val cal = Calendar.getInstance()
            cal.set(year, month, day)
            dog.admissionDate = Date().apply { time = cal.timeInMillis }
            dogAdmissionDateText.text = dog.admissionDate?.let {
                DateFormat.getDateFormat(activity).format(it)
            } ?: ""
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}