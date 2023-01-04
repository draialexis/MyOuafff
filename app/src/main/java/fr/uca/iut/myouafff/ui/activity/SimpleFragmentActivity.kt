package fr.uca.iut.myouafff.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import fr.uca.iut.myouafff.R

abstract class SimpleFragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutResId())

        setSupportActionBar(findViewById(R.id.toolbar_activity))

        if (supportFragmentManager.findFragmentById(R.id.container_fragment) == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container_fragment, createFragment())
                .commit()
        }
    }

    protected abstract fun createFragment(): Fragment

    @LayoutRes
    protected abstract fun getLayoutResId(): Int
}