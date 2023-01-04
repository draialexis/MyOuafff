package fr.uca.iut.myouafff.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import fr.uca.iut.myouafff.R
import java.util.*

class DatePickerFragment : AppCompatDialogFragment() {
    companion object {
        const val EXTRA_YEAR = "fr.iut.uca.myouafff.year"
        const val EXTRA_MONTH = "fr.iut.uca.myouafff.month"
        const val EXTRA_DAY = "fr.iut.uca.myouafff.day"

        private const val ARG_DATE = "date"

        fun newInstance(requestKey: String, date: Date? = null) = DatePickerFragment().apply {
            this.requestKey = requestKey
            if (date != null)
                arguments = Bundle().apply {
                    putLong(ARG_DATE, date.time)
                }
        }
    }

    private lateinit var requestKey: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_date, null)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = arguments?.getLong(ARG_DATE) ?: Date().time
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = view as DatePicker
        datePicker.init(year, month, day, null)

        return AlertDialog.Builder(view.context, R.style.NarrowDialog)
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                setFragmentResult(
                    requestKey,
                    bundleOf(
                        EXTRA_YEAR to datePicker.year,
                        EXTRA_MONTH to datePicker.month,
                        EXTRA_DAY to datePicker.dayOfMonth
                    )
                )
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }
}