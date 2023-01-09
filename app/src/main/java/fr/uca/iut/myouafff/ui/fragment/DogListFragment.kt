package fr.uca.iut.myouafff.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.Group
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.uca.iut.myouafff.R
import fr.uca.iut.myouafff.data.Dog
import fr.uca.iut.myouafff.data.persistence.DogDatabase
import fr.uca.iut.myouafff.ui.utils.DogRecyclerViewAdapter

class DogListFragment : Fragment(), DogRecyclerViewAdapter.Callbacks {
    private var dogList = DogDatabase.getInstance().dogDAO().getAll()
    private var dogListAdapter = DogRecyclerViewAdapter(dogList, this)

    private lateinit var groupEmptyView: Group

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_dog, container, false)
        groupEmptyView = view.findViewById(R.id.group_empty_view)
        groupEmptyView.visibility = if (dogList.isEmpty()) View.VISIBLE else View.GONE
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = dogListAdapter
        ItemTouchHelper(DogListItemTouchHelper()).attachToRecyclerView(recyclerView)
        view.findViewById<FloatingActionButton>(R.id.fab_add_dog).setOnClickListener { addNewDog() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_list_dog, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_item_new_dog -> {
                        addNewDog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    fun updateList() {
        dogList = DogDatabase.getInstance().dogDAO().getAll()
        dogListAdapter.updateList(dogList)
        groupEmptyView.visibility = if (dogList.isEmpty()) View.VISIBLE else View.GONE
    }

    private var listener: OnInteractionListener? = null

    private fun addNewDog() {
        listener?.onAddNewDog()
    }

    override fun onDogSelected(dogId: Long) {
        listener?.onDogSelected(dogId)
    }

    private inner class DogListItemTouchHelper : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ) =
            makeMovementFlags(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.START or ItemTouchHelper.END
            )

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            (viewHolder as DogRecyclerViewAdapter.DogViewHolder).dog?.also {
                removeDog(it)
                listener?.onDogSwiped()
            }
        }
    }

    private fun removeDog(dog: Dog) {
        val dao = DogDatabase.getInstance().dogDAO()
        dao.delete(dog)
        updateList()
    }

    interface OnInteractionListener {
        fun onDogSelected(dogId: Long)
        fun onAddNewDog()
        fun onDogSwiped()
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