package fr.uca.iut.myouafff.ui.utils

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import fr.uca.iut.myouafff.data.persistence.DogDatabase
import fr.uca.iut.myouafff.ui.fragment.DogFragment

class DogPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private var dogList = DogDatabase.getInstance().dogDAO().getAll()

    override fun getItemCount() = dogList.size

    override fun createFragment(position: Int) = DogFragment.newInstance(dogList[position].id)

    fun positionFromId(dogId: Long) = dogList.indexOfFirst { it.id == dogId }
}